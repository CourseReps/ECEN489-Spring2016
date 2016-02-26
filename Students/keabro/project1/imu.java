package project1.keatonbrown.datafunctions;

/**
 * Created by keatonbrown on 2/26/16.
 */

import android.app.Activity;
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

public class imu extends Activity {
    SensorManager sm = null;
    List list;

    SensorEventListener sel = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
        }
    };

    //  Applying three intrinsic rotations in azimuth, pitch, and roll. Tranforms identity matrix to rotation matrix. Returns values in radians and a positive, CCW direction.

    public static float[] getOrientation(float[] R, float[] values) {
        return values;
    }
}