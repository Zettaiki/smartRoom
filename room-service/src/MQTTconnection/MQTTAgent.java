package MQTTconnection;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
//import io.vertx.ext.web.Router;
//import io.vertx.ext.web.RoutingContext;
//import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mqtt.MqttClient;
import sensors.Sensors;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * MQTT Agent
 */
public class MQTTAgent extends AbstractVerticle {
	
	private Sensors sensors;
	
	public MQTTAgent(Sensors sensors) {
		this.sensors = sensors;
	}

	@Override
	public void start() {		
		MqttClient client = MqttClient.create(vertx);

		client.connect(1883, "broker.mqtt-dashboard.com", c -> {

			log("connected");
			
			log("subscribing...");
			client.publishHandler(s -> {
			    // System.out.println("There are new message in topic: " + s.topicName());
			    // System.out.println("Content(as string) of the message: " + s.payload().toString());
			    // System.out.println("QoS: " + s.qosLevel());
				//System.out.println(s.payload().toString());
				updateSensors(s.payload().toString());
			})
			.subscribe("sensors_detection", 2);		

			log("publishing a msg");
			client.publish("sensors_detection",
				  Buffer.buffer("hello"),
				  MqttQoS.AT_LEAST_ONCE,
				  false,
				  false);
		});
	}
	

	private void log(String msg) {
		System.out.println("[MQTT AGENT] "+msg);
	}

	private void updateSensors(String message) {
		String[] values = message.split(" ");
		
		try {
			Integer movement = Integer.parseInt(values[0]);
			Integer photoresistor = Integer.parseInt(values[1]);
			if(movement == 1) {
				sensors.setPirStatus(true);
			} else { 
				sensors.setPirStatus(false);
			}
			
			sensors.setPhotoresistorValue(photoresistor);
		} catch(Exception e) {
			System.out.println("No value.");
		}
		
		System.out.println(sensors.toString());
	}
	
	
}