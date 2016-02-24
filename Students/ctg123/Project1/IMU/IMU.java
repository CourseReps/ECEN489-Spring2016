import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast
import java.util.List;
import android.hardware.Sensor;
import android.hardware.SensorManager;


// This public class prints out the x axis, y axis and z axis fo the IMU.

public class IMU extends activity
{
	SensorManager sm = null;
	TextView textview = null;
	List list;
	
	SensorEventListener sel = new SensorEventListener(){  
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}  
        public void onSensorChanged(SensorEvent event) {  
            float[] values = event.values;  
            textView1.setText("x: "+values[0]+"\ny: "+values[1]+"\nz: "+values[2]);  
        }  
    };  
	
  //  Applying three intrinsic rotations in azimuth, pitch, and roll. Tranforms identity matrix to rotation matrix. Returns values in radians and a positive, CCW direction.
  
	public static float[] getOrientation (float[] R, float[] values){
		return values;
	}
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
  
        /* Get a SensorManager instance */  
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);  
  
        textView1 = (TextView)findViewById(R.id.textView1);  
  
        list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);  
        if(list.size()>0){  
            sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);  
        }else{  
            Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();  
        }  
    }  
  
    @Override  
    protected void onStop() {  
        if(list.size()>0){  
          sm.unregisterListener(sel);  
        }  
        super.onStop();  
    }  
}  

