/**
 * @file MainFragment.java
 *
 * @brief This file contains the brains behind the Data Display Fragment
 *
 **/

package com.example.fanchaozhou.project1;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanchao Zhou on 2/22/2016.
 */

/**
 * @class MainFragment
 *
 * @brief contains the functionality for the buttons, datadisplay listview, and the alignment fragment
 *
 * A more detailed class description
 */
public class MainFragment extends Fragment implements SensorEventListener, LocationListener {

    private ArrayList<String> dataList;
    private ArrayAdapter<String> dataListAdaptor;
    private DBAccess dbHandle;
    private UsbSerialPort port;
    private int HTTP_SEND_STATUS = 0;
    public final static int BUFSIZE = 128;
    private final static String RXID = "Receive ID";
    private final static String TXID = "Transmit ID";
    private final static String RSSI = "RSSI";
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private LocationManager locationManager;

    private MyGLSurfaceView mGLView;
    private Square square = new Square();
    private TextView yawText;
    private TextView pitchText;
    private TextView rollText;

    private DataCollector dataStruct;
    private SharedPreferences sharedPref;

    public MainFragment(){
        dataList = new ArrayList<>();
    }

    /**
     * @fn onCreate
     * @brief sets up the display
     *
     * Sets up the connection to the teensy and sensor manager. also loads in alignment fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Adding the AlignmentFragment
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        AlignmentFragment Al = new AlignmentFragment();

        FT.add(R.id.align_fragment, Al);
        FT.commit();

        if(savedInstanceState==null){
            //Initializing the data list adaptor
            dataListAdaptor = new ArrayAdapter<String>(
                    getActivity(),                    //The Current Parent Activity
                    R.layout.single_record,           //The .xml file that contains the textview
                    R.id.list_item_record,            //The id of the textview
                    dataList                          //The source of the data
            );

            dbHandle = new DBAccess(this.getActivity());

            //Initializing the sensor manager
            senSensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
            senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

            locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }

            // Find all available drivers from attached devices.
            UsbManager manager = (UsbManager)getActivity().getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
            if (availableDrivers.isEmpty()) {
                return;
            }

            // Open a connection to the first available driver.
            UsbSerialDriver driver = availableDrivers.get(0);
            UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
            List<UsbSerialPort> portList = driver.getPorts();
            port = portList.get(0);
            try{
                port.open(connection);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                int BaudRate = sharedPref.getInt(getString(R.string.pref_serial_baudrate_key),
                        Integer.parseInt(getString(R.string.pref_http_default)));  //Get the Baud Rate
                port.setParameters(BaudRate, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        dataStruct = new DataCollector(); //data access/storage wrapper

        // Creating a Shared Preference Manager
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

    }

    /**
     * @fn onStart
     * @brief Populate the list with currently collected data when switching back to the main fragment
     */
    @Override
    public void onStart() {
        super.onStart();
        dataListAdaptor.notifyDataSetChanged();
    }

    /**
     * @fn onDestroy
     * @brief closes serial port
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            port.close();        //Release the serial port
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * @fn onSensorChanged
     * @brief gets the new imu values
     */

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        /* set up constants for orientation graphic */
        String pitchTolString;
        String rollTolString;
        float red[] = {0.85f, 0.0f, 0.0f, 1.0f};
        float green[] = {0.57843137f, 0.83921569f, 0.0f, 1.0f};
        float pitchTol;
        float rollTol;
        float PITCH_MIN;
        float PITCH_MAX;
        float ROLL_MAX; // Roll is symmetric about zero, so no need for min field if using absolute value

        if (mySensor.getType() == Sensor.TYPE_ORIENTATION) {
            dataStruct.yaw = event.values[0];
            dataStruct.pitch = event.values[1];
            dataStruct.roll = event.values[2];
        }

        /* Display yaw pitch and roll in a text view */
        yawText.setText("z: " + String.valueOf((int)dataStruct.yaw));
        pitchText.setText("y: " + String.valueOf((int)dataStruct.pitch));
        rollText.setText("x: " + String.valueOf((int)dataStruct.roll));

        /* Get orientation tolerances from preferences */
        pitchTolString = sharedPref.getString(getString(R.string.pref_tolerance_theta_key), "10");
        rollTolString = sharedPref.getString(getString(R.string.pref_tolerance_phi_key), "5");

        /* Handle inputs that are not parsable floats */
        try {
            pitchTol = Float.parseFloat(pitchTolString);
            rollTol = Float.parseFloat(rollTolString);
        }catch(NumberFormatException e){
            e.printStackTrace();
            pitchTol = 10.0f;
            rollTol = 5.0f;
        }

        /* Make sure the tolerances are acceptable values */
        if(!((0<=pitchTol)&&(pitchTol<=90))){
            pitchTol = 10.0f;
        }
        if(!((0<=rollTol)&&(rollTol<=360))){
            rollTol = 5.0f;
        }

        /* Set acceptable orientation boundaries */
        PITCH_MIN = 90 - pitchTol;
        PITCH_MAX = 90 + pitchTol;
        ROLL_MAX = rollTol; // Roll is symmetric about zero, so no need for min field if using absolute value

