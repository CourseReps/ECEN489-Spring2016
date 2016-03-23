// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         RFFieldSQLDatabase.java
 * @brief        Project #2 - SQL Database Interface Class
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------
package com.example.fanchaozhou.project1;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

//----------------------------------------------------------------------------------------------------------------------

/** @class      RFFieldSQLDatabase
 *  @brief      RFFieldSQLDatabase interfaces SQL Database, supplies methods to access data and passes
 */
public class RFFieldSQLDatabase
{
    // Define class members
    private Connection conn = null;                                     /// MySql Database connection

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    RFFieldSQLDatabase - Constructor
     *
     *           RFFieldSQLDatabase, no initialization given
     */
    public RFFieldSQLDatabase()
    {
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    getConnection
     *
     *           Inputs: URL for server, username, password
     *           Return: Connection
     *           Connects to MySql database
     */
    private static Connection getConnection(String dbURL, String user, String password) throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");                         /// Setup for the MySql JDBC Driver
        Properties props = new Properties();                            /// Build the properties
        props.put("user", user);
        props.put("password", password);
        props.put("autoReconnect", "true");                             /// Enabled auto-reconnection
        return DriverManager.getConnection(dbURL, props);               /// Return the connection to the database
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    ConnectToDatabase
     *
     *           Inputs: host address for server, username, password
     *           Return: Success = TRUE / Failure = FALSE
     *           Establishes a connection to the database
     */
    public boolean ConnectToDatabase(String host_address)
    {
        boolean status;                                                 /// Return status (success / failure)

        try                                                             /// Try and connect to the MySQL Database
        {                                                               /// Use ECEN_RF_Field Database
            System.out.println("Attempting Connection");                /// Print out connection success
            conn = getConnection("jdbc:mysql://" + host_address + "/ECEN_RF_Fields", "ecen689", "ecen689$2016");
            System.out.println("Opened database successfully");         /// Print out connection success
            status = true;                                              /// Success
        }                                                               //
        catch  ( Exception e )                                          /// Exception processing:
        {                                                               ///
            System.err.println("ConnectToDatabase: " + e.getMessage() );/// Print the exception data and exit
            status = false;                                             /// Failure
        }                                                               ///
        return status;                                                  /// Return status
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    DisconnectDatabase
     *
     *           Inputs: none
     *           Return: Success = TRUE / Failure = FALSE
     *           Disconnects the connection to the database
     */
    public boolean DisconnectDatabase()
    {
        boolean status;                                                 /// Return status (success / failure)

        try                                                             /// Try and connect to the MySQL Database
        {                                                               ///
            if (conn != null)  conn.close();                            /// Close the connection if defined
            System.out.println("Database connection closed");           /// Print out status
            status = true;                                              /// Success
        }                                                               ///
        catch  ( Exception e )                                          /// Exception processing:
        {                                                               /// Print the exception data and exit
            System.err.println("DisconnectDatabase: " + e.getMessage() );
            status = false;                                             /// Failure
        }                                                               ///
        return status;                                                  /// Return status
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    AddNewEntry
     *
     *           Inputs: RF Data Entry
     *           Return: Success = TRUE / Failure = FALSE
     *           Insert new data to table  (executes SQL command)
     */
    public boolean AddNewEntry(RFData RFMember)
    {
        boolean status;                                                 /// Return status (success / failure)

        try                                                             /// Try to get JSON, and save data to database
        {
            if (RFMember.XbeeID != -1)                                  /// If not default then save data
            {
                // Print debug information to port
                System.out.println("Insert New RF Data into Table - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI);

                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                String sql_string;                                      /// Build up SQL string
                sql_string  = "INSERT INTO RF_Fields (";                /// Insert SQL statement, Table: RF_Fields
                sql_string += "intXbeeID,";                             /// Field: intXbeeID
                sql_string += "intDeviceID,";                           /// Field: intDeviceID
                sql_string += "fltRSSI,";                               /// Field: fltRSSI
                sql_string += "fltLatitude,";                           /// Field: fltLatitude
                sql_string += "fltLongitude,";                          /// Field: fltLongitude
                sql_string += "fltYaw,";                                /// Field: fltYaw
                sql_string += "fltPitch,";                              /// Field: fltPitch
                sql_string += "fltRoll,";                               /// Field: fltRoll
                sql_string += "dtSampleDate) ";                         /// Field: dtSampleDate
                sql_string += "VALUES (";                               /// Values indetifier
                sql_string += RFMember.XbeeID + ",";                    /// Value: XbeeID
                sql_string += RFMember.DeviceID + ",";                  /// Value: DeviceID
                sql_string += RFMember.RSSI + ",";                      /// Value: RSSI
                sql_string += RFMember.Latitude + ",";                  /// Value: Latitude
                sql_string += RFMember.Longitude + ",";                 /// Value: Longitude
                sql_string += RFMember.Yaw + ",";                       /// Value: Yaw
                sql_string += RFMember.Pitch + ",";                     /// Value: Pitch
                sql_string += RFMember.Roll + ",";                      /// Value: Roll
                sql_string += "'" + ft.format(RFMember.SampleDate) + "')";
                System.out.println("SQL: " + sql_string);               /// Debug print the SQL statement

                Statement stmt = conn.createStatement();                /// Build SQL statement
                stmt.execute(sql_string);                               /// Execute the SQL statement
                stmt.close();                                           /// Close the statement
                status = true;                                          /// Success
            }
            else
            {
                System.err.println("AddNewEntry: Invalid JSON data");   /// Print the exception data and exit
                status = false;                                         /// Failure, invalid JSON or data
            }
        }
        catch  ( Exception e )                                          /// Exception processing:
        {
            System.err.println("AddNewEntry: " + e.getMessage() );      /// Print the exception data and exit
            status = false;                                             /// Failure
        }
        return status;                                                  /// Return status
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    ReadRecords
     *
     *           Inputs: Result Set
     *           Return: ArrayList of RF Data (by ref)
     *           Read the Records, Fill Array List with RF Data
     */
    private void ReadRecords(ResultSet rs, ArrayList records)
    {
        try                                                             /// Try to get JSON, and save data to database
        {
            while (rs.next())                                           /// Loop through all the returned records, until EOF
            {
                RFData RFMember = new RFData();                         /// Create new RF data

                // Capture the data from the record set
                RFMember.SampleNumber = rs.getInt("intSampleNum");      /// Get sample #
                RFMember.XbeeID = rs.getInt("intXbeeID");               /// Get Xbee ID
                RFMember.DeviceID = rs.getInt("intDeviceID");           /// Get Device ID
                RFMember.RSSI = rs.getFloat("fltRSSI");                 /// Get RSSI
                RFMember.Latitude = rs.getFloat("fltLatitude");         /// Get Latitude
                RFMember.Longitude = rs.getFloat("fltLongitude");       /// Get Longitude
                RFMember.Yaw = rs.getFloat("fltYaw");                   /// Get Yaw
                RFMember.Pitch = rs.getFloat("fltPitch");               /// Get Pitch
                RFMember.Roll = rs.getFloat("fltRoll");                 /// Get Roll
                RFMember.SampleDate = rs.getTimestamp("dtSampleDate");  /// Get Sample Date

                // Print debug information to port
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("Got Record: # " + RFMember.SampleNumber + " - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI + " Date/Time: " + ft.format(RFMember.SampleDate));

                records.add(RFMember);                                  /// Add the member to the record
            }
        }
        catch (Exception e)
        {
            System.err.println("ReadRecords: " + e.getMessage() );      /// Print the exception data and exit
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    ListDataByGeoArea
     *
     *           Inputs:  Start Latitude, Start Longitude, End Latitude, End Longitude (create rectangle)
     *           Return: RF Data Entryies (ArrayList)
     *           Get RF Data Entries given Geo Constraints
     */
    public ArrayList ListDataByGeoArea(float StartLatitude, float StartLongitude, float EndLatitude, float EndLongitude)
    {
        ArrayList records = new ArrayList();                            // Build and return the Array List

        try
        {
            /// Print debug information to port
            System.out.println("Select RF Data from Table - GeoLocation");

            String sql_string;                                          /// Build up SQL string
            sql_string  = "SELECT ";                                    /// Select SQL statement
            sql_string += "intSampleNum,";                              /// Field: intSampleNum
            sql_string += "intXbeeID,";                                 /// Field: intXbeeID
            sql_string += "intDeviceID,";                               /// Field: intDeviceID
            sql_string += "fltRSSI,";                                   /// Field: fltRSSI
            sql_string += "fltLatitude,";                               /// Field: fltLatitude
            sql_string += "fltLongitude,";                              /// Field: fltLongitude
            sql_string += "fltYaw,";                                    /// Field: fltYaw
            sql_string += "fltPitch,";                                  /// Field: fltPitch
            sql_string += "fltRoll,";                                   /// Field: fltRoll
            sql_string += "dtSampleDate ";                              /// Field: dtSampleDate
            sql_string += "FROM  RF_Fields ";                           /// Table: RF_Fields

                                                                        /// If, 0,0,0,0 return all records
            if ((StartLatitude != 0) | (StartLongitude != 0) | (EndLatitude != 0) | (EndLongitude != 0))
            {
                sql_string += "WHERE (";                                    /// Where statement
                sql_string += "fltLatitude >= " + StartLatitude + " AND ";  /// Field on Where and condition
                sql_string += "fltLatitude >= " + StartLongitude + " AND "; /// Field on Where and condition
                sql_string += "fltLatitude <= " + EndLatitude + " AND ";    /// Field on Where and condition
                sql_string += "fltLongitude <= " + EndLongitude + ")";      /// Field on Where and condition
            }

            System.out.println("SQL: " + sql_string);                   /// Debug print the SQL statement

            Statement stmt = conn.createStatement();                    /// Build SQL statement
            ResultSet rs = stmt.executeQuery(sql_string);               /// Execute the SQL statement as a query
            ReadRecords(rs, records);                                   /// Read the records

            rs.close();                                                 /// Close the record set
            stmt.close();                                               /// Close the statement

            if (records.isEmpty() == true)                              /// If empty then print out no records found
            {                                                           ///
                System.err.println("No Records Found!");                /// Print the fact that no record was found
            }                                                           ///
        }
        catch  ( Exception e )                                          /// Exception processing:
        {
            System.err.println("ListDataByGeoArea: " + e.getMessage() );/// Print the exception data and exit
        }
        return records;                                                 /// Return Record Set
    }
}
