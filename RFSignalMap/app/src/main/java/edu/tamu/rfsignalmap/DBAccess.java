// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         DBAccess.java
 * @brief        Project #3 - SQL Database Interface Class
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------


package edu.tamu.rfsignalmap;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @class DBAccess
 *
 * @brief Creates a local SQLite database and data table in that database, as well as
 * manages saving and retreiving data from the database. It extends the SQLiteOpenHelper class.
 */
public class DBAccess extends SQLiteOpenHelper{

    public static final String DB_NAME 			  = "DATA.db";

    public static final String COLUMN_XBEE_ID     = "intXbeeID";
    public static final String COLUMN_DEVICE_ID   = "intDeviceID";
    public static final String COLUMN_XBEE2_ID    = "intXbeeID2";
    public static final String COLUMN_DEVICE2_ID  = "intDeviceID2";
    public static final String COLUMN_XBEE3_ID    = "intXbeeID3";
    public static final String COLUMN_DEVICE3_ID  = "intDeviceID3";
    public static final String COLUMN_RSSI		  = "fltRSSI";
    public static final String COLUMN_RSSI2		  = "fltRSSI2";
    public static final String COLUMN_RSSI3		  = "fltRSSI3";
    public static final String COLUMN_LAT 		  = "fltLatitude";
    public static final String COLUMN_LONG        = "fltLongitude";
    public static final String COLUMN_YAW 		  = "fltYaw";
    public static final String COLUMN_PITCH 	  = "fltPitch";
    public static final String COLUMN_ROLL 		  = "fltRoll";
    public static final String COLUMN_LATVN 	  = "fltLatitudeVN";
    public static final String COLUMN_LONGVN      = "fltLongitudeVN";
    public static final String COLUMN_YAWVN 	  = "fltYawVN";
    public static final String COLUMN_PITCHVN 	  = "fltPitchVN";
    public static final String COLUMN_ROLLVN 	  = "fltRollVN";
    public static final String COLUMN_TIMESTAMP	  = "dtSampleDate";
    public static final String COLUMN_CELL	      = "txtCellSignalStrength";
    public static final String COLUMN_ANT_STATE   = "intAntState";

    /** creating a database and connecting **/
    /**
     * @fn DBAccess
     * @brief Constructor for the DBAccess class. DBAccess extends SQLiteOpenHelper, so it just uses the super constructor.
     * The parameters passed are the current context, the name of the database to create/open, the cursor factory (null),
     * and the database version.
     * */
    DBAccess(Context context){

        super(context, DB_NAME, null, 1);

    }


