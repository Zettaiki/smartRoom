#include "ServoManager.h"

/**
 * Update servo position by using degrees (0°-180°).
*/
void ServoManager::update(int degrees) {
    int pulse = map(degrees, MIN_DEGREES, MAX_DEGREES, MIN_PULSE, MAX_PULSE);
    s.write(pulse);
}

/**
 * Attach servo.
 */
void ServoManager::setup() {
    s.attach(M);
}
