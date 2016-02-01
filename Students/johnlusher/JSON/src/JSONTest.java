// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		SimpleServer.java
// Version:			1.0.0
// Date:			January 31, 2016
// Description:	    Assignment #2 - Simple Server
//                  Class to handle server communication - creates socket and communicate to client
//                  Note: Code based off of code in How to Program Java - Paul Deitel
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
import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    JSONTest
//  Description:	JSONTest class - Extends JFRAME
//  --------------------------------------------------------------------------------------------------------------------
public class JSONTest extends JFrame
{
                                                                        // ---------------------------------------------
    private JTextArea OutputText;                                       // Output data to user
    private ServerSocket server;                                        // Server Socket
    private Socket connection;                                          // Connection to client
    private ObjectInputStream inputstream;                              // Input stream from client
    private ObjectOutputStream outputstream;                            // Output stream to client
                                                                        // ---------------------------------------------

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     JSONTest - Constructor
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Establish simple shell and test interaction with JSON
    //	----------------------------------------------------------------------------------------------------------------
    public JSONTest()
    {
                                                                        // ---------------------------------------------
        super("JSONTest");                                              // Super Class
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
        OutputText = new JTextArea();                                   // Output Text Area
        OutputText.setFont(new Font("Courier New", Font.BOLD, 12));     // Set font size
        add(new JScrollPane(OutputText), BorderLayout.CENTER);          // Set Scroll Plane
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
        setSize(640, 480);                                              // Set size of window (VGA size)
        setVisible(true);                                               // Show window
                                                                        // ---------------------------------------------
    }
}
