/**
 * @file DataFunctions.java
 *
 * @brief Used to pull data from sensors and push to local SQLite Database
 *
 **/
package com.example.fanchaozhou.project1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
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
    private DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context mContext;
    private DataCollector dataStruct;

    /**
     * @fn DataFunctions
     * @brief gets context but currently does nothing wiht it
     */
    public DataFunctions(Context mContext){
        this.mContext = mContext;
        dataStruct = new DataCollector();
    }

    /**
     * @fn pulldata
     * @brief puts recived data into an arraylist
     */
    public ArrayList<String> pulldata(){
        ArrayList<String> data = new ArrayList<>();
        String rssist = Double.toString(dataStruct.RSSI);
        timestamp.format(Calendar.getInstance().getTime());
        String imust = Float.toString(dataStruct.yaw) + " " + Float.toString(dataStruct.pitch) + " " + Float.toString(dataStruct.roll);
        //dataStruct.timestamp = Calendar.getInstance().getTime(); //update timestamp
        String timestampst = timestamp.format(dataStruct.timestamp);
        String gpsst = Double.toString(dataStruct.latitude) + ',' + Double.toString(dataStruct.latitude);
        data.add(dataStruct.transmitID);
        data.add(rssist);
        data.add(dataStruct.receiveID);
        data.add(imust);
        data.add(gpsst);
        data.add(timestampst);
        data.add("" + dataStruct.magField[ 0 ] + " " + dataStruct.magField[ 1 ] + " " + dataStruct.magField[ 2 ]);
        data.add(""+dataStruct.wifiRSSI);

        return data;
    }

    /**
     * @fn pushtodb
     * @brief puts most recent received data into JSON and putds JSON into local SQLite db
     */
    public void pushtodb(DBAccess data2){
        try {
            //pulldata();//transmitID, RSSI, receiveID, yaw, pitch, roll, latitude, longitude);
            JSONObject dbdata = new JSONObject();
            dbdata.put("XbeeID", dataStruct.transmitID);
            dbdata.put("RSSI", dataStruct.RSSI);
            dbdata.put("DeviceID", dataStruct.receiveID);
            dbdata.put("Latitude", dataStruct.latitude);
            dbdata.put("Longitude", dataStruct.longitude);
            dbdata.put("Yaw", dataStruct.yaw);
            dbdata.put("Pitch", dataStruct.pitch);
            dbdata.put("Roll", dataStruct.roll);
            dbdata.put("SampleDate", timestamp.format(dataStruct.timestamp));
            data2.addData(dbdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}