    /**
     * @fn onCreate
     * @brief When the class is instantiated, onCreate creates a table called DATA in the database, if there is not already an
     * existing table with that name. The table contains columns for the Xbee ID, Device ID, RSSI, Latitude and Longitude,
     * Yaw, Pitch, and Roll, Timestamp, and an indicator field showing whether or not a row has been sent to the server.
     */
    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("create table if not exists DATA " +
                "(intXbeeID INT(11), " +
                "intDeviceID INT(11), " +
                "intXbeeID2 INT(11), " +
                "intDeviceID2 INT(11), " +
                "intXbeeID3 INT(11), " +
                "intDeviceID3 INT(11), " +
                "intAntState INT(11), " +
                "fltRSSI DOUBLE, " +
                "fltRSSI2 DOUBLE, " +
                "fltRSSI3 DOUBLE, " +
                "txtCellSignalStrength LONGTEXT, " +
                "fltLatitude DOUBLE, " +
                "fltLongitude DOUBLE, " +
                "fltYaw DOUBLE, " +
                "fltPitch DOUBLE, " +
                "fltRoll DOUBLE, " +
                "fltLatitudeVN DOUBLE, " +
                "fltLongitudeVN DOUBLE, " +
                "fltYawVN DOUBLE, " +
                "fltPitchVN DOUBLE, " +
                "fltRollVN DOUBLE, " +
                "dtSampleDate DATETIME)");

    }


    /**
     * @fn onUpgrade
     * @brief  method for upgrading to a new database version
     * (this is not important for this project, it was just necessary for SQLiteOpenHelper)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS DATA");
        onCreate(db);

    }


    /**
     * @fn addData
     * @brief addData is a method for adding data to the database. It parses the JSON object passed to it and uses the 
     * classes ContentValues and SQLiteDatabase to write those values to the database. If successful, it returns true.
     */
    public boolean addData(RFData RFMember){

        try {
            /** INSERT INTO DB **/
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("intXbeeID", RFMember.XbeeID);
            contentValues.put("intDeviceID", RFMember.DeviceID);
            contentValues.put("intXbeeID2", RFMember.XbeeID2);
            contentValues.put("intDeviceID2", RFMember.DeviceID2);
            contentValues.put("intXbeeID3", RFMember.XbeeID3);
            contentValues.put("intDeviceID3", RFMember.DeviceID3);
            contentValues.put("intAntState", RFMember.RFAntennaState);
            contentValues.put("fltRSSI", RFMember.RSSI);
            contentValues.put("fltRSSI2", RFMember.RSSI2);
            contentValues.put("fltRSSI3", RFMember.RSSI3);
            contentValues.put("fltLatitude", RFMember.Latitude);
            contentValues.put("fltLongitude", RFMember.Longitude);
            contentValues.put("fltYaw", RFMember.Yaw);
            contentValues.put("fltPitch", RFMember.Pitch);
            contentValues.put("fltRoll", RFMember.Roll);
            contentValues.put("fltLatitudeVN", RFMember.VecNav_Latitude);
            contentValues.put("fltLongitudeVN", RFMember.VecNav_Longitude);
            contentValues.put("fltYawVN", RFMember.VecNav_Yaw);
            contentValues.put("fltPitchVN", RFMember.VecNav_Pitch);
            contentValues.put("fltRollVN", RFMember.VecNav_Roll);
            contentValues.put("dtSampleDate", ft.format(RFMember.SampleDate));
            contentValues.put("txtCellSignalStrength", RFMember.CellSignalStrength);
            db.insert("DATA", null, contentValues);

        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList getUnsentData(){
        ArrayList records = new ArrayList();                    // Build and return the Array List
        RFData RFMember = new RFData();                         // Create new RF data
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /** PREPARE TO READ AND WRITE DATA **/
        SQLiteDatabase read = this.getReadableDatabase();
        Cursor cursor = read.rawQuery("SELECT * FROM Data", null);
        cursor.moveToFirst();

        /** ITERATE THROUGH DATA **/
        while(!cursor.isAfterLast()){
            RFMember.XbeeID = cursor.getInt(cursor.getColumnIndex(COLUMN_XBEE_ID));
            RFMember.DeviceID = cursor.getInt(cursor.getColumnIndex(COLUMN_DEVICE_ID));
            RFMember.XbeeID2 = cursor.getInt(cursor.getColumnIndex(COLUMN_XBEE2_ID));
            RFMember.DeviceID2 = cursor.getInt(cursor.getColumnIndex(COLUMN_DEVICE2_ID));
            RFMember.XbeeID3 = cursor.getInt(cursor.getColumnIndex(COLUMN_XBEE3_ID));
            RFMember.DeviceID3 = cursor.getInt(cursor.getColumnIndex(COLUMN_DEVICE3_ID));
            RFMember.RFAntennaState = cursor.getInt(cursor.getColumnIndex(COLUMN_ANT_STATE));
            RFMember.RSSI = cursor.getDouble(cursor.getColumnIndex(COLUMN_RSSI));
            RFMember.RSSI2 = cursor.getDouble(cursor.getColumnIndex(COLUMN_RSSI2));
            RFMember.RSSI3 = cursor.getDouble(cursor.getColumnIndex(COLUMN_RSSI3));
            RFMember.Latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LAT));
            RFMember.Longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONG));
            RFMember.Yaw = cursor.getDouble(cursor.getColumnIndex(COLUMN_YAW));
            RFMember.Pitch = cursor.getDouble(cursor.getColumnIndex(COLUMN_PITCH));
            RFMember.Roll = cursor.getDouble(cursor.getColumnIndex(COLUMN_ROLL));
            RFMember.VecNav_Latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATVN));
            RFMember.VecNav_Longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGVN));
            RFMember.VecNav_Yaw = cursor.getDouble(cursor.getColumnIndex(COLUMN_YAWVN));
            RFMember.VecNav_Pitch = cursor.getDouble(cursor.getColumnIndex(COLUMN_PITCHVN));
            RFMember.VecNav_Roll = cursor.getDouble(cursor.getColumnIndex(COLUMN_ROLLVN));
            RFMember.CellSignalStrength = cursor.getString(cursor.getColumnIndex(COLUMN_CELL));
            String strTS = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
            try {
                RFMember.SampleDate = ft.parse(strTS);
            }
            catch (Exception e){
            }

            records.add(records);
            cursor.moveToNext();

        }

        cursor.close();

        /** DELETE DATA FROM DATABASE */
        SQLiteDatabase write = this.getWritableDatabase();
        write.execSQL("DELETE * FROM Data");

        return records;
    }

    public boolean TransferData(){
        RFFieldSQLDatabase RFFieldDatabase = new RFFieldSQLDatabase();
        /// Connect to test server (for now), if not connected return null
        if (RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com") == true) {
            RFData RFMember = new RFData();                         // Create new RF data
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            /** PREPARE TO READ AND WRITE DATA **/
            SQLiteDatabase read = this.getReadableDatabase();
            Cursor cursor = read.rawQuery("SELECT * FROM Data", null);
            cursor.moveToFirst();

            /** ITERATE THROUGH DATA **/
            while (!cursor.isAfterLast()) {
                RFMember.XbeeID = cursor.getInt(cursor.getColumnIndex(COLUMN_XBEE_ID));
                RFMember.DeviceID = cursor.getInt(cursor.getColumnIndex(COLUMN_DEVICE_ID));
                RFMember.XbeeID2 = cursor.getInt(cursor.getColumnIndex(COLUMN_XBEE2_ID));
                RFMember.DeviceID2 = cursor.getInt(cursor.getColumnIndex(COLUMN_DEVICE2_ID));
                RFMember.XbeeID3 = cursor.getInt(cursor.getColumnIndex(COLUMN_XBEE3_ID));
                RFMember.DeviceID3 = cursor.getInt(cursor.getColumnIndex(COLUMN_DEVICE3_ID));
                RFMember.RFAntennaState = cursor.getInt(cursor.getColumnIndex(COLUMN_ANT_STATE));
                RFMember.RSSI = cursor.getDouble(cursor.getColumnIndex(COLUMN_RSSI));
                RFMember.RSSI2 = cursor.getDouble(cursor.getColumnIndex(COLUMN_RSSI2));
                RFMember.RSSI3 = cursor.getDouble(cursor.getColumnIndex(COLUMN_RSSI3));
                RFMember.Latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LAT));
                RFMember.Longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONG));
                RFMember.Yaw = cursor.getDouble(cursor.getColumnIndex(COLUMN_YAW));
                RFMember.Pitch = cursor.getDouble(cursor.getColumnIndex(COLUMN_PITCH));
                RFMember.Roll = cursor.getDouble(cursor.getColumnIndex(COLUMN_ROLL));
                RFMember.VecNav_Latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATVN));
                RFMember.VecNav_Longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGVN));
                RFMember.VecNav_Yaw = cursor.getDouble(cursor.getColumnIndex(COLUMN_YAWVN));
                RFMember.VecNav_Pitch = cursor.getDouble(cursor.getColumnIndex(COLUMN_PITCHVN));
                RFMember.VecNav_Roll = cursor.getDouble(cursor.getColumnIndex(COLUMN_ROLLVN));
                RFMember.CellSignalStrength = cursor.getString(cursor.getColumnIndex(COLUMN_CELL));
                String strTS = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
                try {
                    RFMember.SampleDate = ft.parse(strTS);
                } catch (Exception e) {
                }


                /// Store data to database, return the results
                if (RFFieldDatabase.AddNewEntry(RFMember) == false) return false;
                cursor.moveToNext();
            }
            cursor.close();
        }
        else{
            return false;
        }

        /** DELETE DATA FROM DATABASE */
        //SQLiteDatabase write = this.getWritableDatabase();
        //write.execSQL("DELETE * FROM Data");
        clearData();
        return true;
    }

    /**
     * @fn clearData
     * @brief method to clear all data from the table
     */
    public void clearData(){

        /** CLEAR ALL DATA IN TABLE **/
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("DATA", null, null);
    }
}


