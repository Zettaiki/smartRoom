#include "SmartLight.h"

SmartLight::SmartLight(SysControl *mod, State *lightState, Sensors *sensors)
{
    this->mod = mod;
    this->lightState = lightState;
    this->sensors = sensors;
}

void SmartLight::setup()
{
    this->led = new Led(LIGHT_PIN);
}

void SmartLight::update()
{
    if (*mod == AUTO)
    {
        if (/*!isLightOn() &&*/ sensors->isPirOn() && sensors->isLightLow())
        {

            this->led->switchOn();
        }
        else
        {

            this->led->switchOff();
        }
    }
    else
    {
        if (*lightState == ON)
        {
            this->led->switchOn();
        }
        else
        {
            this->led->switchOff();
        }
    }
}

bool SmartLight::isLightOn()
{
    return this->led->isOn();
}