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
package edu.tamu.project2googlemap;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
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
}

