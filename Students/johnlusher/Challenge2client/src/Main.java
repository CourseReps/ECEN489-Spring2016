// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		Main.java
// Version:			1.0.0
// Date:			January 31, 2016
// Description:	    Assignment #2 - Main Class (Simple Client)
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           January 31, 2016  	        Initial Release
//  --------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import javax.swing.JFrame;

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
                                                                        // ---------------------------------------------
        SimpleClient simpleclient;                                      // Create Simple Client Class
                                                                        // IF there is no arguments, then user default
                                                                        // connect to localhost
        if (args.length == 0) simpleclient = new SimpleClient("10.202.99.216");
        else simpleclient = new SimpleClient(args[0]);                  // else, use given address
        simpleclient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Default Close Operation
        simpleclient.RunSimpleClient();                                 // Run the client application
                                                                        // ---------------------------------------------
        
    }
}


