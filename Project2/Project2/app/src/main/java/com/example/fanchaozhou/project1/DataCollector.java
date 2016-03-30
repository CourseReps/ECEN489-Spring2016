/**
*	@file DataCollector.java
*
*	@brief Contains DataCollector class for background data collection and storage of settings
*			
**/

package com.example.fanchaozhou.project1;

import java.util.Date;

/**
*	@class DataCollector
*
*	@brief Data collection class
*
*	This class stores data variables in a manner that makes them simultaneously accessible to all fragments
**/
public class DataCollector
{
	//data fields
	public static String transmitID = "5"; /*!< Antenna ID */
    public static double RSSI = 0; /*!< Received Signal Strength in hex */
	public static int wifiRSSI = 0;
    public static String receiveID = "6"; /*!< XBee ID */
    public static double latitude = 0; /*!< GPS latitude */
    public static double longitude = 0; /*!< GPS longitude */
    public static Date timestamp = new Date(); /*!< Timestamp of current data read */
    public static float yaw = 0; /*!< X-axis orientation */
    public static float pitch = 0; /*!< Y-axis orientation */
    public static float roll = 0; /*!< Z-axis orientation */
	public static float magField[] = new float[3]; /*!< Magnetic field strength matrix */
	public static Boolean aligned = false;
	public static Boolean contCollection = false;

	public DataCollector()
	{
	}
}