        /* Set orientation graphic color based on current orientation and set boundaries */
        if((PITCH_MIN<(Math.abs(dataStruct.pitch)))&&((Math.abs(dataStruct.pitch))<PITCH_MAX)&&((Math.abs(dataStruct.roll))<ROLL_MAX)){
            square.setColor(green);
        }
        else{
            square.setColor(red);
        }

    }


    /**
     * @fn onAccuracyChanged
     * @brief not implemented
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * @fn onLocationChanged
     * @brief gets new lat and long
     */
    @Override
    public void onLocationChanged(Location location) {
        dataStruct.latitude = location.getLatitude();
        dataStruct.longitude = location.getLongitude();

    }

    /**
     * @fn onStatusChanged
     * @brief not implemented
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * @fn onProviderEnabled
     * @brief not implemented
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * @fn onProviderDisabled
     * @brief not implemented
     */
    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * @class PullData
     *
     * @brief Reading RSSI, TX id, RX id from the serial port
     */
    private class PullData extends AsyncTask<Void, Void, ArrayList<String>>{
        /**
         * @fn doInBackground
         * @brief reads from serial port; calls dataFunc.pulldata from DataFunctiond
         */
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            String serialJSONData;
            //@TODO don't hardcode these
            dataStruct.transmitID = "5";
            dataStruct.receiveID = "6";
            DataFunctions dataFunc = new DataFunctions(getActivity());
            byte buffer[] = new byte[ BUFSIZE ];

            try {
                if(port != null){
                    port.read(buffer, BUFSIZE);
                    serialJSONData = new String(buffer, "UTF-8");
                    try{
                        JSONObject serialJSONObj = new JSONObject(serialJSONData);
                        dataStruct.receiveID = serialJSONObj.getString(RXID);
                        dataStruct.transmitID = serialJSONObj.getString(TXID);
                        dataStruct.RSSI = serialJSONObj.getDouble(RSSI);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            } catch
                    (IOException e) {
                System.out.println(e);
            }

            ArrayList<String> data = dataFunc.pulldata();
            dataFunc.pushtodb(dbHandle);

            return data;
        }

        /**
         * @fn onPostExecute
         * @brief adds data to listview
         */
        @Override
        protected void onPostExecute(ArrayList<String> data) {
            synchronized (dataList){
                dataList.add(0,  //Add the new data to the top of the list
                        "Transmitter ID: " + data.get(0) + "\n" +
                                "Receiver ID: " + data.get(2) + "\n" +
                                "TimeStamp: " + data.get(5) + "\n" +
                                "RSSI: " + -Double.parseDouble(data.get(1)) + " dBm\n" +
                                "Orientation: " + data.get(3) + "\n" +
                                "Location: " + data.get(4)
                );
            }

            dataListAdaptor.notifyDataSetChanged();  //Refresh the list display
        }
    }

    /**
     * @fn onCreateView
     * @brief adds button click listensers, creats http connection, sends data to server
     *
     * A more detailed function description (Only use detailed description if the function needs explanation otherwise your brief description * should suffice)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState == null){
            //Create a GL Surfaceview
            mGLView = (MyGLSurfaceView) rootView.findViewById(R.id.glSurfaceViewID);
            yawText = (TextView)rootView.findViewById(R.id.yawText);
            pitchText = (TextView)rootView.findViewById(R.id.pitchText);
            rollText = (TextView)rootView.findViewById(R.id.rollText);

            ListView list = (ListView)rootView.findViewById(R.id.list);  //Find the id of the target ListView
            list.setAdapter(dataListAdaptor);                            //Bind the adaptor to the ListView

            final Button button_clear_all = (Button)rootView.findViewById(R.id.button_clear_all);
            button_clear_all.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {  //Button handler for clearing the data list
                    dataList.clear();
                    dataListAdaptor.notifyDataSetChanged();
                }
            });

            final Button button_refresh = (Button)rootView.findViewById(R.id.button_refresh);
            button_refresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {  //Button handler for pulling data
                    new PullData().execute();
                }
            });

            final Button button_datatx = (Button)rootView.findViewById(R.id.button_datatx);
            button_datatx.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {  //Button handler for triggering a data transmission
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    final String serverAddr =
                            sharedPref.getString(getString(R.string.pref_http_key),
                                    getString(R.string.pref_http_default));  //Get the server Address
                    //The server address is in the string "serverAddr". For debugging purposes, I set this address adjustable.

                    //code block from tbranyon
                    Thread t = new Thread() {
						public void run() {
                            JSONArray JSONlist = dbHandle.getUnsentData();
							for(int x = 0; x < JSONlist.length(); ++x)
							{
								HTTP_SEND_STATUS = 0;
                                try {
                                    sendHTTPdata((JSONObject)JSONlist.get(x), serverAddr);
                                    System.out.println(JSONlist.get(x));
                                }catch(Exception e)
                                {System.err.print(e);}
                                if(HTTP_SEND_STATUS == -1)
									System.err.println("Error sending!"); //change later to toast message on phone screen
							}
							System.out.println("Send thread finished");
						}
					}; //End of thread t
					t.start(); //start send thread
                }
            });
        }

        return rootView;
    }

    /**
     * @fn snedHTTPdata
     * @brief Method for sending JSON lines from local DB to server
     */

    protected void sendHTTPdata(JSONObject json, String serverAddress)
	{
		final String data = json.toString();
		try{
		    URL url = new URL(serverAddress);

		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setReadTimeout(1000);
		    conn.setConnectTimeout(1000);
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
		    conn.setRequestMethod("POST");
		    conn.connect();

		    OutputStream os = conn.getOutputStream();
		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
		    writer.write(data);
		    writer.close();
		    os.close();

		    int result = conn.getResponseCode();
		    HTTP_SEND_STATUS = 1;
		}catch(Exception e){
		    System.err.print(e);
		    HTTP_SEND_STATUS = -1;
		}
	}
}
