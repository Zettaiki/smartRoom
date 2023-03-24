#include "AppStatusManager.h"

AppStatusManager::AppStatusManager() {
    appMod = AUTO;
    appCurrentLightState = OFF;
    appDegree = 0;
}

void AppStatusManager::update(SysControl appMod, State appCurrentLightState, int appDegree) {
    this->appMod = appMod;
    this->appCurrentLightState = appCurrentLightState;
    this->appDegree = appDegree;
}

//return a string that represent the current status of the app (modality, light state, degree)
String AppStatusManager::getAppStatus() {
    String modString = appMod == MAN ? "true" : "false";
    String appLightState = appCurrentLightState == ON ? "true" : "false";

    return  modString + " " + appLightState + " " + String(appDegree);
}