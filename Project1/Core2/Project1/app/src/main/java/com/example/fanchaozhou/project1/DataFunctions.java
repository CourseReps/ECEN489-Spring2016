package com.example.fanchaozhou.project1;

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
public class DataFunctions{
    private static String transmitID = "";
    private static float RSSI = 0;
    private static String receiveID = "";
    private static double latitude = 0;
    private static double longitude = 0;
    private static DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static float[] imu = new float[3];
    private static String rssist = "";
    private static String gpsst = "";
    private static String imust = "";
    private static String timestampst = "";
    private static float yaw = 0;
    private static float pitch = 0;
    private static float roll = 0;
    private static float[] orient = new float[3];
    Context mContext;

    public DataFunctions(Context mContext){
        this.mContext = mContext;
    }

    private void location() {
        LocationManager locationManager;
        locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null){
            gpsst = "(" + location.getLatitude() + "," + location.getLongitude() + ")";
        } else {
            gpsst = "Unknown Position";
        }
    }


    public ArrayList<String> pulldata(String transmitIDs, float RSSIs, String receiveIDs, float[] imus){
        location();
        /*
        float[] orient = new float[3];
        float[] rotation = new float[9];
        imu myimu = new imu(mContext);
        orient = myimu.getorient(rotation,orient);
        */
        ArrayList<String> data = new ArrayList<>();
        transmitID = transmitIDs;
        RSSI = RSSIs;
        receiveID = receiveIDs;
        imu = imus;
        yaw = imu[0];
        pitch = imu[1];
        roll = imu[2];
        orient = imu;

        rssist = Float.toString(RSSI);
        timestamp.format(Calendar.getInstance().getTime());
        imust = Float.toString(imu[0]) + " " + Float.toString(imu[1]) + " " + Float.toString(imu[2]);
        timestampst = timestamp.format(Calendar.getInstance().getTime());
        data.add(transmitID);
        data.add(rssist);
        data.add(receiveID);
        data.add(imust);
        data.add(gpsst);
        data.add(timestampst);
        return data;
    }

    public void pushtodb(DBAccess data2){
        try {
            pulldata(transmitID, RSSI, receiveID, orient);
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