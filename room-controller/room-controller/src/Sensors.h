#ifndef __SENSORS__
#define __SENSORS__

#include <Arduino.h>

#define THL 900; // threshold light

class Sensors
{
public:
    Sensors();
    bool isPirOn();
    bool isLightLow();
    void update(bool pirStatus, int photoresistorValue);
    void setPirStatus(bool pirStatus);
    void setPhotoresistorValue(int photoresistorValue);

private:
    int thl;
    bool pir;
    int photoresistor;
};
#endif