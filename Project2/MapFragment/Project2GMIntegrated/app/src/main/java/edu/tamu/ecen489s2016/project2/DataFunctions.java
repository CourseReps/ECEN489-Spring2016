/**
 * @file DataFunctions.java
 *
 * @brief Used to pull data from sensors and push to local SQLite Database
 *
 **/
package edu.tamu.ecen489s2016.project2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by keatonbrown on 2/22/16.
 */

/**
 * @class DataFunctions
 *
 * @brief contains simple pull and push functions
 */
public class DataFunctions{
    private String transmitID = "5";
    private double RSSI = 0;
    private String receiveID = "6";
    private double latitude = 0;
    private double longitude = 0;
    private DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String rssist = "";
    private String gpsst = "";
    private String imust = "";
    private String timestampst = "";
    private float yaw = 0;
    private float pitch = 0;
    private float roll = 0;
    private Context mContext;

    /**
     * @fn DataFunctions
     * @brief gets context but currently does nothing wiht it
     */
    public DataFunctions(Context mContext){
        this.mContext = mContext;
    }

    /**
     * @fn pulldata
     * @brief puts recived data into an arraylist
     */
    public ArrayList<String> pulldata(String transmitIDs, double RSSIs, String receiveIDs, float x, float y, float z, double lat, double longi){
        ArrayList<String> data = new ArrayList<>();
        transmitID = transmitIDs;
        RSSI = RSSIs;
        receiveID = receiveIDs;
        yaw = x;
        pitch = y;
        roll = z;
        latitude = lat;
        longitude = longi;
        rssist = Double.toString(RSSI);
        timestamp.format(Calendar.getInstance().getTime());
        imust = Float.toString(yaw) + " " + Float.toString(pitch) + " " + Float.toString(roll);
        timestampst = timestamp.format(Calendar.getInstance().getTime());
        gpsst = Double.toString(latitude) + ',' + Double.toString(latitude);
        data.add(transmitID);
        data.add(rssist);
        data.add(receiveID);
        data.add(imust);
        data.add(gpsst);
        data.add(timestampst);
        return data;
    }

    /**
     * @fn pushtodb
     * @brief puts received data into JSON and putds JSON into local SQLite db
     */
    public void pushtodb(DBAccess data2){
        try {
            pulldata(transmitID, RSSI, receiveID, yaw, pitch, roll, latitude, longitude);
            JSONObject dbdata = new JSONObject();
            dbdata.put("XbeeID", transmitID);
            dbdata.put("RSSI", RSSI);
            dbdata.put("DeviceID", receiveID);
            dbdata.put("Latitude", latitude);
            dbdata.put("Longitude", longitude);
            dbdata.put("Yaw", yaw);
            dbdata.put("Pitch", pitch);
            dbdata.put("Roll", roll);
            dbdata.put("SampleDate", timestampst);
            data2.addData(dbdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}