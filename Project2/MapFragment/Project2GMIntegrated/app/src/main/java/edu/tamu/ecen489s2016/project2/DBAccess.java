/**
 * @file DBAccess.java
 *
 * @brief Contains every function needed to interact with local SQlite database
 *
 **/
package edu.tamu.ecen489s2016.project2;

/**
 * Created by Sam on 2/20/2016.
 */

import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @class DBAccess
 *
 * @brief Creates DB, Table, and manages input and output of data
 */
public class DBAccess extends SQLiteOpenHelper{

    public static final String DB_NAME 			  = "DATA.db";
    public static final String COLUMN_XBEE_ID     = "XbeeID";
    public static final String COLUMN_RSSI		  = "RSSI";
    public static final String COLUMN_DEVICE_ID   = "DeviceID";
    public static final String COLUMN_LAT 		  = "Latitude";
    public static final String COLUMN_LONG        = "Longitude";
    public static final String COLUMN_YAW 		  = "Yaw";
    public static final String COLUMN_PITCH 	  = "Pitch";
    public static final String COLUMN_ROLL 		  = "Roll";
    public static final String COLUMN_TIMESTAMP	  = "SampleDate";

    /** creating a database and connecting **/
    DBAccess(Context context){

        super(context, DB_NAME, null, 1);

    }


