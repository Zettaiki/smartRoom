#include "Pir.h"

Pir::Pir(int pin)
{
    this->pin = pin;
    pinMode(pin, INPUT);
}

void Pir::calibrate()
{
    Serial.print("Calibrating pir sensor...");
    for (int i = 0; i < CALIBRATION_TIME_SEC; i++)
    {
        Serial.print(".");
        delay(1000);
    }
    Serial.println(" done!");
    Serial.println("PIR SENSOR READY.");
    delay(50);
}

bool Pir::isDetected()
{
    return digitalRead(pin) == HIGH ? true : false;
}