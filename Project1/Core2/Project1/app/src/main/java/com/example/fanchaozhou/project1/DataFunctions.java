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
    private String transmitID = "5";
    private double RSSI = 0;
    private String receiveID = "6";
    private double latitude = 0;
    private double longitude = 0;
    private DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private float[] imu = new float[3];
    private String rssist = "";
    private String gpsst = "";
    private String imust = "";
    private String timestampst = "";
    private float yaw = 0;
    private float pitch = 0;
    private float roll = 0;
    private float[] orient = new float[3];
    private Context mContext;

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


    public ArrayList<String> pulldata(String transmitIDs, double RSSIs, String receiveIDs, float x, float y, float z){
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
        yaw = x;
        pitch = y;
        roll = z;
        orient = imu;

        rssist = Double.toString(RSSI);
        timestamp.format(Calendar.getInstance().getTime());
        imust = Float.toString(yaw) + " " + Float.toString(pitch) + " " + Float.toString(roll);
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
            pulldata(transmitID, RSSI, receiveID, yaw, pitch, roll);
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