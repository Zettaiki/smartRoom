#ifndef __ENVVAR__
#define __ENVVAR__

#include <Arduino.h>

enum State {
    OFF = 0,
    ON = 1
};

enum SysControl {
    AUTO = 0, // based on day time and movement (pir sensor)
    MAN = 1   // from mobile app
};

#endif