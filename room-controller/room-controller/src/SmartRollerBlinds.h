#ifndef __SMARTROLLERBLINDS__
#define __SMARTROLLERBLINDS__

#include <Arduino.h>
#include "EnvVar.h"
#include "Sensors.h"
#include "ServoManager.h"

#define FULLYUP 0
#define FULLYDOWN 180

enum RollerBlindState
{
    UP = FULLYUP,
    DOWN = FULLYDOWN
};

class SmartRollerBlinds
{
public:
    SmartRollerBlinds(SysControl *mod, Sensors *sensors, int *time, int *degree);
    void setup();
    void update();
    int getCurrentDegree();

private:
    ServoManager *servoManager;
    SysControl *mod;
    Sensors *sensors;
    int *time;
    int *degree;
    int currentDegree;
};
#endif
