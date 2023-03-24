package arduinoconnector;

import dashbordconnection.WebsocketServer;
import room.RoomStatus;

public class ArduinoStatus {

	private RoomStatus room;
	private WebsocketServer dashboard; 

	// private Boolean controlState;
	private Boolean lightState;
	private int rollerBlindValue;

	private Boolean appControlStatus;
	private Boolean appLightState;
	private int appRollerBlindValue;

	public ArduinoStatus(RoomStatus room, WebsocketServer dashboard) {
		this.room = room;
		this.dashboard = dashboard;
		
		// this.controlState = false;
		this.lightState = false;
		this.rollerBlindValue = 0;
		this.appControlStatus = false;
		this.appLightState = false;
		this.appRollerBlindValue = 0;

	}

	/**
	 * 
	 * @param mg represent the message arrived from arduino
	 */
	public void updateRoomStatus(String mg) {
		
		String[] parameters = mg.split(" ");
		if (parameters.length == 6) {
			// Arduino room info
			// Boolean controlState = Boolean.parseBoolean(parameters[0]);
			Boolean lightState = Boolean.parseBoolean(parameters[1]);
			int rollerBlindValue = Integer.parseInt(parameters[2]);

			// App status
			Boolean appControlState = Boolean.parseBoolean(parameters[3]);
			Boolean appLightState = Boolean.parseBoolean(parameters[4]);
			int appRollerBlindValue = Integer.parseInt(parameters[5]);

			
			System.out.println(lightState + " " + rollerBlindValue + " " +  appControlState + " " + appLightState + " " + rollerBlindValue);
			
			// check if the message contains different informations from the previous one
			if (this.lightState != lightState || this.rollerBlindValue != rollerBlindValue
					|| this.appControlStatus != appControlState || this.appLightState != appLightState
					|| this.appRollerBlindValue != appRollerBlindValue) {

				// if the appControllState is changed change the room controllState
				if (appControlState != this.appControlStatus) {
					this.room.setAppControllStatus(appControlState);
					if(appControlState) { 
						this.room.setLight(appLightState);
						this.room.setRollerBlinder(appRollerBlindValue);	
					}
				}
				
				// if the room is in manual control from the app and some values are changed update the room status
				if (this.room.getAppControllState()
						&& (this.appLightState != appLightState || this.appRollerBlindValue != appRollerBlindValue)) {
					this.room.setLight(appLightState);
					this.room.setRollerBlinder(appRollerBlindValue);
				}

				//if the room is in automatic control update the value of the room
				if (!this.room.isManualStatus()) {
					this.room.setLight(lightState);
					this.room.setRollerBlinder(rollerBlindValue);
				}

				// update class status with the new values
				this.lightState = lightState;
				this.rollerBlindValue = rollerBlindValue;
				this.appControlStatus = appControlState;
				this.appLightState = appLightState;
				this.appRollerBlindValue = appRollerBlindValue;
				
				this.dashboard.sendCurrentStatus();
			}

		}
	}

}
