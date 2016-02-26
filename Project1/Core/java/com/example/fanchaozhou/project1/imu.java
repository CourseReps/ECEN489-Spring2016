package com.example.fanchaozhou.project1;

/**
 * Created by keatonbrown on 2/26/16.
 */

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import android.hardware.Sensor;
import android.hardware.SensorManager;


// This public class prints out the x axis, y axis and z axis fo the IMU.

public class imu extends Activity{

    private Context mContext;
    SensorManager sensorManager;

    public imu(Context mContext){
        this.mContext = mContext;
        sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
    }

    public float[] getorient(float[] R, float[] values){
        sensorManager.getOrientation(R, values);

        return values;
    }
}