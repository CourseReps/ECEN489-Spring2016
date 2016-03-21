/**
 * @file Example.java
 *
 * @brief Contains everything needed to interact with the IMU sensor
 *
 **/

package com.example.fanchaozhou.project1;

/**
 * Created by keatonbrown on 2/26/16.
 */

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;

import java.security.SecureRandom;

//
/**
 * @class Example
 *
 * @brief This public class prints out the x axis, y axis and z axis fo the IMU.
 */
public class imu{

    private Context mContext;
    SensorManager sensorManager;

    /**
     * @fn imu
     * @brief Construcot to get context
     */
    public imu(Context mContext){
        /*
        this.mContext = mContext;
        sensorManager = (SensorManager) mContext.getSystemService(Activity.SENSOR_SERVICE);
        */
    }
    /**
     * @fn getorient
     * @brief gets the orientation
     */
    public float[] getorient(float[] R, float[] values){
        //sensorManager.getOrientation(R, values);

        values[ 0 ] = new SecureRandom().nextFloat();
        values[ 1 ] = new SecureRandom().nextFloat();
        values[ 2 ] = new SecureRandom().nextFloat();

        return values;
    }
}