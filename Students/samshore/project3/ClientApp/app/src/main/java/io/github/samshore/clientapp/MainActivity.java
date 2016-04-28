package io.github.samshore.clientapp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static float yaw;
    public static float pitch;
    public static float roll;
    public static boolean running;
    public static String targetAddress;
    public static boolean paused;
    public static int length;

    List<Float> pitchData = new ArrayList<Float>(Collections.nCopies(100,0f)); // initialize some data for initial filtering
    List<Float> rollData = new ArrayList<Float>(Collections.nCopies(100,0f));
    List<Float> f = new ArrayList<Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText)findViewById(R.id.IPText);
        EditText filterLength = (EditText)findViewById(R.id.filterLength);
        final TextView text = (TextView) findViewById(R.id.textView);

        try {
            editText.setCursorVisible(false);
            filterLength.setCursorVisible(false);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        // Initializing the sensor manager
        SensorManager senSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        Sensor senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Create thread to update text view
        Thread textThread = new Thread(){

            @Override
            public void run(){
                try{
                    while(true) {
                        while (!paused) {
                            Thread.sleep(100);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (running) {
                                        text.setText("sending data");
                                    } else {
                                        text.setText("disconnected");
                                    }
                                }

                            });
                        }
                    }
                }catch(NullPointerException|InterruptedException e){
                    e.printStackTrace();
                }
            }
        };

        textThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        paused = true;
    }

    @Override
    public void onResume(){
        super.onResume();
        paused = false;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
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
        running = true;
        final EditText ipText = (EditText) findViewById(R.id.IPText);
        final EditText filterText = (EditText) findViewById(R.id.filterLength);

        try {
            length = Integer.parseInt(filterText.getText().toString());
        }catch(NullPointerException|NumberFormatException e){
            length = 1;
        }

        /* consider out of bounds inputs */
        if(!(length > 0)){
            length = 1;
        }
        if(length > 100){   // arbitrary, set by initialized data list length
            length = 100;
        }

        f = makeFilter(length);

        Thread thread = new Thread() {

            @Override
            public void run() {
                try{
                    /* get a datagram socket */
                    DatagramSocket socket = new DatagramSocket();

                    /* get target ip address */
                    targetAddress = ipText.getText().toString();

                    while(running) {
                        /* collect data and filter */
                        pitchData.add(0, pitch);
                        rollData.add(0, roll);

                        /* filter data and send */
                        byte[] buf = createByteMessage(Math.round(filter(f, pitchData)), Math.round(filter(f, rollData)));    // filter data and store in byte array
                        InetAddress address = InetAddress.getByName(targetAddress);  // set destination IP
                        address = InetAddress.getByName(address.getHostAddress());
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8080); // create the packet
                        socket.send(packet);    // send the packet

                        /* clear data lists when they get large */
                        if(pitchData.size()>1200){
                            pitchData.subList(length, pitchData.size()).clear(); // clear data, but keep enough to continue filtering
                            Log.e("tag", ":)");
                            rollData.subList(length, rollData.size()).clear();
                        }

                        sleep(50);  // wait
                    }

                }catch(IOException|InterruptedException|NullPointerException e){
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }

    public static float filter(List<Float> f, List<Float> data){
        float sum = 0;
        for(int i=0; i<f.size(); i++){
            sum = sum + f.get(i)*data.get(i);   // dot product of data and filter
        }
        return sum; // filtered data point
    }

    public static List<Float> makeFilter(int N){
        if(N<1){
            N=1;
        }

        /* calculate denominator of filter coefficients */
        float denominator = 0f;
        for(int i = N; i>0; i--){
            denominator = denominator + i;
        }

        /* create filter */
        List<Float> f = new ArrayList<Float>();
        for(int i = N; i>0; i--){
            f.add((N-i),i/denominator);
        }
        return f;
    }

    public void disconnect(View view){
        running = false;
    }

    public static List<Integer> decodeByteMessage(byte[] b) {
        List<Integer> list = new ArrayList<Integer>();

        int i0 = b[0] & 0xFF;
        int i1 = b[1] & 0xFF;

        list.add(i0);
        list.add(i1);

        return list;
    }

    public static byte[] createByteMessage(int a, int b) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF),
                (byte) ((b >> 24) & 0xFF),
                (byte) ((b >> 16) & 0xFF),
                (byte) ((b >> 8) & 0xFF),
                (byte) (b & 0xFF)
        };
    }


}

