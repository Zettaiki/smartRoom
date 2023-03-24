#include "Sensors.h"

Sensors::Sensors()
{
	this->pir = false;
	this->photoresistor = 0;
}

/**
 * @return true if the pir is on, false otherwise
 */
bool Sensors::isPirOn()
{
	return pir;
}

/**
 * @return true if the photoresistor value is greater than lightLimit. This parameter can be
 * specified in the constructor
 */
bool Sensors::isLightLow()
{
	return this->photoresistor <= THL;
}

/**
 * Update the status of the pir and the value of the photoresistor
 */
void Sensors::update(bool pirStatus, int photoresistorValue)
{
	setPirStatus(pirStatus);
	setPhotoresistorValue(photoresistorValue);
}

/**
 * @param pirStatus the new status of the pir. True if someone i detected in the room,
 *  false otherwise
 */
void Sensors::setPirStatus(bool pirStatus)
{
	this->pir = pirStatus;
}

/**
 * @param photoresistorValue the new value detected by the photoresistor
 */
void Sensors::setPhotoresistorValue(int photoresistorValue)
{
	this->photoresistor = photoresistorValue;
}