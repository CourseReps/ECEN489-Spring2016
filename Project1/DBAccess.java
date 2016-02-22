package io.github.samshore.android1;

/**
 * Created by Sam on 2/20/2016.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class DBAccess extends SQLiteOpenHelper{

    public static final String DB_NAME 			  = "DATA.db";
    public static final String COLUMN_TRANSMIT_ID = "transmitID";
    public static final String COLUMN_RSSI		  = "rssi";
    public static final String COLUMN_RECEIVE_ID  = "receiveID";
    public static final String COLUMN_GPS 		  = "gps";
    public static final String COLUMN_IMU 		  = "imu";
    public static final String COLUMN_TIMESTAMP	  = "timestamp";

    /** creating a database and connecting **/
    DBAccess(Context context){

        super(context, DB_NAME, null, 1);

    }


    /** create a table in the database with columns for Transmit ID, RSSI, Receive ID, GPS, IMU, Timestamp, and a sent field **/
    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("create table DATA " + "(transmitID text, rssi text, receiveID text, gps text, imu text, timestamp text PRIMARY KEY, sent text)");

    }


    /** method for upgrading to a new database (this is not important, it was just necessary for SQLiteOpenHelper) **/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);

    }


    /** method for adding data to the database**/
    // Convert JSON object to a string before passing to the method
    public boolean addData(String data){

        try {
            /** PARSE JSON STRING **/
            JSONObject obj = new JSONObject(data);
            String transmitID = obj.getString("Transmit ID");
            String rssi = obj.getString("RSSI");
            String receiveID = obj.getString("Receive ID");
            String gps = obj.getString("GPS");
            String imu = obj.getString("IMU");
            String timestamp = obj.getString("Timestamp");
            String sent = "no";


            /** INSERT INTO DB **/
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("transmitID", transmitID);
            contentValues.put("rssi", rssi);
            contentValues.put("receiveID", receiveID);
            contentValues.put("gps", gps);
            contentValues.put("imu", imu);
            contentValues.put("timestamp", timestamp);
            contentValues.put("sent", sent);
            db.insert("DATA", null, contentValues);

    }catch(JSONException e){
        e.printStackTrace();
    }
        return true;
    }


    /** method to return data that has not been sent to the database **/
    public List<String> getUnsentData(){

        JSONObject entry = new JSONObject();
        String transmitID;
        String rssi;
        String receiveID;
        String gps;
        String imu;
        String timestamp;

        /** PREPARE TO READ AND WRITE DATA **/
        SQLiteDatabase read = this.getReadableDatabase();
        SQLiteDatabase write = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = read.rawQuery( "SELECT * FROM Data WHERE sent='no'", null);
        cursor.moveToFirst();

        /** INSTANTIATE LIST **/
        SQLiteStatement statement = read.compileStatement("SELECT SUM(LENGTH(timestamp)) FROM DATA");
        long rowsLong = statement.simpleQueryForLong();
        int rows = (int) rowsLong;
        List<String> list;
        list = new ArrayList<String>(rows);


        /** ITERATE THROUGH UNSENT DATA **/
        while(!cursor.isAfterLast()){

            /** PULL DATA FROM EACH COLUMN OF THE TABLE **/
            transmitID = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSMIT_ID));
            rssi 	   = cursor.getString(cursor.getColumnIndex(COLUMN_RSSI));
            receiveID  = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVE_ID));
            gps 	   = cursor.getString(cursor.getColumnIndex(COLUMN_GPS));
            imu 	   = cursor.getString(cursor.getColumnIndex(COLUMN_IMU));
            timestamp  = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));

            /** PUT DATA INTO JSON OBJECT **/
            try {
                entry.put("Transmit ID", transmitID);
                entry.put("RSSI", rssi);
                entry.put("Receive ID", receiveID);
                entry.put("GPS", gps);
                entry.put("IMU", imu);
                entry.put("Timestamp", timestamp);
            }catch(JSONException e){
                e.printStackTrace();
            }

            /** CONVERT JSON OBJECT INTO STRING AND ADD TO LIST **/
            list.add(entry.toString());

            /** UPDATE SENT COLUMN **//*
            contentValues.put("sent", "yes");
            write.insert("DATA", null, contentValues);*/

            /** MOVE TO NEXT ROW **/
            cursor.moveToNext();

        }
        SQLiteStatement update = read.compileStatement("UPDATE DATA SET sent='yes' WHERE sent='no'");
        update.executeUpdateDelete();

        cursor.close();
        return list;

    }


    /** method to return all data in the table **/
    public List<String> getAllData(){

        JSONObject entry = new JSONObject();
        String transmitID;
        String rssi;
        String receiveID;
        String gps;
        String imu;
        String timestamp;

        /** PREPARE TO READ AND WRITE DATA **/
        SQLiteDatabase read = this.getReadableDatabase();
        SQLiteDatabase write = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = read.rawQuery( "SELECT * FROM Data", null);
        cursor.moveToFirst();

        /** INITIALIZE LIST **/
        SQLiteStatement statement = read.compileStatement("SELECT SUM(LENGTH(timestamp)) FROM DATA");
        long rowsLong = statement.simpleQueryForLong();
        int rows = (int) rowsLong;
        List<String> list;
        list = new ArrayList<String>(rows);

        /** ITERATE THROUGH UNSENT DATA **/
        while(!cursor.isAfterLast()){

            /** PULL DATA FROM EACH COLUMN OF THE TABLE **/
            transmitID = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSMIT_ID));
            rssi 	   = cursor.getString(cursor.getColumnIndex(COLUMN_RSSI));
            receiveID  = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVE_ID));
            gps 	   = cursor.getString(cursor.getColumnIndex(COLUMN_GPS));
            imu 	   = cursor.getString(cursor.getColumnIndex(COLUMN_IMU));
            timestamp  = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));

            try {
                /** PUT DATA INTO JSON OBJECT **/
                entry.put("Transmit ID", transmitID);
                entry.put("RSSI", rssi);
                entry.put("Receive ID", receiveID);
                entry.put("GPS", gps);
                entry.put("IMU", imu);
                entry.put("Timestamp", timestamp);
            }catch(JSONException e){
                e.printStackTrace();
            }

            /** CONVERT JSON OBJECT INTO STRING AND ADD TO LIST **/
            list.add(entry.toString());

            /** MOVE TO NEXT ROW **/
            cursor.moveToNext();

        }
        cursor.close();
        return list;

    }

    
    /** method to clear all data from the table **/
    public void clearData(){

        /** CLEAR ALL DATA IN TABLE **/
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("DATA", null, null);
    }
}

    /** WHEN DONE WITH THE DATABASE CONNECTION, USE close() METHOD TO CLOSE CONNECTION AND PREVENT MEMORY LEAKS **/


