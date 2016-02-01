// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		SimpleServer.java
// Version:			1.0.0
// Date:			January 31, 2016
// Description:	    Tutorial - JSON/Gson - JavaScript Object Notation
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

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
        Members testperson = new Members();                             // Create example member
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
        testperson.lastName = "Lusher";                                 // Set some values
        testperson.firstName = "John";                                  //
        testperson.phoneNumber = "(979) 229-1950";                      //
        testperson.ID = 1;                                              //
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
        Gson gson = new GsonBuilder().create();                         // Create Gson builder
        gson.toJson(testperson, System.out);                            // Convert to JSON and send out the console
                                                                        //
        System.out.println();                                           // Print blank line x 2
        System.out.println();                                           //
                                                                        //
        try                                                             // Catch and file expections
        {                                                               //
            Writer writer = new FileWriter("Output.json");              // Create file writer - filename  Output.json
            Reader reader = new FileReader("Input.json");               // Create file reader - filename  Input.json
                                                                        //
            gson.toJson(testperson, writer);                            // now send out via the file writer
            Members newperson = gson.fromJson(reader, Members.class);   // now read via file reader, create new person
                                                                        //
            System.out.println("New Person via JSON file");             // Print out new person data
            System.out.println("        ID: " + newperson.ID);          //
            System.out.println("First Name: " + newperson.firstName);   //
            System.out.println(" Last Name: " + newperson.lastName);    //
            System.out.println("   Phone #: " + newperson.phoneNumber); //
                                                                        //
            writer.close();                                             // Clean up objects, close writer/reader
            reader.close();                                             //
        }                                                               //
        catch (IOException ioException)                                 // Process exception
        {                                                               //
            ioException.printStackTrace();                              // Just printout the exception trace
        }                                                               //
                                                                        // ---------------------------------------------
    }
}
