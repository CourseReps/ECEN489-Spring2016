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
    private String transmitID = "x";
    private float rssi = 0;
    private String receiveID = "z";
    private double latitude = 0;
    private double longitude = 0;
    private ArrayList imu = new ArrayList();
    private DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String rssist = "";
    private String gpsst = "";
    private String imust = "";
    private String timestampst = "";
    private float yaw = 0;
    private float pitch = 0;
    private float roll = 0;
    Context mContext;

    public DataFunctions(Context mContext){
        this.mContext = mContext;
    }

    private void location() {
        /*
        LocationManager locationManager;
        locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null){
            gpsst = "(" + location.getLatitude() + "," + location.getLongitude() + ")";
        } else {
            gpsst = "Unknown Position";
        }*/

        gpsst = "Unknown Position";
    }


    public ArrayList<String> pulldata(String transmitID, float RSSI, String receiveID, Float[] imu){
        location();
        /*
        float[] orient = new float[3];
        float[] rotation = new float[9];
        imu myimu = new imu(mContext);
        orient = myimu.getorient(rotation,orient);
        ArrayList<String> data = new ArrayList<>();
        */
        yaw = imu[0];
        pitch = imu[1];
        roll = imu[2];

        rssist = float.toString(RSSI);
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
            pulldata();
            JSONObject dbdata = new JSONObject();
            dbdata.put("Xbee ID", transmitID);
            dbdata.put("RSSI", RSSI);
            dbdata.put("Device ID", receiveID);
            dbdata.put("Latitude", latitude);
            dbdata.put("Longitude", longitude);
            dbdata.put("yaw");
            dbdata.put("pitch");
            dbdata.put("roll");
            dbdata.put("Date", timestamp);
            data2.addData(dbdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}