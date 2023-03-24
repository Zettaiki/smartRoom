package room;

import java.util.Date;  
import java.text.SimpleDateFormat;
import java.time.Instant;

import org.json.JSONException;
import org.json.JSONObject;

public class RoomStatus {

	private boolean dashboardControllStatus; 
	private boolean appControllStatus; 
	private boolean light; 
	private int rollerBlind; 
	
	private JSONObject roomHistory = new JSONObject(); 
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	private Date date;  
	
	public RoomStatus() {
		this.dashboardControllStatus = false; 
		this.appControllStatus = false; 
		this.light = false; 
		this.rollerBlind = 0; 
	}

	/**
	 * 
	 * @return true is the room status is manual, false if automatic
	 */
	public boolean isManualStatus() {
		return this.dashboardControllStatus || this.appControllStatus;
	}
	
	public boolean getAppControllState() {
		return this.appControllStatus; 
	}

	public boolean getDashboardControllState() { 
		return this.dashboardControllStatus; 
	}
	
	/**
	 * This method change the status of the room. 
	 * @param manualStatus false = Automatic, true = Manual
	 */
	public void setDashboardControllStatus(boolean manualStatus) {
		if(this.dashboardControllStatus != manualStatus) { 
			this.dashboardControllStatus = manualStatus;
			updateRoomHistory(); 			
		}
		toString();
	}

	public void setAppControllStatus(boolean manualStatus) {
		if(this.appControllStatus != manualStatus) { 
			this.appControllStatus = manualStatus;
			updateRoomHistory(); 			
		}
		toString();
	}
	
	
	/**
	 * 
	 * @return true if the light is on, false otherwise
	 */
	public boolean isLightOn() {
		return light;
	}

	/**
	 * 
	 * @param light true: light on, false: light off
	 */
	public void setLight(boolean light) {
		if(this.light != light) { 
			this.light = light;
			updateRoomHistory(); 			
		}
		toString(); 
	}

	/**
	 * 
	 * @return the current position of the roller blind (from 0 to 180)
	 */
	public int getRollerBlinder() {
		return rollerBlind;
	}

	/**
	 * 
	 * @param rollerBlind the position of the roller blind
	 */
	public void setRollerBlinder(int rollerBlind) {
		if(this.rollerBlind != rollerBlind) { 
			this.rollerBlind = rollerBlind;
			updateRoomHistory(); 			
		}
		toString(); 
	}
	
	/**
	 * 
	 * @return a JSONObject that represent the status of the room
	 * @throws JSONException
	 */
	public JSONObject generateRoomStatusJSON() throws JSONException {
		
		JSONObject json = new JSONObject(); 
		json.append("manualControl", this.isManualStatus()); 
		json.append("light", this.light); 
		json.append("rollerBlind", this.rollerBlind); 
		
		return json;  
	}
	
	
	
	private void updateRoomHistory() { 
		long unixTime = Instant.now().getEpochSecond();
		try {
			this.roomHistory.append(Long.toString(unixTime), new JSONObject().append("manualControl", this.dashboardControllStatus)
																		.append("light", this.light)
																		.append("rollerBlind", this.rollerBlind));
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	 * @ return a json that represent the old status of the room
	 */
	public JSONObject getRoomHistory() {
		return this.roomHistory; 
	}

	@Override
	public String toString() {
		return "RoomStatus [dashboardControllStatus=" + dashboardControllStatus + ", appControllStatus="
				+ appControllStatus + ", light=" + light + ", rollerBlind=" + rollerBlind + "]";
	}

}
