/**
*	@file DataCollector.java
*
*	@brief Contains DataCollector class for background data collection and storage of settings
*			
**/
/**
*	@class DataCollector
*
*	@brief Data collection and settings storage class
*
*	DataCollector can be run as a thread to start the data collection process in the background.<br>
*	DataCollector's fields store collected data as well as settings that are defined in the Settings Activity
**/
public class DataCollector implements Runnable
{
	//settings fields
	public static int refresh_ms; /*!< Data refresh rate in ms */
	public static String serverURL; /*!< URL of Tomcat server */
	public static int portNum; /*!< Tomcat server port number */
	public static int s1_ID; /*!< ID of Source 1 */ 
	public static int s2_ID; /*!< ID of Source 2 */
	public static String fusionTableName; /*!< Text name of Fusion Table */
	public static boolean displayVars[]; /*!< Indicates which data should be recorded */
	public static float angleTolerance[]; /*!< Maximum deviation in degrees for orientation in (phi,theta) */
	public static boolean collectEnable; /*!< Flag to enable/disable data collection */
	public static boolean settingsChangedFlag; /*!< Indicates that the user has changed a setting in the Settings Activity */
	
	//data fields
	//currently copied from Project 1, may need review
	public static String transmitID = "5";
    public static double RSSI = 0;
    public static String receiveID = "6";
    public static double latitude = 0;
    public static double longitude = 0;
    public static DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String rssist = "";
    public static String gpsst = "";
    public static String imust = "";
    public static String timestampst = "";
    public static float yaw = 0;
    public static float pitch = 0;
    public static float roll = 0;
	
	public DataCollector()
	{
		displayVars = new boolean[8]; //@TODO check actual number of vars
		angleTolerance = new float[2];
		//@TODO add any needed instantiations of class objects for sensor reads
	}

	/**
	*	@fn run
	*	@brief Data collection loop, run as thread from data activity
	*	This function continously runs, collecting data when the user indicates to, calling the necessary classes and functions to read sensor data
	**/
	public void run()
	{
		while(true)
		{
			try{
				Thread.sleep(refresh_ms*1000); //delay based on user-defined refresh rate
			}catch(InterruptedException e){System.err.println(e);}; //print any errors to system log

			//@TODO implement data collection loop			
		}
	}
}
