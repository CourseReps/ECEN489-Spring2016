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
	private static boolean displayVars[]; /*!< Indicates which data should be recorded */
	private static float angleTolerance[]; /*!< Maximum deviation in degrees for orientation in (phi,theta) */
	private static boolean collectEnable; /*!< Flag to enable/disable data collection */
	private static boolean settingsChangedFlag; /*!< Indicates that the user has changed a setting in the Settings Activity */
	
	//data fields
	//@TODO
	
	public DataCollector()
	{
		displayVars = new boolean[8]; //@TODO check actual number of vars
		angleTolerance = new float[2];
		//@TODO add any needed instantiations of class objects for sensor reads
	}
	
	/*****************
	* Accessor Methods
	*****************/

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
	
	/**
	*	@fn getTblName
	*	@brief Returns name of specified Fusion Table
	**/
	public String getTblName(){return fusionTableName;}
	
	/**
	*	@fn getActiveVars
	*	@brief Returns bool array of variables currently marked for display
	**/
	public boolean[] getActiveVars(){return displayVars;}
	
	/**
	*	@fn getTolerance
	*	@brief Returns 2-entry float array of angle tolerances for orientation
	**/
	public float[] getTolerance(){return angleTolerance;}
	
	/**
	*	@fn getCollectEnableStatus
	*	@brief Returns true if data collection is enabled currently
	**/
	public boolean getCollectEnableStatus(){return collectEnable;}
	
	/*****************
	* Modifier Methods
	*****************/

	/**
	*	@fn setRate
	*	@brief Sets the refresh rate to [rate_ms] milliseconds
	**/
	public void setRate(int rate_ms){refresh_ms = rate_ms;}

	/**
	*	@fn setURL
	*	@brief Sets server address to [url]
	**/
	public void setURL(String url){serverURL = url;}

	/**
	*	@fn setPort
	*	@brief Sets remote server port to [port]
	**/
	public void setPort(int port){portNum = port;}
	
	/**
	*	@fn setSource1ID
	*	@brief Sets Source 1 ID to [S1ID]
	**/
	public void setSource1ID(int S1ID){s1_ID = S1ID;}

	/**
	*	@fn setSource1ID
	*	@brief Sets Source 2 ID to [S2ID]
	**/
	public void setSource2ID(int S2ID){s2_ID = S2ID;}

	//@TODO finish set methods
	

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
