// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         RFData.java
 * @brief        Project #3 - RF Data
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------
package edu.tamu.rfsignalmap;

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
    public double RSSI;
    public int XbeeID;
    public int DeviceID;
    public double RSSI2;
    public int XbeeID2;
    public int DeviceID2;
    public double RSSI3;
    public int XbeeID3;
    public int DeviceID3;
    public String CellSignalStrength;
    public double Latitude;
    public double Longitude;
    public double Yaw;
    public double Pitch;
    public double Roll;
    public Date SampleDate;

    public int RFAntennaState;

    public double VecNav_Latitude;
    public double VecNav_Longitude;
    public double VecNav_Yaw;
    public double VecNav_Pitch;
    public double VecNav_Roll;

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      RFData
     * @brief   RFData - Constructor
     *
     *          RFData, no initialization given, just set defaults
     */
    public RFData()
    {
        SampleNumber = -1;                                              /// Default is -1 (undefined)
        XbeeID = -1;                                                    /// Default is -1 (undefined)
        DeviceID = -1;                                                  /// Default is -1 (undefined)
        RSSI = -9999.99;                                                /// Default is -9999.99 (not valid)
        RSSI2 = -9999.99;                                               /// Default is -9999.99 (not valid)
        RSSI3 = -9999.99;                                               /// Default is -9999.99 (not valid)
        RFAntennaState = 0;                                             /// Default is zero
    }
}

