package main;

import MQTTconnection.MQTTAgent;
import arduinoconnector.ArduinoCommunication;
import dashbordconnection.WebsocketServer;
import io.vertx.core.Vertx;
import room.RoomStatus;
import sensors.Sensors;
import testthread.testThread;

public class Main {
	public static void main(String[] args) {

		
		System.out.println("running");
		
		Sensors sensors = new Sensors();
		RoomStatus room = new RoomStatus();
		
		WebsocketServer websocket =new WebsocketServer(room); 
		websocket.start();
		
		// testThread testThread = new testThread(websocket); 
		// testThread.start();
		
		// create MQTT client
		Vertx vertx = Vertx.vertx();
		MQTTAgent agent = new MQTTAgent(sensors);
		vertx.deployVerticle(agent);
		
		ArduinoCommunication arduinoCommunication = new ArduinoCommunication(sensors, room, websocket); 
		arduinoCommunication.start();
	}
}
