package arduinoconnector;

import java.time.Instant;
import java.time.LocalDateTime;

import dashbordconnection.WebsocketServer;
import room.RoomStatus;
import sensors.Sensors;

public class ArduinoCommunication extends Thread {
	
	private Sensors sensor; 
	private RoomStatus room; 
	private static SerialCommChannel channel;
	private ArduinoStatus arduinoStatus; 
	
	public ArduinoCommunication(Sensors sensor, RoomStatus room, WebsocketServer websocket) { 
		this.sensor = sensor;
		this.room = room; 
		this.arduinoStatus = new ArduinoStatus(this.room, websocket); 
	}
	
	public void run(){
		try {
			channel = new SerialCommChannel("COM3",9600);
			/* attesa necessaria per fare in modo che Arduino completi il reboot */
			System.out.println("Waiting Arduino for rebooting...");		
			Thread.sleep(1000);
			
			//wait for the arduino connection
			connectToArduino(); 
			System.out.println("Ready");
			//when the connection is established the program start exchange message with arduino
			comunicateWithArduino(); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/*
	 * This method start sending connection message to the arduino in order to establish a connection. 
	 * When the arduino respond to the connection message the function ends. 
	 */
	private static void connectToArduino() throws InterruptedException {
		while(!channel.isMsgAvailable()) {
			channel.sendMsg("connect");
			Thread.sleep(1000);
			System.out.println("connecting...");
		}
	}
	
	/*
	 * This method continue listening to new incoming ardiono messages. 
	 * When a new message is available the gui data are updated and a "received" message is send. 
	 */
	private void comunicateWithArduino() throws InterruptedException {
		while (true){
			if (channel.isMsgAvailable()) {
				var mg = channel.receiveMsg();
				
				this.arduinoStatus.updateRoomStatus(mg);
				
				try {
					//recived message: 
					// ROOM STATUS | APP STATUS
			        // BOOL : mod | BOOL : lightOn | INT :  degree  || BOOL : mod | BOOL : lightOn | int : degree 
					
					System.out.println("ROOMmod | lightOn | degree  || APPmod | APPlightOn | APPdegree ");
					System.out.println("recived: " + mg);
					
					channel.sendMsg(generateaArduinoMessage());
					//ismanual | islightOn | rollerblind | pir | photoresistor
					System.out.println("ismanual | islightOn | rollerblind | pir | photoresistor | currentHour");
					System.out.println("sended: " + generateaArduinoMessage());
					//System.out.println("arduinoCom: " + this.room.toString());
					System.out.println(this.room.toString());
					System.out.println("-----------------------");
					
					}
					catch(Exception e) {
						System.out.println("error whith incoming message: " + mg );
						System.out.println(e.getLocalizedMessage());
						System.out.println("try to reconnect...");
						connectToArduino(); 
					}
			}				
		}		
	}
	
	private String generateaArduinoMessage() {
		String roomMessageString = room.isManualStatus() + " " + room.isLightOn() + " " + room.getRollerBlinder(); 
		String sensorMessageString = sensor.isPirOn() + " " + sensor.getPhotoresistorValue(); 
		int hour = LocalDateTime.now().getHour();
		return roomMessageString + " " + sensorMessageString + " " + hour; 
	}
	
	

}
