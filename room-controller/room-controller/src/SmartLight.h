#ifndef __SMARTLIGHT__
#define __SMARTLIGHT__

#include <Arduino.h>
#include "EnvVar.h"
#include "Led.h"
#include "Sensors.h"

#define LIGHT_PIN LED_BUILTIN

class SmartLight
{
public:
    SmartLight(SysControl *mod, State *lightState, Sensors *sensors);
    void setup();
    void update();
    bool isLightOn();

private:
    Light *led;
    SysControl *mod;
    State *lightState;
    Sensors *sensors;
};
#endif