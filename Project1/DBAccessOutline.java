public class DBAccess extends SQLiteOpenHelper{

	/** CONSTRUCTOR **/

	DBAccess(Context context){

		// provide the context when instantiating (just DBAccess(this) should be fine)

	}
	
	// The class will use an onCreate method to make a database and table when the 
	// class is instantiated


	/** METHODS **/

	public boolean addData(String data){

		// the data parameter should be a json string with the following format:
		//   "Transmit ID":"Device ID",
  		//	 "RSSI":"Decibels",
  		//	 "Receive ID":"Device ID"
  		//	 "GPS":"Location",
  		//	 "IMU":"Orientation",
  		//	 "Timestamp":"<UNIX time>"
  		//	this string will be parsed and its contents will be saved in the database 
  		

	}

	public List<String> getUnsentData(){

		// returns a list of strings containing all of the json objects that have not 
		// yet been sent to the server

	}

	public List<String> getAllData(){

		// returns a list of strings containing all of the data collected, stored as 
		// json objects

		// (is this needed?)
	}

	public void clearData(){

		// clears the contents of the database

	}

}