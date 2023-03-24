#ifndef __APPSTATUSMANAGER__
#define __APPSTATUSMANAGER__

#include <Arduino.h>
#include "EnvVar.h"

class AppStatusManager {
public:
    AppStatusManager();
    void update(SysControl appMod, State appCurrentLightState, int appDegree);
    String getAppStatus();
private:
    SysControl appMod;
    State appCurrentLightState;
    int appDegree;
};
#endif