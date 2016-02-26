package com.example.fanchaozhou.project1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
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
        LocationManager locationManager;
        locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        /*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000, 1, (LocationListener) this);
                */
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        gpsst = "(" + location.getLatitude() + "," + location.getLongitude() + ")";
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }


    public ArrayList<String> pulldata(){
        location();
        float[] orient = new float[3];
        float[] rotation = new float[9];
        imu myimu = new imu(mContext);
        orient = myimu.getorient(rotation,orient);
        ArrayList<String> data = new ArrayList<>();
        yaw = orient[0];
        pitch = orient[1];
        roll = orient[2];
        timestamp.format(Calendar.getInstance().getTime());
        transmitID = "x";
        rssist = "y";
        receiveID = "z";
        imust = Float.toString(orient[0]) + Float.toString(orient[1]) + Float.toString(orient[2]);
        timestampst = timestamp.format(Calendar.getInstance().getTime());
        data.add(transmitID);
        data.add(rssist);
        data.add(receiveID);
        data.add(imust);
        data.add(gpsst);
        data.add(timestampst);
        return data;
    }

    public void pushtodb(){
        try {
            pulldata();
            JSONObject dbdata = new JSONObject();
            dbdata.put("Xbee ID", transmitID);
            dbdata.put("RSSI", rssi);
            dbdata.put("Device ID", receiveID);
            dbdata.put("Latitude", latitude);
            dbdata.put("Longitude", longitude);
            dbdata.put("IMU", imu);
            dbdata.put("Date", timestamp);
            DBAccess data2 = new DBAccess(mContext);
            data2.addData(dbdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}