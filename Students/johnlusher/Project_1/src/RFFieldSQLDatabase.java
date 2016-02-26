// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		RFFieldSQLDatabase
// Version:			1.0.0
// Date:			February 18, 2016
// Description:	    SQL Database Interface Class for RF Field Database
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;
 */

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    RFFieldSQLDatabase
//  Description:	RFFieldSQLDatabase interfaces SQL Database, supplies methods to access
//                  data and passes JSON to/from calling classes.
//  --------------------------------------------------------------------------------------------------------------------
public class RFFieldSQLDatabase
{
    // Define class members
    private Connection conn = null;                                     // MySql Database connection

/*
    private static final String APPLICATION_NAME = "ECEN689Project1";   // Set the Application Name

    // Directory to store user credentials for this
    // application.
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.dir"),
                    ".store/ecen689project1");

    private static FileDataStoreFactory DATA_STORE_FACTORY;             // Global instance of the {@link FileDataStoreFactory}.
    private static HttpTransport HTTP_TRANSPORT;                        // Global instance of the HTTP transport
    // Global instance of the JSON factory.
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Fusiontables FusionTables;                           // Global instance of the Fusion Tables

    // -----------------------------------------------------------------------------------------------------------------
    // Create new instances of HTTP_TRANSPORT
    static                                                              //
    {                                                                   // and DATA_STORE_FACTORY
        try                                                             //
        {                                                               //
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        }                                                               //
        catch (Throwable t)                                             //
        {                                                               //
            t.printStackTrace();                                        //
            System.exit(1);                                             //
        }                                                               //
    }                                                                   //

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     authorize
    //      Inputs:	    none
    //     Outputs:	    Credentials
    // Description:     Creates an authorized credential object, will throw IO exception
    //	----------------------------------------------------------------------------------------------------------------
    private static Credential authorize() throws IOException
    {
        // Load client secrets (from JSON file)
        InputStream in = Main.class.getResourceAsStream("/client_secret_ecen689project1.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setDataStoreFactory(DATA_STORE_FACTORY).build();

        // Authorize and get the credential
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;                                              // Return the credential
    }
*/

    //	----------------------------------------------------------------------------------------------------------------
    //       Method:    getConnection
    //       Inputs:	database URL, username, password
    //      Outputs:	connection to DB
    //  Description:    Connects to MySql database
    //	----------------------------------------------------------------------------------------------------------------
    private static Connection getConnection(String dbURL, String user, String password) throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");                         // Setup for the MySql JDBC Driver
        Properties props = new Properties();                            // Build the properties
        props.put("user", user);
        props.put("password", password);
        props.put("autoReconnect", "true");                             // Enabled auto-reconnection
        return DriverManager.getConnection(dbURL, props);               // Return the connection to the database
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     Class Construction
    //      Inputs:	    none
    //     Outputs:	    none
    //  Description:    Class Construction
    // 	----------------------------------------------------------------------------------------------------------------
    public RFFieldSQLDatabase()
    {
    }

    //	----------------------------------------------------------------------------------------------------------------
    //       Method:    ConnectToDatabase
    //       Inputs:    none
    //      Outputs:    Success = TRUE / Failure = FALSE
    //  Description:    Establishes a connection to the database
    //	----------------------------------------------------------------------------------------------------------------
    public boolean ConnectToDatabase(String host_address)
    {
        boolean status;                                                 // Return status (success / failure)

/*        try
        {
                                                                        // Google Fusion Tables Authorization
            Credential credential = authorize();                        // Client authorization
                                                                        // Set up global FusionTables instance
            FusionTables = new Fusiontables.Builder(HTTP_TRANSPORT,
                    JSON_FACTORY,
                    credential).setApplicationName(APPLICATION_NAME).build();
        }                                                               //
        catch (IOException e)                                           // Print out exception messages
        {                                                               //
            System.err.println(e.getMessage());                         //
        }                                                               //
        catch (Throwable t)                                             //
        {                                                               //
            t.printStackTrace();                                        //
        }                                                               //
*/

        try                                                             // Try and connect to the MySQL Database
        {                                                               // Use ECEN_RF_Field Database
            System.out.println("Attempting Connection");                // Print out connection success
            //conn = getConnection("jdbc:mysql://69.5.199.164/ECEN_RF_Fields", "ecen689", "ecen689$2016");
           // conn = getConnection("jdbc:mysql://10.202.102.173/ECEN_RF_Fields", "ecen689", "ecen689$2016");
            conn = getConnection("jdbc:mysql://" + host_address + "/ECEN_RF_Fields", "ecen689", "ecen689$2016");
            System.out.println("Opened database successfully");         // Print out connection success
            status = true;                                              // Success
        }                                                               //
        catch  ( Exception e )                                          // Exception processing:
        {                                                               //
            System.err.println("ConnectToDatabase: " + e.getMessage() );// Print the exception data and exit
            status = false;                                             // Failure
        }                                                               //
        return status;                                                  // Return status
    }

