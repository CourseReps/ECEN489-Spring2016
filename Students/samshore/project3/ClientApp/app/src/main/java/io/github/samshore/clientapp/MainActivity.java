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

/** MainActivity. This is the only source file for this app. This activity records rotation information and sends
 *    it to the target computer via a UDP socket.
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static float yaw;
    public static float pitch;
    public static float roll;
    public static boolean running;
    public static String targetAddress;
    public static boolean paused;
    public static int length;

    /** Data Lists. Used to store the current and previous rotation values for filtering
     */
    List<Float> pitchData = new ArrayList<Float>(Collections.nCopies(100,0f)); // initialize some data for initial filtering
    List<Float> rollData = new ArrayList<Float>(Collections.nCopies(100,0f));
    /** Filter List. Used for storing the filter
     */
    List<Float> f = new ArrayList<Float>();

    /** onCreate. Sets up the textView, editTexts, and sensor manager. Also starts a thread to update the 
     *    status textview.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText)findViewById(R.id.IPText);
        EditText filterLength = (EditText)findViewById(R.id.filterLength);
        final TextView text = (TextView) findViewById(R.id.textView);

        try { // turning off blinking cursor
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

    /** onPause. Sets a paused flag to true when the app is paused so that threads do 
     *    not keep running in the background.
     */
    @Override
    public void onPause(){
        super.onPause();
        paused = true;
    }

    /** onResume. Sets the paused flag to false so that the threads resume when the app
     *    is resumed.
     */
    @Override
    public void onResume(){
        super.onResume();
        paused = false;
    }

    /** onDestroy. Completely stops the app on close.
     */
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

    /** onSensorChanged. Reads orientation values from the orientation sensor
     */
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

    /** connect. Runs when connect button is pressed. It first sets a running flag to true so that the thread
     * runs until the disconnect button is pressed. It then pulls the target IP and filter length parameters from the
     * editText fields. Next, it creates the filter. Finally, it starts a thread to record rotation data and send it to 
     * the target computer.
     */
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

        /** connect thread. Creates a datagram socket in order to send data. It stores pitch and roll values in the data lists.
         * It then filters the data and stores the results in a byte array. A datagram packet is created with this data and is targeted
         * at the server's InetAdress. Finally, the packet is sent. This repeates every 50 ms.
         */
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

                        /* clear data lists when lists get large */
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

    /** filter method. This method dots the filter with the data list parameter and returns a float.
     */
    public static float filter(List<Float> f, List<Float> data){
        float sum = 0;
        for(int i=0; i<f.size(); i++){
            sum = sum + f.get(i)*data.get(i);   // dot product of data and filter
        }
        return sum; // filtered data point
    }

    /** makeFilter method. This method creates a linear filter for smoothing cursor movements. The weights
     * for each data point are created as follows for a filter length N: (d = N + (N-1) + (N-2) + ... 1) 
     *{N/d, (N-1)/d, (N-2)/d, ... , 1/d}. It returns a list of floats.
     */
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

    /** disconnect. When the disconnect button is pressed, the running flag is set to false and the app stops
     * collecting data and sending data to the server.
     */
    public void disconnect(View view){
        running = false;
    }

    /** createByteMessage. Accepts two ints (pitch and roll) and formats them into a byte array.
     */
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

