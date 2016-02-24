package com.example.fanchaozhou.project1;

import java.util.ArrayList;
import java.util.Random;

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
    private String rssi = "y";
    private String receiveID = "z";
    private String gps = "";
    private String imu = "";
    private String timestamp = "";
    Context mContext;
//    private DBAccess data2 = new DBAccess(mContext);
    public DataFunctions(Context mContext){
        this.mContext = mContext;
    }

    public static float[] getorientation(float[] r, float[] values) {
        values[ 0 ] = (new Random()).nextFloat();
        values[ 1 ] = (new Random()).nextFloat();
        values[ 2 ] = (new Random()).nextFloat();

        return values;
    }

    private void location() {
        LocationManager locationManager;
        locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null){
            gps = "(" + location.getLatitude() + "," + location.getLongitude() + ")";
        } else {
            gps = "Unknown Location";
        }
    }


    public ArrayList<String> pulldata(){
        location();
        float[] orient = new float[3];
        float[] rotation = new float[0];
        getorientation(rotation , orient);
        ArrayList<String> data = new ArrayList<>();
        long timestampint = System.currentTimeMillis() / 1000L;
        transmitID = "x";
        rssi = "y";
        receiveID = "z";
        imu = Float.toString(orient[0])+" "+Float.toString(orient[1])+" "+Float.toString(orient[2]);
        timestamp = Long.toString(timestampint);
        data.add(transmitID);
        data.add(rssi);
        data.add(receiveID);
        data.add(imu);
        data.add(gps);
        data.add(timestamp);
        return data;
    }

    public void pushtodb(){
        try {

            JSONObject dbdata = new JSONObject();
            dbdata.put("Transmit ID", transmitID);
            dbdata.put("RSSI", rssi);
            dbdata.put("Receive ID", receiveID);
            dbdata.put("GPS", gps);
            dbdata.put("IMU", imu);
            dbdata.put("Timestamp", timestamp);
//            data2.addData(dbdata.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}