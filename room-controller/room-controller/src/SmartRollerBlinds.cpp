#include "SmartRollerBlinds.h"

SmartRollerBlinds::SmartRollerBlinds(SysControl *mod, Sensors *sensors, int *time, int *degree)
{
	this->servoManager = new ServoManager();
	this->mod = mod;
	this->sensors = sensors;
	this->time = time;
	this->degree = degree;
	this->currentDegree = FULLYDOWN;
}

void SmartRollerBlinds::setup()
{
	this->servoManager->setup();
}

void SmartRollerBlinds::update()
{
	if (*mod == AUTO)
	{
		// if it's day time and there's the first movement fully up
		if (sensors->isPirOn() && (*time >= 8 && *time < 19))
		{
			currentDegree = UP;
			servoManager->update(FULLYUP);
		}
		// if it's night time and there's no more movement fully down
		else if (!sensors->isPirOn() && *time < 8 && *time >= 19)
		{
			currentDegree = DOWN;
			servoManager->update(FULLYDOWN);
		}
	}
	else // if modality is manual from the mobile app
	{
		currentDegree = *degree;
		servoManager->update(*degree);
	}
}

int SmartRollerBlinds::getCurrentDegree()
{
	return this->currentDegree;
}