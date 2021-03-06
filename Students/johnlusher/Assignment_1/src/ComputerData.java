// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		ComputerData.java
// Version:			1.0.0
// Date:			January 25, 2016
// Description:	    Assignment #1 - ComputerData Class
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           January 25, 2016  	        Initial Release
//  --------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import java.util.Date;

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    ComputerData
//  Description:	ComputerData class for project
//  --------------------------------------------------------------------------------------------------------------------
public class ComputerData
{
    // Public members
    public Date time = new Date();
    public String JavaVersion = new String();
    public String OS = new String();


    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     GetUpdatedData
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Get Updated Data
    //	----------------------------------------------------------------------------------------------------------------
    public void GetUpdatedData( )
    {
        time = new Date();                                          // Get current date and time
                                                                    // Get system properties
        JavaVersion = System.getProperty("java.version");           // Get the java version from the system
        OS = System.getProperty("os.name");                         // Get the OS name from the system
    }
}
