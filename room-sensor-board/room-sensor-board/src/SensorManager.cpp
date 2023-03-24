#include "SensorManager.h"

SensorManager::SensorManager()
{
    this->led = new Led(LED_PIN);
    this->pir = new Pir(PIR_PIN);
    pinMode(LS, INPUT);
}

void SensorManager::setup()
{
    this->led->switchOff();
    this->pir->calibrate();
}

void SensorManager::update()
{
    bool detected = getPirStatus();
    int lsValue = getLightSensorValue();
    if (detected) {
        if (!led->isOn()) {
            led->switchOn();
        }
    } else {
        led->switchOff();
    }
}

bool SensorManager::getPirStatus()
{
    return pir->isDetected();
}

int SensorManager::getLightSensorValue()
{
    return analogRead(LS);
}