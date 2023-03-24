#ifndef __PIR__
#define __PIR__

#include <Arduino.h>

#define CALIBRATION_TIME_SEC 10

class Pir
{
public:
    Pir(int pin);
    void calibrate();
    bool isDetected();

private:
    int pin;
};

#endif