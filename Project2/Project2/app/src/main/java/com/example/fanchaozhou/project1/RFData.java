// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         RFData.java
 * @brief        Project #2 - RF Data
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------
package com.example.fanchaozhou.project1;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//----------------------------------------------------------------------------------------------------------------------

/** @class      RFData
 *  @brief      RFData class - RF Field Data w/ JSON support
 */
public class RFData
{
    public int SampleNumber;
    public int XbeeID;
    public int DeviceID;
    public float RSSI;
    public float Latitude;
    public float Longitude;
    public float Yaw;
    public float Pitch;
    public float Roll;
    public Date SampleDate;

//    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// JDL: updated format to match what is really in JSON object
    private DateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");



    //@TODO risky. These are copied from DBAccess.java. Changes there will not be reflected here
    private final String COLUMN_XBEE_ID     = "XbeeID";
    private final String COLUMN_RSSI		  = "RSSI";
    private final String COLUMN_DEVICE_ID   = "DeviceID";
    private final String COLUMN_LAT 		  = "Latitude";
    private final String COLUMN_LONG        = "Longitude";
    private final String COLUMN_YAW 		  = "Yaw";
    private final String COLUMN_PITCH 	  = "Pitch";
    private final String COLUMN_ROLL 		  = "Roll";
    private final String COLUMN_TIMESTAMP	  = "SampleDate";

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    RFData - Constructor
     *
     *           RFData, no initialization given, just set defaults
     */
    public RFData()
    {
        SampleNumber = -1;                                              /// Default is -1 (undefined)
        XbeeID = -1;                                                    /// Default is -1 (undefined)
        DeviceID = -1;                                                  /// Default is -1 (undefined)
    }

    public void assignValuesFromJSON(JSONObject json)
    {
        try {
            XbeeID = json.getInt(COLUMN_XBEE_ID);
            DeviceID = json.getInt(COLUMN_DEVICE_ID);
            RSSI = (float)json.getDouble(COLUMN_RSSI); //possible issue here
            Latitude = (float)json.getDouble(COLUMN_LAT);
            Longitude = (float)json.getDouble(COLUMN_LONG);
            Yaw = (float)json.getDouble(COLUMN_YAW);
            Pitch = (float)json.getDouble(COLUMN_PITCH);
            Roll = (float)json.getDouble(COLUMN_ROLL);
            SampleDate = (Date)format.parse(json.getString(COLUMN_TIMESTAMP));
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }
}

