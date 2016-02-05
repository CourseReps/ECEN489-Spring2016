// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		Main.java
// Version:			1.0.0
// Date:			January 31, 2016
// Description:	    Assignment #2 - Main Class (Simple Server)
//                  Create a simple shell and run the SimpleServer Class
//
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
        SimpleServer simpleserver = new SimpleServer();                 // Create Simple Server Class
        simpleserver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Default Close Operation
        simpleserver.RunSimpleServer();                                 // Run the server application
                                                                        // ---------------------------------------------
    }
}


