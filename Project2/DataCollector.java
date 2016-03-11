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
	private static int refresh_ms; /*!< Data refresh rate in ms */
	private static String serverURL; /*!< URL of Tomcat server */
	private static int portNum; /*!< Tomcat server port number */
	private static int s1_ID; /*!< ID of Source 1 */ 
	private static int s2_ID; /*!< ID of Source 2 */
	private static String fusionTableName; /*!< Text name of Fusion Table */
	private static boolean displayVars[8]; /*!< Indicates which data should be recorded */
	private static float angleTolerance[2]; /*!< Maximum deviation in degrees for orientation in (phi,theta) */
	private static boolean collectEnable; /*!< Flag to enable/disable data collection */
	private static boolean settingsChangedFlag; /*!< Indicates that the user has changed a setting in the Settings Activity */
	
	//data fields
	//@TODO
	
	public DataCollector()
	{
		//@TODO
	}
	
	/**
	*	@fn getRate
	*	@brief Returns refresh rate in milliseconds
	**/
	public int getRate(){return refresh_ms;}
	
	/**
	*	@fn getURL
	*	@brief Returns currently specified Tomcat server address
	**/
	public String getURL(){return serverURL;}
	
	/**
	*	@fn getPort
	*	@brief Returns currently specified port number
	**/
	public int getPort(){return portNum;}
	
	/**
	*	@fn getID1
	*	@brief Returns currently specified source 1 ID
	**/
	public int getID1(){return s1_ID;}
	
	/**
	*	@fn getID2
	*	@brief Returns currently specified source 2 ID
	**/
	public int getID2(){return s2_ID;}
	
	public String getTblName(){return fusionTableName;}
	
	public boolean[] getActiveVars(){return displayVars;}
	
	public float[] getTolerance(){return angleTolerance;}
	
	public boolean getCollectEnableStatus(){return collectEnable;}
	
	//@TODO create set methods
	
	/**
	*	@fn run
	*	@brief Data collection loop, run as thread from data activity
	*	This function continously runs, collecting data when the user indicates to, calling the necessary classes and functions to read sensor data
	**/
	public void run()
	{
		while(1)
		{
			sleep(refresh_ms*1000);
			//@TODO implement data collection loop			
		}
	}
}
