#include <BluetoothService.h>

SoftwareSerial btChannel(BT_RX_PIN, BT_TX_PIN);

BluetoothService::BluetoothService(String *msg) {
    this->msg = msg;
}

void BluetoothService::init() {
    btChannel.begin(BT_BAUDRATE);
    btChannel.print("AT");  // check if the module is in pairing mode
    delay(1000);
    btChannel.print("AT+NAMERoomController");   // set the name of the module to "RoomController"
    delay(1000);
    btChannel.print("AT+BAUD4");   // set the baud rate of the module to 9600
    delay(1000);
    btChannel.print("AT+PIN1234");   // set the pin code of the module to "1234"
    delay(1000);
}

void BluetoothService::update() {
    this->readMsg();
}

void BluetoothService::sendMsg(String msg) {
    btChannel.println(msg);
}

void BluetoothService::readMsg() {
    String tempMsg = "";
    while (btChannel.available()) {
        tempMsg += (char)btChannel.read();
    }
    *msg = tempMsg;
}