    /**
     * @fn onCreate
     * @brief create a table in the database with columns for Transmit ID, RSSI, Receive ID, GPS, IMU, Timestamp, and a sent field
     */
    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("create table if not exists DATA " + "(XbeeID text, RSSI real, DeviceID text, Latitude real, " +
                "Longitude real, Yaw real, Pitch real, Roll real, SampleDate text, sent text)");

    }


    /**
     * @fn onUpgrade
     * @brief  method for upgrading to a new database (this is not important, it was just necessary for SQLiteOpenHelper)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS DATA");
        onCreate(db);

    }


    /**
     * @fn addData
     * @brief method for adding data to the database
     */
    public boolean addData(JSONObject data){

        try {
            /** PARSE JSON OBJECT **/
            String transmitID = data.getString("XbeeID");
            String receiveID = data.getString("DeviceID");
            float rssi = BigDecimal.valueOf(data.getDouble("RSSI")).floatValue();
            float latitude = BigDecimal.valueOf(data.getDouble("Latitude")).floatValue();
            float longitude = BigDecimal.valueOf(data.getDouble("Longitude")).floatValue();
            float yaw = BigDecimal.valueOf(data.getDouble("Yaw")).floatValue();
            float pitch = BigDecimal.valueOf(data.getDouble("Pitch")).floatValue();
            float roll = BigDecimal.valueOf(data.getDouble("Roll")).floatValue();
            String timestamp = data.getString("SampleDate");
            String sent = "no";


            /** INSERT INTO DB **/
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("XbeeID", transmitID);
            contentValues.put("RSSI", rssi);
            contentValues.put("DeviceID", receiveID);
            contentValues.put("Latitude", latitude);
            contentValues.put("Longitude", longitude);
            contentValues.put("Yaw", yaw);
            contentValues.put("Pitch", pitch);
            contentValues.put("Roll", roll);
            contentValues.put("SampleDate", timestamp);
            contentValues.put("sent", sent);
            db.insert("DATA", null, contentValues);

        }catch(JSONException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @fn getUnsentData
     * @brief method to return data that has not been sent to the database
     */
    public JSONArray getUnsentData(){

        JSONObject entry = new JSONObject();
        String deviceID;
        float rssi;
        String xbeeID;
        float latitude;
        float longitude;
        float yaw;
        float pitch;
        float roll;
        String timestamp;
        Date date;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JSONArray resultsArray = new JSONArray();

        /** PREPARE TO READ AND WRITE DATA **/
        SQLiteDatabase read = this.getReadableDatabase();
        Cursor cursor = read.rawQuery( "SELECT * FROM Data WHERE sent='no'", null);
        cursor.moveToFirst();


        /** ITERATE THROUGH UNSENT DATA **/
        while(!cursor.isAfterLast()){

            /** PULL DATA FROM EACH COLUMN OF THE TABLE **/
            deviceID   = cursor.getString(cursor.getColumnIndex(COLUMN_DEVICE_ID));
            rssi 	   = cursor.getFloat(cursor.getColumnIndex(COLUMN_RSSI));
            xbeeID     = cursor.getString(cursor.getColumnIndex(COLUMN_XBEE_ID));
            latitude   = cursor.getFloat(cursor.getColumnIndex(COLUMN_LAT));
            longitude  = cursor.getFloat(cursor.getColumnIndex(COLUMN_LONG));
            yaw        = cursor.getFloat(cursor.getColumnIndex(COLUMN_YAW));
            pitch      = cursor.getFloat(cursor.getColumnIndex(COLUMN_PITCH));
            roll       = cursor.getFloat(cursor.getColumnIndex(COLUMN_ROLL));
            timestamp  = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));



            /** PUT DATA INTO JSON OBJECT **/
            try {
                entry.put("DeviceID", deviceID);
                entry.put("RSSI", rssi);
                entry.put("XbeeID", xbeeID);
                entry.put("Latitude", latitude);
                entry.put("Longitude", longitude);
                entry.put("Yaw", yaw);
                entry.put("Pitch", pitch);
                entry.put("Roll", roll);

                date = format.parse(timestamp); //convert to date
                entry.put("SampleDate", date);

            }catch(JSONException e){
                e.printStackTrace();
            }catch(ParseException pe){
                pe.printStackTrace();
            }

            /** ADD JSON OBJECT INTO A RESULTS ARRAY **/
            resultsArray.put(entry);

            /** MOVE TO NEXT ROW **/
            cursor.moveToNext();

        }
        SQLiteStatement update = read.compileStatement("UPDATE DATA SET sent='yes' WHERE sent='no'");
        update.executeUpdateDelete();

        cursor.close();
        return resultsArray;

    }

    /**
     * @fn getAllData
     * @brief method to return all data in the table
     */
    public JSONArray getAllData(){

        JSONObject entry = new JSONObject();
        String deviceID;
        float rssi;
        String xbeeID;
        float latitude;
        float longitude;
        float yaw;
        float pitch;
        float roll;
        String timestamp;
        Date date;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JSONArray resultsArray = new JSONArray();

        /** PREPARE TO READ AND WRITE DATA **/
        SQLiteDatabase read = this.getReadableDatabase();
        Cursor cursor = read.rawQuery("SELECT * FROM Data", null);
        cursor.moveToFirst();

        /** ITERATE THROUGH UNSENT DATA **/
        while(!cursor.isAfterLast()){

            /** PULL DATA FROM EACH COLUMN OF THE TABLE **/
            deviceID   = cursor.getString(cursor.getColumnIndex(COLUMN_DEVICE_ID));
            rssi 	   = cursor.getFloat(cursor.getColumnIndex(COLUMN_RSSI));
            xbeeID     = cursor.getString(cursor.getColumnIndex(COLUMN_XBEE_ID));
            latitude   = cursor.getFloat(cursor.getColumnIndex(COLUMN_LAT));
            longitude  = cursor.getFloat(cursor.getColumnIndex(COLUMN_LONG));
            yaw        = cursor.getFloat(cursor.getColumnIndex(COLUMN_YAW));
            pitch      = cursor.getFloat(cursor.getColumnIndex(COLUMN_PITCH));
            roll       = cursor.getFloat(cursor.getColumnIndex(COLUMN_ROLL));
            timestamp  = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));

            /** PUT DATA INTO JSON OBJECT **/
            try {
                entry.put("DeviceID", deviceID);
                entry.put("RSSI", rssi);
                entry.put("XbeeID", xbeeID);
                entry.put("Latitude", latitude);
                entry.put("Longitude", longitude);
                entry.put("Yaw", yaw);
                entry.put("Pitch", pitch);
                entry.put("Roll", roll);

                date = format.parse(timestamp); //convert to date
                entry.put("SampleDate", date);

            }catch(JSONException e){
                e.printStackTrace();
            }catch(ParseException pe){
                pe.printStackTrace();
            }

            /** ADD JSON OBJECT INTO A RESULTS ARRAY **/
            resultsArray.put(entry);

            /** MOVE TO NEXT ROW **/
            cursor.moveToNext();

        }
        SQLiteStatement update = read.compileStatement("UPDATE DATA SET sent='yes' WHERE sent='no'");
        update.executeUpdateDelete();

        cursor.close();
        return resultsArray;

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

/* Make sure to use close() method when finished to prevent memory leaks */

