package testthread;

import java.nio.channels.NotYetConnectedException;

import org.json.JSONException;

import dashbordconnection.WebsocketServer;

public class testThread extends Thread {
	
	String stringTosend = "ping n. "; 
	int i = 0; 
	
	WebsocketServer websocket; 
	
	public testThread(WebsocketServer websocket) {	
		this.websocket = websocket;
	}
	
	public void run() {
		String string;  
		while(true) {
			//System.out.println("Sending data to client");
			i = i + 1; 
			string = stringTosend + " " + i; 
			
			try {
				//websocket.sendCurrentStatus();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

}
