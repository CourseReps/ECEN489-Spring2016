package com.example.fanchaozhou.project1;

/**
 * Created by keatonbrown on 2/26/16.
 */

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;

import java.security.SecureRandom;

// This public class prints out the x axis, y axis and z axis fo the IMU.

public class imu{

    private Context mContext;
    SensorManager sensorManager;

    public imu(Context mContext){
        /*
        this.mContext = mContext;
        sensorManager = (SensorManager) mContext.getSystemService(Activity.SENSOR_SERVICE);
        */
    }

    public float[] getorient(float[] R, float[] values){
        //sensorManager.getOrientation(R, values);

        values[ 0 ] = new SecureRandom().nextFloat();
        values[ 1 ] = new SecureRandom().nextFloat();
        values[ 2 ] = new SecureRandom().nextFloat();

        return values;
    }
}