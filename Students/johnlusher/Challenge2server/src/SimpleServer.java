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
//        Class:    SimpleServer
//  Description:	SimpleServer class - Extends JFRAME
//  --------------------------------------------------------------------------------------------------------------------
public class SimpleServer extends JFrame
{
                                                                        // ---------------------------------------------
    private JTextArea OutputText;                                       // Output data to user
    private ServerSocket server;                                        // Server Socket
    private Socket connection;                                          // Connection to client
    private ObjectInputStream inputstream;                              // Input stream from client
    private ObjectOutputStream outputstream;                            // Output stream to client
    private ComputerData computerdata;                                  // Computer Data Class
                                                                        // ---------------------------------------------

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     SimpleServer - Constructor
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Establish simple shell and setup the server
    //	----------------------------------------------------------------------------------------------------------------
    public SimpleServer()
    {
                                                                        // ---------------------------------------------
        super("SimpleServer");                                          // Super Class
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

                                                                        // ---------------------------------------------
        computerdata = new ComputerData();                              // ComputerData class
                                                                        // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     RunSimpleServer
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Run the Simple Server - Listen for connections
    //	----------------------------------------------------------------------------------------------------------------
    public void RunSimpleServer()
    {
                                                                        // ---------------------------------------------
        try                                                             //
        {                                                               // Create ServerSocket - Port 200, 100 max
            server = new ServerSocket(2000, 100);                       // clients that can wait for server
                                                                        //
            while (true)                                                // Loop until exit
            {                                                           //
                try                                                     //
                {                                                       //
                    WaitForConnection();                                // Wait for a socket connection
                                                                        // set up output stream for objects
                    outputstream = new ObjectOutputStream(connection.getOutputStream());
                    outputstream.flush();                               // Flush output buffer
                                                                        // Set up input stream for objects
                    inputstream = new ObjectInputStream(connection.getInputStream());
                    ProcessStream();                                    // Process Stream
                }                                                       //
                catch (EOFException eofException)                       //
                {                                                       //
                    DisplayMessage("\nConnection Terminated!\n\n");     // Display Status
                }                                                       //
                finally                                                 //
                {                                                       //
                    CloseConnection();                                  //  Close connection
                }                                                       //
            }                                                           //
        }                                                               //
        catch (IOException ioException)                                 // Exception Processing:
        {                                                               // Just print out exception trace
            ioException.printStackTrace();                              //
        }                                                               // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     RunSimpleServer
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Wait for connection then accepts connection
    //	----------------------------------------------------------------------------------------------------------------
    private void WaitForConnection() throws IOException
    {
                                                                        // ---------------------------------------------
        DisplayMessage("Waiting for connection from client\n");         // Display message
        connection = server.accept();                                   // Allow server to accept connection
        DisplayMessage("Client Connection received from: " + connection.getInetAddress().getHostName() + "\n");
                                                                        // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     CloseConnection
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Close Connection
    //	----------------------------------------------------------------------------------------------------------------
    private void CloseConnection()
    {
                                                                        // ---------------------------------------------
        try                                                             // Close the streams and connection
        {                                                               //
            inputstream.close();                                        // Close input stream
            outputstream.close();                                       // Close output stream
            connection.close();                                         // Close socket
        }                                                               //
        catch (IOException ioException)                                 // Exception Processing:
        {                                                               // Just print out exception trace
            ioException.printStackTrace();                              //
        }                                                               //
                                                                        // ---------------------------------------------
        DisplayMessage("\nTerminating connection\n");                   // Terminate Connection - Inform User
                                                                        // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     ProcessStream
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Process Input Stream
    //	----------------------------------------------------------------------------------------------------------------
    private void ProcessStream() throws IOException
    {
                                                                        // ---------------------------------------------
        SendData("SERVER> Connected to John's SimpleServer");           // Send connection successful
        outputstream.writeObject(computerdata);                         // Send computer data object
        SendData("SERVER> TERMINATE");                                  // Send connection termination
                                                                        // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     SendData
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Send Data to Client
    //	----------------------------------------------------------------------------------------------------------------
    private void SendData(String message)
    {
                                                                        // ---------------------------------------------
        try                                                             //
        {                                                               //
            outputstream.writeObject(message);                          // Send Message
            outputstream.flush();                                       // Flush output to client
            DisplayMessage(message + "\n");                             // Display message send to user on server side
        }                                                               //
        catch (IOException ioException)                                 // Exception Processing:
        {                                                               // Display Error
            OutputText.append("\nError writing object\n");              //
        }                                                               //
                                                                        // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     DisplayMessage
    //      Inputs:	    messageToDisplay - Text Message to Append to Field
    //     Outputs:	    none
    //  Description:    Display Message in Output Field
    //	----------------------------------------------------------------------------------------------------------------
    private void DisplayMessage(final String messageToDisplay)
    {
                                                                        // ---------------------------------------------
        SwingUtilities.invokeLater                                      //
        (                                                               //
            new Runnable()                                              //
            {                                                           //
                public void run()                                       // Update the Output Text
                {                                                       //
                    OutputText.append(messageToDisplay);                // Append message to text field
                }                                                       //
            }                                                           //
        );                                                              //
                                                                        // ---------------------------------------------
    }
}
