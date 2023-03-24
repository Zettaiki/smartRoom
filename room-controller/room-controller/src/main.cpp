//  Assignment #3 - Smart room.

//  NOME                              | EMAIL                                 | MATRICOLA
//  Luca Babboni                      | luca.babboni2@studio.unibo.it         | 0000971126
//  Sara Romeo                        | sara.romeo3@studio.unibo.it           | 0000969946
//  Pablo Sebastian Vargas Grateron   | pablo.vargasgrateron@studio.unibo.it  | 0000970487

#include <Arduino.h>
#include <TaskScheduler.h>
#include "EnvVar.h"
#include "MsgService.h"
#include "BluetoothService.h"
#include "Sensors.h"
#include "SmartLight.h"
#include "SmartRollerBlinds.h"
#include "AppStatusManager.h"

// Global variables.
SysControl mod;
int systemTime;
State lightState;
int blindDegree;

String btMsg; // msg received from app

// Modules.
Scheduler sched;
BluetoothService bluetoothService(&btMsg);
Sensors sensors;
SmartLight smartLight(&mod, &lightState, &sensors);
SmartRollerBlinds smartRollerBlinds(&mod, &sensors, &systemTime, &blindDegree);
AppStatusManager appStatusManager;

/**
 * Parse the message received from the serial and register global variables.
 */
void parseMsg(Msg *msg)
{
    String input = String(msg->getContent());
    String values[7];
    int lastIndex = 0, startIndex = 0, i=0;

    for (; i < (int)input.length(); i++)
    {
        if (input.charAt(i) == ' ')
        {
            values[lastIndex++] = input.substring(startIndex, i);
            startIndex = i + 1;
        }
    }
    values[lastIndex] = input.substring(startIndex, i); // store the last value

    values[0] == "true" ? mod = MAN : mod = AUTO;
    values[1] == "true" ? lightState = ON : lightState = OFF;
    blindDegree = values[2].toInt();
    values[3] == "true" ? sensors.setPirStatus(true) : sensors.setPirStatus(false);
    sensors.setPhotoresistorValue(values[4].toInt());
    systemTime = values[5].toInt();
}

/**
 * Tasks.
 */
void taskBluetooth()
{
    bluetoothService.update();
    if (btMsg != "")
    {
        // DEBUG
        // Serial.println("From bluetooth: " + btRxMsg);
        // Serial.println(btRxMsg.substring(8, 9)); // modality
        // Serial.println(btRxMsg.substring(16, 17)); // lightOn
        // Serial.println(btRxMsg.substring(25, 28)); // degree
        SysControl appMod = btMsg.substring(8, 9) == "1" ? MAN : AUTO;
        State appCurrentLightState = btMsg.substring(16, 17) == "1" ? ON : OFF;
        int appDegree = btMsg.substring(25, 28).toInt();
        appStatusManager.update(appMod, appCurrentLightState, appDegree);
    }
}

void taskSerial()
{
    if (MsgService.isMsgAvailable())
    {
        // Receive and parse the message to get parameters values.
        Msg *msg = MsgService.receiveMsg();
        parseMsg(msg);
        delete msg; // deallocate message

        // Send current state.
        // String appState; // from app: mod, lightOn, degree
        String roomState; // actual room state: mod, lightOn, degree

        mod == MAN ? roomState = "true " : roomState = "false ";
        smartLight.isLightOn() ? roomState += "true " : roomState += "false ";
        roomState += smartRollerBlinds.getCurrentDegree();

        String msgSending; // sends room state + app state manager to JAVA
        msgSending = roomState + " " + appStatusManager.getAppStatus() + " ";
        const char *msgChar = msgSending.c_str();

        // ROOM STATUS | APP STATUS
        // BOOL : mod | BOOL : lightOn | INT :  degree  || BOOL : mod | BOOL : lightOn | int : degree
        MsgService.sendMsg(msgChar);
    }
}

void taskSmartLight()
{
    smartLight.update();
}

void taskSmartRollerBlinds()
{
    smartRollerBlinds.update();
}

// TODO: set atomic tasks
Task t1(100, TASK_FOREVER, &taskBluetooth, &sched, true);
Task t2(100, TASK_FOREVER, &taskSerial, &sched, true);
Task t3(100, TASK_FOREVER, &taskSmartLight, &sched, true);
Task t4(100, TASK_FOREVER, &taskSmartRollerBlinds, &sched, true);

/**
 * Main setup.
 */
void setup()
{
    Serial.begin(9600);
    MsgService.init();
    bluetoothService.init();
    smartLight.setup();
    smartRollerBlinds.setup();
    systemTime = 9;
    Serial.println("Done setup.");
}

/**
 * Main loop.
 */
void loop()
{
    sched.execute();
}
