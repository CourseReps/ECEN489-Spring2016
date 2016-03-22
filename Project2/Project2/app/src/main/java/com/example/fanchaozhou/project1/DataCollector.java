/**
*	@file DataCollector.java
*
*	@brief Contains DataCollector class for background data collection and storage of settings
*			
**/

package com.example.fanchaozhou.project1;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	//data fields
	//currently copied from Project 1, may need review
	public static String transmitID = "5"; /*!< Antenna ID */
    public static double RSSI = 0; /*!< Received Signal Strength in hex */
    public static String receiveID = "6"; /*!< XBee ID */
    public static double latitude = 0; /*!< GPS latitude */
    public static double longitude = 0; /*!< GPS longitude */
    public static Date timestamp = new Date(); /*!< Timestamp of current data read */
    public static float yaw = 0; /*!< X-axis orientation */
    public static float pitch = 0; /*!< Y-axis orientation */
    public static float roll = 0; /*!< Z-axis orientation */

	public DataCollector()
	{
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
				//@TODO pull user-defined refresh rate from XML
				Thread.sleep(500); //run at 2Hz
			}catch(InterruptedException e){System.err.println(e);}; //print any errors to system log

			//@TODO collect live data that isn't already collected in the MainFragment and send to server
			timestamp = Calendar.getInstance().getTime();
		}
	}
}
