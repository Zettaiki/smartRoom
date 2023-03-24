#ifndef __SENSORMANAGER__
#define __SENSORMANAGER__

#include <Arduino.h>

#include "Led.h"
#include "Pir.h"
#define PIR_PIN 2
#define LED_PIN 5
#define LS 1

class SensorManager
{
public:
    SensorManager();
    void setup();
    void update();
    bool getPirStatus();
    int getLightSensorValue();
private:
    Light *led;
    Pir *pir;
};

#endif