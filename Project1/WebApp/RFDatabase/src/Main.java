// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		Main.java
// Version:			1.0.0
// Date:			February 18, 2016
// Description:	    SQL Database Interface Class for RF Field Database - Example and Test Class
//
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           February 18, 2016  	     Initial Release
//  --------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.lang.Math;
import java.util.Iterator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


//  --------------------------------------------------------------------------------------------------------------------
//        Class:    Main
//  Description:	Main class for project
//  --------------------------------------------------------------------------------------------------------------------
public class Main
{
    private static RFFieldSQLDatabase RFFieldDatabase;                  // RF Field Database

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     Class Construction
    //      Inputs:	    arguments
    //     Outputs:	    none
    //  Description:    Defines and initializes the class: main
    //	----------------------------------------------------------------------------------------------------------------
    public static void main(String[] args)
    {
        RFFieldDatabase = new RFFieldSQLDatabase();                     // Create new RF Field Database
//        RFFieldDatabase.ConnectToDatabase("10.202.102.173");            // Connect to MySql database (local to EIC)

        // Lusher Test MySQL Server
        RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com");

        // Get a new Lat/Long randomly around the EIC area
        LatLong newpos = new LatLong(359.99 * Math.random(), 650.0 * Math.random(),  30.618651, -96.341498);


        RFData testmember = new RFData();                               // Create test RF data
        testmember.XbeeID = 456;                                        // Fill test with dummy data
        testmember.DeviceID = 1234;
        testmember.Latitude = (float)newpos.Latitude;
        testmember.Longitude = (float)newpos.Longitude;
        testmember.RSSI = (float)(100.0 * Math.random());
        testmember.Yaw = (float)(359.99 * Math.random());
        testmember.Pitch = (float)(359.99 * Math.random());
        testmember.Roll = (float)(359.99 * Math.random());
        testmember.SampleDate = new Date();

        String jsondata;                                                // Create JSON holding string
        Gson gson = new GsonBuilder().create();                         // Create Gson builder
        jsondata = gson.toJson(testmember);                             // Convert to JSON and store in string

        System.out.println("AddNewEntry Test:");                        // Perform test of AddNewEntry
        System.out.println("JSON Data: " + jsondata);                   //
        RFFieldDatabase.AddNewEntry(jsondata);                          // Insert data into database

        System.out.println("")                      ;                   //
        System.out.println("ListDataByEntryID Test:");                  // Perform test of ListDataByEntryID
        jsondata = RFFieldDatabase.ListDataByEntryID(1);                // Get first record
        System.out.println("JSON: " + jsondata);                        // Print result


        System.out.println("")                      ;                   //
        System.out.println("ListDataByDevice Test:");                   // Perform test of ListDataByDevice
        jsondata = RFFieldDatabase.ListDataByDevice(1234);              // Get list of records by device ID
        System.out.println("JSON: " + jsondata);                        // Print result

        System.out.println("")                      ;                   //
        System.out.println("ListDataByRSSI Test:");                     // Perform test of ListDataByRSSI - GTE
        jsondata = RFFieldDatabase.ListDataByRSSI(95.0F, true);         // Get list of records by RSSI
        System.out.println("JSON: " + jsondata);                        // Print result

        System.out.println("")                      ;                   //
        System.out.println("ListDataByRSSI Test:");                     // Perform test of ListDataByRSSI - LT
        jsondata = RFFieldDatabase.ListDataByRSSI(99.0F, false);        // Get list of records by RSSI
        System.out.println("JSON: " + jsondata);                        // Print result

        System.out.println("")                      ;                   //
        System.out.println("ListDataByGeoArea Test:");                  // Perform test of ListDataByGeoArea - LT
        jsondata = RFFieldDatabase.ListDataByGeoArea(30.0F, -97.0F, 31.0F, -96.0F);
        System.out.println("JSON: " + jsondata);                        // Print result

                                                                        // Pull apart JSON
        ArrayList records = gson.fromJson(jsondata, ArrayList.class);   // Convert from JSON to Array List
        Iterator itr = records.iterator();                              // Build an Iterator for the Array List
        while(itr.hasNext())                                            // Loop through the list
        {                                                               // Entries are JSON strings
            Object element = itr.next();                                // Get the next object
                                                                        // Convert from JSON to Array List
            RFData RFMember = gson.fromJson((String)element, RFData.class);
                                                                        // Print debug information to port
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            System.out.println("Got Record: # " + RFMember.SampleNumber + " - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI + ", Lat: " + RFMember.Latitude + ", Long: " + RFMember.Longitude + " Date/Time: " + ft.format(RFMember.SampleDate));
        }

        RFFieldDatabase.DisconnectDatabase();                           // Disconnect from MySql database
    }




}


