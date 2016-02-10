// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		Main.java
// Version:			1.0.0
// Date:			February 8, 2016
// Description:	    Task #5 - Main Class (Google Fusion Tables)
//                  Use Google Fusion Tables
//
// Author:          John Lusher II
//                  Note: Code a modified version of example code found at https://developers.google.com
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           February 8, 2016  	         Initial Release
//  --------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.text.*;


//  --------------------------------------------------------------------------------------------------------------------
//        Class:    Main
//  Description:	Main class for project
//  --------------------------------------------------------------------------------------------------------------------
public class Main
{
                                                                        // ---------------------------------------------
                                                                        // Set the Application Name
    private static final String APPLICATION_NAME = "ECEN689Task5TestProject";
                                                                        // Directory to store user credentials for this
                                                                        // application.
    private static final java.io.File DATA_STORE_DIR =
        new java.io.File(System.getProperty("user.dir"),
        ".store/fusion_tables_sample");
                                                                        //
    private static FileDataStoreFactory DATA_STORE_FACTORY;             // Global instance of the {@link FileDataStoreFactory}.
    private static HttpTransport HTTP_TRANSPORT;                        // Global instance of the HTTP transport
                                                                        // Global instance of the JSON factory.
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Fusiontables FusionTables;                           // Global instance of the Fusion Tables
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
    static                                                              // Create new instances of HTTP_TRANSPORT
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
                                                                        // ---------------------------------------------

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     authorize
    //      Inputs:	    none
    //     Outputs:	    Credentials
    // Description:     Creates an authorized credential object, will throw IO exception
    //	----------------------------------------------------------------------------------------------------------------
    private static Credential authorize() throws IOException
    {
                                                                        // ---------------------------------------------
                                                                        // Load client secrets (from JSON file)
        InputStream in = Main.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
                                                                        //
                                                                        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
            Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setDataStoreFactory(DATA_STORE_FACTORY).build();
                                                                        //
                                                                        // Authorize and get the credential
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;                                              // Return the credential
                                                                        // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     Main
    //      Inputs:	    arguments
    //     Outputs:	    none
    // Description:     Defines and initializes the class: main
    //	----------------------------------------------------------------------------------------------------------------
    public static void main(String[] args)
    {
        try
        {
                                                                        // ---------------------------------------------
            Credential credential = authorize();                        // Client authorization
                                                                        // Set up global FusionTables instance
            FusionTables = new Fusiontables.Builder(HTTP_TRANSPORT,
                                                    JSON_FACTORY,
                                                    credential).setApplicationName(APPLICATION_NAME).build();
                                                                        //
            ListTables();                                               // List the tables
            String tableId = GetTableId("Customer Data");               // Get the "Customer Data" Table
            InsertData(tableId);                                        // Insert a new row into table
            GetRows(tableId);                                           // Get the rows that exist
        }                                                               //
        catch (IOException e)                                           // Print out exception messages
        {                                                               //
            System.err.println(e.getMessage());                         //
        }                                                               //
        catch (Throwable t)                                             //
        {                                                               //
            t.printStackTrace();                                        //
        }                                                               //
        System.exit(1);                                                 //
    }                                                                   // ---------------------------------------------

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     InsertData
    //      Inputs:	    none
    //     Outputs:	    none
    // Description:     Insert new data to table  (executes SQL command)
    //	----------------------------------------------------------------------------------------------------------------
    private static String GetTableId(String tableName) throws IOException
    {
                                                                        // ---------------------------------------------
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
                                                                        //----------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     ListTables
    //      Inputs:	    none
    //     Outputs:	    none
    // Description:     List tables in fusion tables
    //	----------------------------------------------------------------------------------------------------------------
    private static void ListTables() throws IOException
    {
                                                                        // ---------------------------------------------
        System.out.println("List of Tables:");                          // Print header
                                                                        //
                                                                        // Get the table list
        Fusiontables.Table.List listTables = FusionTables.table().list();
        TableList tablelist = listTables.execute();                     //
                                                                        // If there is no talbes then list it as such
        if (tablelist.getItems() == null || tablelist.getItems().isEmpty())
        {                                                               //
            System.out.println("No tables found!");                     //
        }                                                               //
        else                                                            //
        {                                                               //
            for (Table table : tablelist.getItems())                    // Loop through all tables, print out data
            {                                                           //
                System.out.println("--------------------------------------------------");
                System.out.println("id: " + table.getTableId());
                System.out.println("name: " + table.getName());
                System.out.println("description: " + table.getDescription());
                System.out.println("attribution: " + table.getAttribution());
                System.out.println("attribution link: " + table.getAttributionLink());
                System.out.println("kind: " + table.getKind());
                System.out.println("--------------------------------------------------");
            }                                                           //
        }                                                               // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     ShowRows
    //      Inputs:	    Table ID
    //     Outputs:	    none
    // Description:     Gets ALL the rows from the table (executes SQL command)
    //	----------------------------------------------------------------------------------------------------------------
    private static void GetRows(String tableId) throws IOException
    {
                                                                        // ---------------------------------------------
        System.out.println("Rows of Table: " + tableId);                // Print header
                                                                        // Build SQL command for table
        Sql sql = FusionTables.query().sql("SELECT FirstName,LastName,PhoneNumber,Location,LocationLongitude,Date FROM " + tableId);
                                                                        // Try and execute the SQL command
        try                                                             //
        {                                                               //
            sql.executeAndDownloadTo(System.out);                       // Execute command, stream to the system.out
        }                                                               //
        catch (IllegalArgumentException e)                              //
        {                                                               //
        }                                                               // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     InsertData
    //      Inputs:	    none
    //     Outputs:	    none
    // Description:     Insert new data to table  (executes SQL command)
    //	----------------------------------------------------------------------------------------------------------------
    private static void InsertData(String tableId) throws IOException
    {
                                                                        // ---------------------------------------------
        System.out.println("Insert New Data in Table: " + tableId);     // Print header
                                                                        // Build SQL command for table
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");


        System.out.println("INSERT INTO " + tableId + " (FirstName,LastName,PhoneNumber,Location,LocationLongitude,Date) "
                        + "VALUES (" + "'Joe', "  + "'Doe', " +  "'979-555-1234', " + "30.720737, " + "'-96.249564', '" + ft.format(new Date()) + "')");

        Sql sql = FusionTables.query().sql("INSERT INTO " + tableId + " (FirstName,LastName,PhoneNumber,Location,LocationLongitude,Date) "
                + "VALUES (" + "'Joe', "  + "'Doe', " +  "'979-555-1234', " + "30.720737, " + "'-96.249564', '" + ft.format(new Date()) + "')");
                                                                        // Try and execute the SQL command
        try                                                             //
        {                                                               //
            sql.executeAndDownloadTo(System.out);                       // Execute command, stream to the system.out
        }                                                               //
        catch (IllegalArgumentException e)                              //
        {                                                               //
        }                                                               // ---------------------------------------------
    }
}
