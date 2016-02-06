// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		SimpleServer.java
// Version:			1.0.0
// Date:			January 31, 2016
// Description:	    Assignment #2 - Simple Client
//                  Class to handle client communication - creates socket and communicate to server
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
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    SimpleClient
//  Description:	SimpleClient class - Extends JFRAME
//  --------------------------------------------------------------------------------------------------------------------
public class SimpleClient extends JFrame
{
                                                                        // ---------------------------------------------
    private JTextArea OutputText;                                       // Output data to user
    private Socket client;                                              // Socket to communicate with server
    private String hostAddress;                                         // Host IP Address / Name
    private ObjectInputStream inputstream;                              // Input stream from client
    private ObjectOutputStream outputstream;                            // Output stream to client
    private String message = "";                                        // Message from server
    private ComputerData computerdata;                                  // Computer Data Class
    private int msgcount = 0;                                           // Message counter
                                                                        // ---------------------------------------------

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     SimpleClient - Constructor
    //      Inputs:	    Host Address
    //     Outputs:	    none
    //  Description:    Establish simple shell and setup the client
    //	----------------------------------------------------------------------------------------------------------------
    public SimpleClient(String host)
    {
        super("SimpleClient");
                                                                        // ---------------------------------------------
        hostAddress = host;                                             // Copy over Host Address
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
        OutputText = new JTextArea();                                   // Output Text Area
        OutputText.setFont(new Font("Courier New", Font.BOLD,12));      // Set font size
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
    //    Function:     RunSimpleClient
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Run the Simple Client - Go to sever, establish connection, get data
    //	----------------------------------------------------------------------------------------------------------------
    public void RunSimpleClient()
    {
                                                                        // ---------------------------------------------
        try                                                             //
        {                                                               //
            msgcount = 0;                                               // Reset count
            ConnectToServer();                                          // Create a Socket to make connection
                                                                        // Set up the output stream for objects
            outputstream = new ObjectOutputStream(client.getOutputStream());
            outputstream.flush();                                       // Flush output buffer
                                                                        // Set up input stream for objects
            inputstream = new ObjectInputStream(client.getInputStream());
            ProcessConnection();                                        // Process Connection
        }                                                               //
        catch (EOFException eofException)                               //
        {                                                               //
            DisplayMessage("\nConnection Terminated!\n\n");             // Display Status
        }                                                               //
        catch (IOException ioException)                                 // Exception Processing:
        {                                                               // Just print out exception trace
            ioException.printStackTrace();                              //
        }                                                               //
        finally                                                         //
        {                                                               //
                CloseConnection();                                      // Close the socket connection
        }                                                               // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     ConnectToServer
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Connect to the server
    //	----------------------------------------------------------------------------------------------------------------
    private void ConnectToServer() throws IOException
    {
                                                                        // ---------------------------------------------
        DisplayMessage("\nConnecting to server: " + hostAddress + "\n");// Display user prompt
        client = new Socket(InetAddress.getByName(hostAddress), 2000);  // Create Socket to make connection to server
        DisplayMessage("Connected to: " + client.getInetAddress().getHostName());
                                                                        // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     ProcessConnection
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Process data to/from server
    //	----------------------------------------------------------------------------------------------------------------
    private void ProcessConnection() throws IOException
    {

                                                                        // ---------------------------------------------
        do                                                              // Process messages sent from server
        {                                                               //
            try                                                         // Get message and parse
            {                                                           //
                Object msgobj = inputstream.readObject();               // Read new message
                switch (msgcount)                                       // Process based upoon message #
                {
                    case 0:                                             // Message #1: Connection Message
                        message = (String)msgobj;                       // Cast to string
                        DisplayMessage("\n" + message);                 // Display message
                        break;                                          //
                    case 1:                                             // Message #2: Computer Data Class
                        computerdata = (ComputerData)msgobj;            // Cast to computer data
                                                                        // Display message
                        DisplayMessage("\nSERVER> Got Server Computer Data\n");
                                                                        // Print out server computer data
                                                                        // Display data class
                        DisplayMessage("\n\n" + computerdata.toString() + "\n\n");
                        break;                                          //
                    default:                                            // Else, just print the data
                        message = (String)msgobj;                       // Cast to string
                        DisplayMessage("\n" + message);                 // Display message
                        break;                                          //
                }                                                       //
                                                                        //
                msgcount++;                                             // Increment the message count
            }                                                           //
            catch (ClassNotFoundException classNotFoundException)       // Class not supported
            {                                                           //
                DisplayMessage("\nUnknown object type received\n");     //
            }                                                           //
        } while (!message.equals("SERVER> TERMINATE"));                 //
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
            client.close();                                             // Close socket
        }                                                               //
        catch (IOException ioException)                                 // Exception Processing:
        {                                                               // Just print out exception trace
            ioException.printStackTrace();                              //
        }                                                               //
                                                                        // ---------------------------------------------
        DisplayMessage("\nCLIENT> Terminating connection\n");           // Terminate Connection - Inform User
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
            outputstream.writeObject("CLIENT> " + message);             // Send Message
            outputstream.flush();                                       // Flush output to client
            DisplayMessage("CLIENT> " + message + "\n");                // Display message send to user on server side
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