    //	----------------------------------------------------------------------------------------------------------------
    //       Method:    DisconnectDatabase
    //       Inputs:    none
    //      Outputs:    Success = TRUE / Failure = FALSE
    //  Description:    Disconnects the connection to the database
    //	----------------------------------------------------------------------------------------------------------------
    public boolean DisconnectDatabase()
    {
        boolean status;                                                 // Return status (success / failure)

        try                                                             // Try and connect to the MySQL Database
        {                                                               //
            if (conn != null)  conn.close();                            // Close the connection if defined
            System.out.println("Database connection closed");           // Print out status
            status = true;                                              // Success
        }                                                               //
        catch  ( Exception e )                                          // Exception processing:
        {                                                               // Print the exception data and exit
            System.err.println("DisconnectDatabase: " + e.getMessage() );
            status = false;                                             // Failure
        }                                                               //
        return status;                                                  // Return status
    }
/*

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     GetTableId
    //      Inputs:	    none
    //     Outputs:	    none
    // Description:     Get Table ID from Table Nam
    //	----------------------------------------------------------------------------------------------------------------
    private static String GetTableId(String tableName) throws IOException
    {
        String tid = null;                                              // Set default return
        System.out.println("Get Table ID: " + tableName);               // Print header
        // Get the table list
        Fusiontables.Table.List listTables = FusionTables.table().list();
        TableList tablelist = listTables.execute();                     //
        // If there are no tables, print that info
        if (tablelist.getItems() == null || tablelist.getItems().isEmpty())
        {                                                               //
            System.out.println("No tables found!");                     //
        }                                                               //
        else                                                            // Else, loop through tables, find match
        {                                                               // If it matches then save the table ID
            for (Table table : tablelist.getItems())                    //
            {                                                           //
                if (table.getName().equals(tableName)) tid = table.getTableId();
            }                                                           // Printout the results of that
            System.out.println(tableName + " - tableId: " + tid);       //
        }                                                               //
        return tid;                                                     // Return the table ID
    }
*/

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     AddNewEntry
    //      Inputs:	    RF Data Entry (JSON)
    //     Outputs:	    Success = TRUE / Failure = FALSE
    // Description:     Insert new data to table  (executes SQL command)
    //	----------------------------------------------------------------------------------------------------------------
    public boolean AddNewEntry(String json)
    {
        boolean status;                                                 // Return status (success / failure)

        try                                                             // Try to get JSON, and save data to database
        {                                                               //
            Gson gson = new GsonBuilder().create();                     // Create Gson builder
            RFData RFMember = gson.fromJson(json, RFData.class);        // Convert from JSON to RFData
            if (RFMember.XbeeID != -1)                                  // If not default then save data
            {
                // Print debug information to port
                System.out.println("Insert New RF Data into Table - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI);

                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                String sql_string;                                      // Build up SQL string
                sql_string  = "INSERT INTO RF_Fields (";                // Insert SQL statement, Table: RF_Fields
                sql_string += "intXbeeID,";                             // Field: intXbeeID
                sql_string += "intDeviceID,";                           // Field: intDeviceID
                sql_string += "fltRSSI,";                               // Field: fltRSSI
                sql_string += "fltLatitude,";                           // Field: fltLatitude
                sql_string += "fltLongitude,";                          // Field: fltLongitude
                sql_string += "fltYaw,";                                // Field: fltYaw
                sql_string += "fltPitch,";                              // Field: fltPitch
                sql_string += "fltRoll,";                               // Field: fltRoll
                sql_string += "dtSampleDate) ";                         // Field: dtSampleDate
                sql_string += "VALUES (";                               // Values indetifier
                sql_string += RFMember.XbeeID + ",";                    // Value: XbeeID
                sql_string += RFMember.DeviceID + ",";                  // Value: DeviceID
                sql_string += RFMember.RSSI + ",";                      // Value: RSSI
                sql_string += RFMember.Latitude + ",";                  // Value: Latitude
                sql_string += RFMember.Longitude + ",";                 // Value: Longitude
                sql_string += RFMember.Yaw + ",";                       // Value: Yaw
                sql_string += RFMember.Pitch + ",";                     // Value: Pitch
                sql_string += RFMember.Roll + ",";                      // Value: Roll
                sql_string += "'" + ft.format(RFMember.SampleDate) + "')";
                System.out.println("SQL: " + sql_string);               // Debug print the SQL statement
                                                                        //
                Statement stmt = conn.createStatement();                // Build SQL statement
                stmt.execute(sql_string);                               // Execute the SQL statement
                stmt.close();                                           // Close the statement
                status = true;                                          // Success
/*
                // Add to fusion table
                String tableId = GetTableId("RF Field Data");           // Get the "Customer Data" Table
                sql_string  = "INSERT INTO " + tableId + " (";          // Insert SQL statement, Table: Table ID
                sql_string += "XbeeID,";                                // Field: intXbeeID
                sql_string += "DeviceID,";                              // Field: intDeviceID
                sql_string += "RSSI,";                                  // Field: fltRSSI
                sql_string += "Location,";                              // Field: fltLatitude
                sql_string += "Longitude,";                             // Field: fltLongitude
                sql_string += "Yaw,";                                   // Field: fltYaw
                sql_string += "Pitch,";                                 // Field: fltPitch
                sql_string += "Roll,";                                  // Field: fltRoll
                sql_string += "SampleDate) ";                           // Field: dtSampleDate
                sql_string += "VALUES (";                               // Values indetifier
                sql_string += RFMember.XbeeID + ",";                    // Value: XbeeID
                sql_string += RFMember.DeviceID + ",";                  // Value: DeviceID
                sql_string += RFMember.RSSI + ",";                      // Value: RSSI
                sql_string += RFMember.Latitude + ",";                  // Value: Latitude
                sql_string += RFMember.Longitude + ",";                 // Value: Longitude
                sql_string += RFMember.Yaw + ",";                       // Value: Yaw
                sql_string += RFMember.Pitch + ",";                     // Value: Pitch
                sql_string += RFMember.Roll + ",";                      // Value: Roll
                sql_string += "'" + ft.format(RFMember.SampleDate) + "')";
                Sql sql = FusionTables.query().sql(sql_string);         // Build Fusion Query
                // Try and execute the SQL command
                try                                                     //
                {                                                       //
                    sql.executeAndDownloadTo(System.out);               // Execute command, stream to the system.out
                }                                                       //
                catch (IllegalArgumentException e)                      //
                {                                                       //
                }                                                       //
*/
            }                                                           //
            else                                                        //
            {                                                           //
                System.err.println("AddNewEntry: Invalid JSON data");   // Print the exception data and exit
                status = false;                                         // Failure, invalid JSON or data
            }
        }                                                               //
        catch  ( Exception e )                                          // Exception processing:
        {                                                               //
            System.err.println("AddNewEntry: " + e.getMessage() );      // Print the exception data and exit
            status = false;                                             // Failure
        }                                                               //
        return status;                                                  // Return status
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     ReadRecords
    //      Inputs:	    Result Set and Array List
    //     Outputs:	    (by ref) Array List
    // Description:     Read the Records, Fill Array List with JSON data
    //	----------------------------------------------------------------------------------------------------------------
    public void ReadRecords(ResultSet rs, ArrayList records)
    {
        Gson gson = new GsonBuilder().create();                         // Create Gson builder
        try                                                             // Try to get JSON, and save data to database
        {                                                               //
            while (rs.next())                                           // Loop through all the returned records, until EOF
            {                                                           //
                RFData RFMember = new RFData();                         // Create new RF data

                // Capture the data from the record set
                RFMember.SampleNumber = rs.getInt("intSampleNum");      // Get sample #
                RFMember.XbeeID = rs.getInt("intXbeeID");               // Get Xbee ID
                RFMember.DeviceID = rs.getInt("intDeviceID");           // Get Device ID
                RFMember.RSSI = rs.getFloat("fltRSSI");                 // Get RSSI
                RFMember.Latitude = rs.getFloat("fltLatitude");         // Get Latitude
                RFMember.Longitude = rs.getFloat("fltLongitude");       // Get Longitude
                RFMember.Yaw = rs.getFloat("fltYaw");                   // Get Yaw
                RFMember.Pitch = rs.getFloat("fltPitch");               // Get Pitch
                RFMember.Roll = rs.getFloat("fltRoll");                 // Get Roll
                RFMember.SampleDate = rs.getTimestamp("dtSampleDate");  // Get Sample Date

                // Print debug information to port
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("Got Record: # " + RFMember.SampleNumber + " - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI + " Date/Time: " + ft.format(RFMember.SampleDate));

                String json_record = gson.toJson(RFMember);             // Get JSON data of this record
                records.add(json_record);                               // Add the JSON string to the record
            }
        }
        catch (Exception e)
        {
            System.err.println("ReadRecords: " + e.getMessage() );      // Print the exception data and exit
        }

    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     ListDataByEntryID
    //      Inputs:	    SampleNum
    //     Outputs:	    RF Data Entry (JSON)
    // Description:     Get RF Data Entry
    //	----------------------------------------------------------------------------------------------------------------
    public String ListDataByEntryID(int SampleNum)
    {
        String json = "";                                               // Return JSON entry

        try                                                             // Try to get JSON, and save data to database
        {                                                               //
                                                                        // Print debug information to port
            System.out.println("Select RF Data from Table - Sample #: " + SampleNum);

            String sql_string;                                          // Build up SQL string
            sql_string  = "SELECT ";                                    // Select SQL statement
            sql_string += "intSampleNum,";                              // Field: intSampleNum
            sql_string += "intXbeeID,";                                 // Field: intXbeeID
            sql_string += "intDeviceID,";                               // Field: intDeviceID
            sql_string += "fltRSSI,";                                   // Field: fltRSSI
            sql_string += "fltLatitude,";                               // Field: fltLatitude
            sql_string += "fltLongitude,";                              // Field: fltLongitude
            sql_string += "fltYaw,";                                    // Field: fltYaw
            sql_string += "fltPitch,";                                  // Field: fltPitch
            sql_string += "fltRoll,";                                   // Field: fltRoll
            sql_string += "dtSampleDate ";                              // Field: dtSampleDate
            sql_string += "FROM  RF_Fields ";                           // Table: RF_Fields
            sql_string += "WHERE ";                                     // Where statement
            sql_string += "intSampleNum = ";                            // Field on Where and condition
            sql_string += SampleNum;                                    // Condition value

            System.out.println("SQL: " + sql_string);                   // Debug print the SQL statement

            Statement stmt = conn.createStatement();                    // Build SQL statement
            ResultSet rs = stmt.executeQuery(sql_string);               // Execute the SQL statement as a query

            while ( rs.next() )                                         // Loop through all the returned records, until EOF
            {                                                           // However, there should only be one return record
                Gson gson = new GsonBuilder().create();                 // Create Gson builder
                RFData RFMember = new RFData();                         // Create new RF data

                // Capture the data from the record set
                RFMember.SampleNumber = rs.getInt("intSampleNum");      // Get sample #
                RFMember.XbeeID = rs.getInt("intXbeeID");               // Get Xbee ID
                RFMember.DeviceID = rs.getInt("intDeviceID");           // Get Device ID
                RFMember.RSSI = rs.getFloat("fltRSSI");                 // Get RSSI
                RFMember.Latitude = rs.getFloat("fltLatitude");         // Get Latitude
                RFMember.Longitude = rs.getFloat("fltLongitude");       // Get Longitude
                RFMember.Yaw = rs.getFloat("fltYaw");                   // Get Yaw
                RFMember.Pitch = rs.getFloat("fltPitch");               // Get Pitch
                RFMember.Roll = rs.getFloat("fltRoll");                 // Get Roll
                RFMember.SampleDate = rs.getTimestamp("dtSampleDate");  // Get Sample Date

                // Print debug information to port
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                System.out.println("Got Record: # " + RFMember.SampleNumber + " - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI + " Date/Time: " + ft.format(RFMember.SampleDate));

                json = gson.toJson(RFMember);                           // Get JSON data from this record
            }

            rs.close();                                                 // Close the record set
            stmt.close();                                               // Close the statement

            if (json == "")                                             //
            {                                                           //
                System.err.println("No Record Found!");                 // Print the fact that no record was found
            }
        }                                                               //
        catch  ( Exception e )                                          // Exception processing:
        {                                                               //
            System.err.println("ListDataByEntryID: " + e.getMessage() );// Print the exception data and exit
        }                                                               //
        return json;                                                    // Return JSON string
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     ListDataByDevice
    //      Inputs:	    DeviceID
    //     Outputs:	    RF Data Entry (JSON) Collection
    // Description:     Get RF Data Entries
    //	----------------------------------------------------------------------------------------------------------------
    public String ListDataByDevice(int DeviceID)
    {
        String json = "";                                               // Return JSON entry

        try                                                             // Try to get JSON, and save data to database
        {                                                               //
                                                                        // Print debug information to port
            Gson gson = new GsonBuilder().create();                     // Create Gson builder
            System.out.println("Select RF Data from Table - Device ID: " + DeviceID);

            String sql_string;                                          // Build up SQL string
            sql_string  = "SELECT ";                                    // Select SQL statement
            sql_string += "intSampleNum,";                              // Field: intSampleNum
            sql_string += "intXbeeID,";                                 // Field: intXbeeID
            sql_string += "intDeviceID,";                               // Field: intDeviceID
            sql_string += "fltRSSI,";                                   // Field: fltRSSI
            sql_string += "fltLatitude,";                               // Field: fltLatitude
            sql_string += "fltLongitude,";                              // Field: fltLongitude
            sql_string += "fltYaw,";                                    // Field: fltYaw
            sql_string += "fltPitch,";                                  // Field: fltPitch
            sql_string += "fltRoll,";                                   // Field: fltRoll
            sql_string += "dtSampleDate ";                              // Field: dtSampleDate
            sql_string += "FROM  RF_Fields ";                           // Table: RF_Fields
            sql_string += "WHERE ";                                     // Where statement
            sql_string += "intDeviceID = ";                             // Field on Where and condition
            sql_string += DeviceID;                                     // Condition value

            System.out.println("SQL: " + sql_string);                   // Debug print the SQL statement

            Statement stmt = conn.createStatement();                    // Build SQL statement
            ResultSet rs = stmt.executeQuery(sql_string);               // Execute the SQL statement as a query
            ArrayList records = new ArrayList();                        // Build the Array List
            ReadRecords(rs, records);                                   // Read the records
            json = gson.toJson(records);                                // Get JSON data from this record

            rs.close();                                                 // Close the record set
            stmt.close();                                               // Close the statement

            if (records.isEmpty() == true)                              // If empty then print out no records found
            {                                                           //
                System.err.println("No Records Found!");                // Print the fact that no record was found
            }                                                           //
        }                                                               //
        catch  ( Exception e )                                          // Exception processing:
        {                                                               //
            System.err.println("ListDataByDevice: " + e.getMessage() ); // Print the exception data and exit
        }                                                               //
        return json;                                                    // Return JSON string
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     ListDataByRSSI
    //      Inputs:	    RSSI, GTE
    //     Outputs:	    RF Data Entry (JSON) Collection
    // Description:     Get RF Data Entries
    //	----------------------------------------------------------------------------------------------------------------
    public String ListDataByRSSI(float RSSI, boolean GTE)
    {
        String json = "";                                               // Return JSON entry

        try                                                             // Try to get JSON, and save data to database
        {                                                               //
            Gson gson = new GsonBuilder().create();                     // Create Gson builder
            // Print debug information to port
            if (GTE)
                System.out.println("Select RF Data from Table - RSSI >= " + RSSI);
            else
                System.out.println("Select RF Data from Table - RSSI < " + RSSI);

            String sql_string;                                          // Build up SQL string
            sql_string  = "SELECT ";                                    // Select SQL statement
            sql_string += "intSampleNum,";                              // Field: intSampleNum
            sql_string += "intXbeeID,";                                 // Field: intXbeeID
            sql_string += "intDeviceID,";                               // Field: intDeviceID
            sql_string += "fltRSSI,";                                   // Field: fltRSSI
            sql_string += "fltLatitude,";                               // Field: fltLatitude
            sql_string += "fltLongitude,";                              // Field: fltLongitude
            sql_string += "fltYaw,";                                    // Field: fltYaw
            sql_string += "fltPitch,";                                  // Field: fltPitch
            sql_string += "fltRoll,";                                   // Field: fltRoll
            sql_string += "dtSampleDate ";                              // Field: dtSampleDate
            sql_string += "FROM  RF_Fields ";                           // Table: RF_Fields
            sql_string += "WHERE ";                                     // Where statement

            if (GTE)                                                    // Change condition based upon direction (GTE)
                sql_string += "fltRSSI >= ";                            // Field on Where and condition
            else
                sql_string += "fltRSSI < ";                             // Field on Where and condition

            sql_string += RSSI;                                         // Condition value

            System.out.println("SQL: " + sql_string);                   // Debug print the SQL statement

            Statement stmt = conn.createStatement();                    // Build SQL statement
            ResultSet rs = stmt.executeQuery(sql_string);               // Execute the SQL statement as a query
            ArrayList records = new ArrayList();                        // Build the Array List
            ReadRecords(rs, records);                                   // Read the records
            json = gson.toJson(records);                                // Get JSON data from this record
            rs.close();                                                 // Close the record set
            stmt.close();                                               // Close the statement

            if (records.isEmpty() == true)                              // If empty then print out no records found
            {                                                           //
                System.err.println("No Records Found!");                // Print the fact that no record was found
            }                                                           //
        }                                                               //
        catch  ( Exception e )                                          // Exception processing:
        {                                                               //
            System.err.println("ListDataByRSSI: " + e.getMessage() );   // Print the exception data and exit
        }                                                               //
        return json;                                                    // Return JSON string
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     ListDataByGeoArea
    //      Inputs:	    Start Latitude, Start Longitude, End Latitude, End Longitude (create rectangle)
    //     Outputs:	    RF Data Entry (JSON) Collection
    // Description:     Get RF Data Entries
    //	----------------------------------------------------------------------------------------------------------------
    public String ListDataByGeoArea(float StartLatitude, float StartLongitude, float EndLatitude, float EndLongitude)
    {
        String json = "";                                               // Return JSON entry

        try                                                             // Try to get JSON, and save data to database
        {                                                               //
            Gson gson = new GsonBuilder().create();                     // Create Gson builder
            // Print debug information to port
            System.out.println("Select RF Data from Table - GeoLocation");

            String sql_string;                                          // Build up SQL string
            sql_string  = "SELECT ";                                    // Select SQL statement
            sql_string += "intSampleNum,";                              // Field: intSampleNum
            sql_string += "intXbeeID,";                                 // Field: intXbeeID
            sql_string += "intDeviceID,";                               // Field: intDeviceID
            sql_string += "fltRSSI,";                                   // Field: fltRSSI
            sql_string += "fltLatitude,";                               // Field: fltLatitude
            sql_string += "fltLongitude,";                              // Field: fltLongitude
            sql_string += "fltYaw,";                                    // Field: fltYaw
            sql_string += "fltPitch,";                                  // Field: fltPitch
            sql_string += "fltRoll,";                                   // Field: fltRoll
            sql_string += "dtSampleDate ";                              // Field: dtSampleDate
            sql_string += "FROM  RF_Fields ";                           // Table: RF_Fields

                                                                        // If, 0,0,0,0 return all records

            sql_string += "WHERE (";                                    // Where statement
            sql_string += "fltLatitude >= " + StartLatitude + " AND ";  // Field on Where and condition
            sql_string += "fltLatitude >= " + StartLongitude + " AND "; // Field on Where and condition
            sql_string += "fltLatitude <= " + EndLatitude + " AND ";    // Field on Where and condition
            sql_string += "fltLongitude <= " + EndLongitude + ")";      // Field on Where and condition

            System.out.println("SQL: " + sql_string);                   // Debug print the SQL statement

            Statement stmt = conn.createStatement();                    // Build SQL statement
            ResultSet rs = stmt.executeQuery(sql_string);               // Execute the SQL statement as a query
            ArrayList records = new ArrayList();                        // Build the Array List
            ReadRecords(rs, records);                                   // Read the records
            json = gson.toJson(records);                                // Get JSON data from this record

            rs.close();                                                 // Close the record set
            stmt.close();                                               // Close the statement

            if (records.isEmpty() == true)                              // If empty then print out no records found
            {                                                           //
                System.err.println("No Records Found!");                // Print the fact that no record was found
            }                                                           //
        }                                                               //
        catch  ( Exception e )                                          // Exception processing:
        {                                                               //
            System.err.println("ListDataByGeoArea: " + e.getMessage() );// Print the exception data and exit
        }                                                               //
        return json;                                                    // Return JSON string
    }
}
