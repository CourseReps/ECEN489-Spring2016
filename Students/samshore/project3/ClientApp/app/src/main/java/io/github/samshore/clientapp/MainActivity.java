package io.github.samshore.clientapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public float yaw;
    public float pitch;
    public float roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ORIENTATION){
            yaw = event.values[0];
            pitch = event.values[1];
            roll = event.values[2];
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void connect(View view){
        String serverName = "computer_server";
        final int port = 8080;
        final TextView text = (TextView) view.findViewById(R.id.textView);

        Thread thread = new Thread() {

            @Override
            public void run() {
                try{
                    text.setText("connecting to server...");
                    Socket client = new Socket("10.202.117.7", port);
                    text.setText("connected");
                    OutputStream outputStream = client.getOutputStream();
                    DataOutputStream out = new DataOutputStream((outputStream));
                    out.writeUTF("hello :^)");
                    InputStream inputStream = client.getInputStream();
                    DataInputStream in = new DataInputStream(inputStream);
                    text.setText("server says: " + in.readUTF());
                    client.close();
                }catch(IOException e){
                    text.setText("cannot connect to server");
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }


}

