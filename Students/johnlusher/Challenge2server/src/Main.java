// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		Main.java
// Version:			1.0.0
// Date:			January 31, 2016
// Description:	    Assignment #2 - Main Class (Simple Server)
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           January 31, 2016  	        Initial Release
//  --------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import java.text.*;

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    Main
//  Description:	Main class for project
//  --------------------------------------------------------------------------------------------------------------------
public class Main
{
    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     Class Construction
    //      Inputs:	    arguments
    //     Outputs:	    none
    //  Description:    Defines and initializes the class: main
    //	----------------------------------------------------------------------------------------------------------------
    public static void main(String[] args)
    {
        // Create instance of ComputerData class (from Assignment #1)
        ComputerData cdata = new ComputerData();

        // Date/Time Format
        SimpleDateFormat date_ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss");


        // Get the updated information
        cdata.GetUpdatedData();

        // Print data out the console
        System.out.println("   Date/Time:  " + date_ft.format(cdata.time));
        System.out.println("Java Version:  " + cdata.JavaVersion);
        System.out.println("          OS:  " + cdata.OS);
    }
}


