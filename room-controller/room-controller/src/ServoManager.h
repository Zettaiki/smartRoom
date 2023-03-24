#ifndef __SERVOMANAGER__
#define __SERVOMANAGER__

#include <Arduino.h>
#include <Servo.h>
#include "EnvVar.h"
//#include <ServoTimer2.h>

#define M 3             // servo-motor pin
#define MIN_PULSE 750   // 0° = 750 (min pulse width sent to servo)
#define MAX_PULSE 2250  // 180° = 2250 (max pulse width sent to servo)
#define MIN_DEGREES 0
#define MAX_DEGREES 180

class ServoManager {
public:
    void setup();
    void update(int pulse);
private:
    Servo s;
};

#endif