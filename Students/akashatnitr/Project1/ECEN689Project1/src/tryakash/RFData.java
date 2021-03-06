package tryakash;

//---------------------------------------------------------------------------------------------------------------------
//ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
//File Name: 		RFData.java
//Version:			1.0.0
//Date:			January 31, 2016
//Description:	    RFData Member
//
//Author:          John Lusher II
//---------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------
//Revision(s):     Date:                       Description:
//v1.0.0           January 31, 2016  	        Initial Release
//--------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------
//Imports
//--------------------------------------------------------------------------------------------------------------------
import java.util.Date;

//--------------------------------------------------------------------------------------------------------------------
//     Class:    RFData
//Description:	RFData class - RF Field Data w/ JSON support
//--------------------------------------------------------------------------------------------------------------------

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
     RSSI = 0.0f;
     Latitude = 0.0f;
     Longitude = 0.0f;
     Yaw = 0.0f;
     Pitch = 0.0f;
     Roll = 0.0f;
     SampleDate = new Date();
 }
}
