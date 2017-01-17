/*
 * Java assignment 2 - JAC444
 * @author: Lawrence Reyes
 * @student number: 108307158 
 */
public class MobileDevice{
	public String deviceName; //the device name
	public int valueTag; //an integer between -100 and 100
	Lab lab; //the lab having this device in its inventory
	RentSettings rs; //rent settings
	
	/*
	 * @constructor
	 */
	MobileDevice(String deviceName, int valueTag){
		this.deviceName = deviceName;
		this.valueTag = valueTag;
	}
	
	public void setLab(Lab lab) {
		this.lab = lab;
    }
    
	public Lab getLab(){
		return lab;
	}
		
	public RentSettings getRs() {        
        return rs;
    }

    public void setRs(RentSettings rs) {
        this.rs = rs;
    }
    
    public String deviceName() {
        return "(" + deviceName + ", " + valueTag + ')';
    }
    
	/*
	 * Sets the information for the device that is being rented
	 * @param rentDate - date the device is wanted
	 * @param dueDate - date the device has to be returned
	 * @param lab
	 * @return boolean
	 */
	public boolean rentDevice(String rentDate, String dueDate, Lab lab){	
		boolean rentDevice = false;
		try{
			if(Helper.isValidDate(rentDate) && Helper.isValidDate(dueDate)){
				if(Helper.timeDifference(rentDate, dueDate) > 0){
					RentSettings rs = new RentSettings(rentDate, dueDate, lab);
					setLab(lab);
					setRs(rs);
					rentDevice = true;
				}else
					throw new RentPeriodException();
			}else
				throw new DateFormatException();
		}catch(DateFormatException date){
			System.err.println("The date entered is not valid. Correct format for date is (MM/DD/YYYY).");
		}catch(RentPeriodException rent){
			System.err.println("The rent period of the device is over.");
		}
		return rentDevice;
	}
    
	/*
	 * Checks if the device is overdue
	 * @return boolean
	 */
    public boolean isDeviceOverdue() {
        boolean overdue = false; 
        try {
			if(Helper.timeDifference(Helper.getCurrentDate(), rs.dueDate) < 0){
				overdue = true;
				System.out.println("Device is overdue.");
			}
			else
				System.out.println("Device is not overdue.");
		} catch (DateFormatException e) {
			e.printStackTrace();
		}
        return overdue;
    }
    
    /*
     * Check is a device is rented
     * @param lab
     * @return boolean
     */
    public boolean isRented(Lab l) {
        boolean rented = false;   
        for(MobileDevice md : l.devices){
        	if(l.isThereDevice(md) && rs.borrowed)
        		rented = true;
        } 
        return rented;
    }
    
    /*
     * Destroys the RentSettings object from lab
     * @param lab - lab object where the RentSettings are being destroyed
     */
    public void returnDevice(Lab lab) {
       if(this.lab.equals(lab)){
    	   setRs(null);
    	   setLab(null);
       }
    }
    
    /*
     * Prints the available date for the device in a lab
     * @param lab - lab object
     * @return available date
     */
    public String availableDate(Lab lab) {
    	return rs == null ? Helper.getCurrentDate() : rs.dueDate;
    }
    
    /*
  	 * @equals
  	 * @toString
  	 * @hashCode
  	 */
    @Override
    public boolean equals(Object o) {
    	if(this == o)
			return true;
		if(o == null)
			return false;
		if(!(o instanceof MobileDevice))
			return false;
		return ((MobileDevice)o).deviceName.equals(deviceName) && ((MobileDevice)o).valueTag == valueTag;
    }

    @Override
    public String toString() {
        String s = "";

		// check if device belongs to a lab
		if(lab == null)
			s += deviceName();
		else
			s += "(" + deviceName + ", " + valueTag + " => " + lab.labName + ")";

		// check if device is rented
		if(rs != null)
			s += " " + rs.toString();

		return s;
    }
    
    @Override
    public int hashCode() {
        int result = deviceName != null ? deviceName.hashCode() : 0;
        result = 31 * result + valueTag;
        return result;
    }
    
    private class RentSettings {
        private String rentDate;          // date when the item is requested
        private String dueDate;           // date when the item must be returned
        private boolean borrowed = false; // true if the item is rented

        //default ctr
        private RentSettings(){
           rentDate = null;
           dueDate = null;
           borrowed = false;
        }

        // private ctr must throw DateFormatException and RentPeriodException
        private RentSettings(String rentDate, String dueDate, Lab lab) throws DateFormatException, RentPeriodException {
            Helper.checkDate(rentDate);
            Helper.checkDate(dueDate);
            
            if(Helper.isValidDate(rentDate) && Helper.isValidDate(dueDate)){
	            if(Helper.timeDifference(rentDate, dueDate) < 0)
					throw new RentPeriodException();
	            this.rentDate = rentDate;
	            this.dueDate = dueDate;
	            borrowed = true;
	            
	            for(MobileDevice dev : lab.devices){
	            	dev.setRs(rs);
	            	dev.setLab(lab);
	            }
            }else
            	throw new DateFormatException();
        }

        @Override
        public String toString() {
            return "RentSettings{" +
                    "rentDate='" + rentDate + '\'' +
                    ", dueDate='" + dueDate + '\'' + MobileDevice.this.lab.labName +
                    ", borrowed=" + borrowed +
                    '}';
        }
    }
}
