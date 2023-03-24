#ifndef __BLUETOOTHSERVICE__
#define __BLUETOOTHSERVICE__

#include <Arduino.h>
#include <SoftwareSerial.h>
#include "EnvVar.h"

#define BT_RX_PIN 10
#define BT_TX_PIN 11
#define BT_BAUDRATE 9600

class BluetoothService
{
public:
    BluetoothService(String *msg);
    void init();
    void update();
private:
    String *msg;
    void sendMsg(String msg);
    void readMsg();
};

#endif