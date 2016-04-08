// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		RFData.java
// Version:			1.0.0
// Date:			February 20, 2016
// Description:	    RFData Member
//
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           February 20, 2016  	     Initial Release
//  --------------------------------------------------------------------------------------------------------------------
package edu.tamu.testoauthlogin;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import java.util.Date;

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    RFData
//  Description:	RFData class - RF Field Data w/ JSON support
//  --------------------------------------------------------------------------------------------------------------------
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

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     RFData - Constructor
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    RFData, no initialization given, just set defaults
    //	----------------------------------------------------------------------------------------------------------------
    public RFData()
    {
        SampleNumber = -1;                                              // Default is -1 (undefined)
        XbeeID = -1;                                                    // Default is -1 (undefined)
        DeviceID = -1;                                                  // Default is -1 (undefined)
    }
}

