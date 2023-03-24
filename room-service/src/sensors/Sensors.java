package sensors;

public class Sensors {

	private boolean pir; 
	private int photoresistor; 

	
	public Sensors() {
		this.pir = false; 
		this.photoresistor = 0;  
	}

	/**
	 * 
	 * @return true if the pir is on, false otherwise
	 */
	public boolean isPirOn() {
		return pir; 
	}
	
	public int getPhotoresistorValue() {
		return this.photoresistor; 
	}
	

	/**
	 * 
	 * @param pirStatus the new status of the pir. True if someone i detected in the room,
	 *  false otherwise
	 */
	public void setPirStatus(boolean pirStatus) { 
		this.pir = pirStatus; 
	}
	
	/**
	 * 
	 * @param photoresistorValue the new value detected by the photoresistor
	 */
	public void setPhotoresistorValue(int photoresistorValue) {
		this.photoresistor = photoresistorValue; 
	}

	@Override
	public String toString() {
		return "Sensors [pir=" + pir + ", photoresistor=" + photoresistor + "]";
	}
		
}
