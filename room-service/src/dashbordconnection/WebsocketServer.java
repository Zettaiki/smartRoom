package dashbordconnection;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import room.RoomStatus;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

//source https://stackoverflow.com/questions/41470482/java-server-javascript-client-websockets

public class WebsocketServer extends WebSocketServer {

    private static int TCP_PORT = 4444;

    private Set<WebSocket> conns;
    private RoomStatus room; 

    public WebsocketServer(RoomStatus room) {
        super(new InetSocketAddress(TCP_PORT));
        this.conns = new HashSet<>();
        this.room = room; 
        
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        this.conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        sendCurrentStatus(); 
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

//    @Override
//    public void onMessage(WebSocket conn, String message) {
//        System.out.println("Message from client: " + message);
//        for (WebSocket sock : conns) {
//            sock.send(message);
//        }
//    }
//    
    
    @Override
    public void onMessage(WebSocket conn, String message) {
    	
    	JSONObject json;
    	
    	try {
			json = new JSONObject((String) message);
			
			if(json.getString("requestType").equals("history")) {
				System.out.println("history");
				this.sendRoomHistory();
			}else { 
	        	this.room.setDashboardControllStatus(json.getBoolean("manualControl"));
	        	this.room.setLight(json.getBoolean("light"));
	        	this.room.setRollerBlinder(json.getInt("rollerBlind"));
	        	this.sendCurrentStatus(); 
	        	//System.out.println("from websocket: " +  this.room.toString());
			}
	        
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
    	
        //System.out.println("Message from dashboard: " + message);  

        //System.out.println("Room History: \n" + this.room.getRoomHistory().toString());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
    	try {
    		ex.printStackTrace();
    		if (conn != null) {
    			conns.remove(conn);
    			// do some thing if required
    		}
    		System.out.println(ex);
    		System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    	} catch (Exception e) {
			System.out.println("eccezione");
		}
    }

    
    public void sendCurrentStatus() { 
    	for (WebSocket sock : conns) {
            try {
            	JSONObject response = this.room.generateRoomStatusJSON(); 
            	response.append("responseType", "currentStatus"); 
				sock.send(response.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
    
    public void sendRoomHistory() { 
    	for (WebSocket sock : conns) {
            try {
            	JSONObject response = this.room.getRoomHistory(); 
            	if(!response.has("responseType")) {
            		response.append("responseType", "roomHistory");             		
            	}
            	System.out.println(response.toString());
				sock.send(response.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
}


