/**
 * @file MainFragment.java
 *
 * @brief This file contains the brains behind the Data Display Fragment
 *
 **/

package com.example.fanchaozhou.project1;

//import android.Manifest;
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
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

//import org.json.JSONArray;
import org.json.JSONObject;

//import java.io.BufferedWriter;
import java.io.IOException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
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

    private final ArrayList<String> dataList;
    private ArrayAdapter<String> dataListAdaptor;
    private DBAccess dbHandle;
    private UsbSerialPort port;
    private int HTTP_SEND_STATUS = 0;
    public final static int BUFSIZE = 128;
    /*public final static boolean IS_AUTO_RUNNING_DEF = false;
    public final static boolean IS_USED_DEF = false;
    public final static boolean IS_ALIGNED_DEF = false;
    public final static String IS_AUTO_RUNNING_PREF_KEY = "Auto Running Preference";
    public final static String IS_USED_PREF_KEY = "Is_Used Preference";
    public final static String IS_ALIGNED_PREF_KEY = "Is_Aligned Preference";
    public static final String SETTINGS_FILE = "SETTINGS_ON_MAINFRAGMENT";
    private SharedPreferences.Editor editor = null;
    private SharedPreferences settings;*/
    private final static String RXID = "Receive ID";
    private final static String TXID = "Transmit ID";
    private final static String RSSI = "RSSI";
    private Sensor senMagnetometer;

    private MyGLSurfaceView mGLView; //@TODO: Never used!
    private Square square = new Square();
    private TextView yawText;
    private TextView pitchText;
    private TextView rollText;

    private DataCollector dataStruct;
    private SharedPreferences sharedPref;

    private boolean runEnable, loopIsRunning; //flags for data collection start/stop

    public MainFragment(){
        dataList = new ArrayList<>();
        dataStruct = new DataCollector(); //data access/storage wrapper

        runEnable = false;
        loopIsRunning = false;
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

        // Creating a Shared Preference Manager
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Adding the AlignmentFragment
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        AlignmentFragment Al = new AlignmentFragment();

        FT.add(R.id.align_fragment, Al);
        FT.commit();

        if(savedInstanceState==null){
            //Initializing the data list adaptor
            dataListAdaptor = new ArrayAdapter<>(
                    getActivity(),                    //The Current Parent Activity
                    R.layout.single_record,           //The .xml file that contains the textview
                    R.id.list_item_record,            //The id of the textview
                    dataList                          //The source of the data
            );

            dbHandle = new DBAccess(this.getActivity());

            //Initializing the sensor manager
            SensorManager senSensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
            Sensor senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            senSensorManager.registerListener(this, senMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
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
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * @fn onStart
     * @brief Populate the list with currently collected data when switching back to the main fragment
     */
    @Override
    public void onStart() {
        super.onStart();
        dataListAdaptor.notifyDataSetChanged(); //@TODO this line causes an exception on app close
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
            System.out.println(e.getMessage());
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

        //store new orientation values
        if (mySensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            DataCollector.yaw = event.values[0];
            DataCollector.pitch = event.values[1];
            DataCollector.roll = event.values[2];
        }

        if(mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            DataCollector.magField[0] = event.values[0];
            DataCollector.magField[1] = event.values[1];
            DataCollector.magField[2] = event.values[2];
        }

        /* Display yaw pitch and roll in a text view */
        yawText.setText(getString(R.string.yawsettext, String.valueOf((int)DataCollector.yaw)));
        pitchText.setText(getString(R.string.pitchsettext, String.valueOf((int) DataCollector.pitch)));
        rollText.setText(getString(R.string.rollsettext, String.valueOf((int) DataCollector.roll)));

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
        if((PITCH_MIN<(Math.abs(DataCollector.pitch)))&&((Math.abs(DataCollector.pitch))<PITCH_MAX)&&((Math.abs(DataCollector.roll))<ROLL_MAX)){
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
        DataCollector.latitude = location.getLatitude();
        DataCollector.longitude = location.getLongitude();

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
         * @brief reads from serial port; calls dataFunc.pulldata from DataFunctions
         */
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            String serialJSONData;
            DataCollector.transmitID = "5";
            DataCollector.receiveID = "6";
            DataFunctions dataFunc = new DataFunctions(getActivity());
            byte buffer[] = new byte[ BUFSIZE ];

            try {
                if(port != null){
                    port.read(buffer, BUFSIZE);
                    serialJSONData = new String(buffer, "UTF-8");
                    try{
                        JSONObject serialJSONObj = new JSONObject(serialJSONData);
                        DataCollector.receiveID = serialJSONObj.getString(RXID);
                        DataCollector.transmitID = serialJSONObj.getString(TXID);
                        DataCollector.RSSI = serialJSONObj.getDouble(RSSI);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch
                    (IOException e) {
                System.out.println(e.getMessage());
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
     * @brief adds button click listeners, creates http connection, sends data to server
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState == null) {

            //Create a GL Surfaceview
            mGLView = (MyGLSurfaceView) rootView.findViewById(R.id.glSurfaceViewID);
            yawText = (TextView) rootView.findViewById(R.id.yawText);
            pitchText = (TextView) rootView.findViewById(R.id.pitchText);
            rollText = (TextView) rootView.findViewById(R.id.rollText);

            ListView list = (ListView) rootView.findViewById(R.id.list);  //Find the id of the target ListView
            list.setAdapter(dataListAdaptor);                            //Bind the adaptor to the ListView

            final Button button_clear_all = (Button) rootView.findViewById(R.id.button_clear_all);
            button_clear_all.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {  //Button handler for clearing the data list
                    dataList.clear();
                    dataListAdaptor.notifyDataSetChanged();
                }
            });

            final Button button_refresh = (Button) rootView.findViewById(R.id.button_refresh);
            button_refresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {  //Button handler for pulling data
                    new PullData().execute();
                }
            });

            final Button button_datatx = (Button) rootView.findViewById(R.id.button_datatx);
            button_datatx.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {  //Button handler for triggering a data transmission
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    /*final String serverAddr =
                            sharedPref.getString(getString(R.string.pref_http_key),
                                    getString(R.string.pref_http_default));  //Get the server Address*/
                    //The server address is in the string "serverAddr". For debugging purposes, I set this address adjustable.

                    /**********************************/
                    //@TODO this only sends the most current line to the server!!!
                    SaveRFData saveRF = new SaveRFData();
                    dataStruct.updateObject();
                    saveRF.RFMember = DataCollector.kludge;
                    saveRF.execute();
                    /**********************************/

                    //tbranyon: code block to push data to server
                    /*Thread t = new Thread() {
                        public void run() {
                            JSONArray JSONlist = dbHandle.getUnsentData();
                            for (int x = 0; x < JSONlist.length(); ++x) {
                                HTTP_SEND_STATUS = 0;
                                try {
                                    sendHTTPdata((JSONObject) JSONlist.get(x), serverAddr);
                                    System.out.println(JSONlist.get(x));
                                } catch (Exception e) {
                                    System.err.print(e);
                                }
                                if (HTTP_SEND_STATUS == -1)
                                    System.err.println("Error sending!"); //change later to toast message on phone screen
                            }
                            System.out.println("Send thread finished");
                        }
                    }; //End of thread t
                    t.start(); //start send thread*/
                }
            });

            final Button button_dataCollect = (Button) rootView.findViewById(R.id.run_button);
            button_dataCollect.setOnClickListener(new View.OnClickListener() { //tbranyon: click listener for run/stop button
                public void onClick(View v) {
                    if (loopIsRunning) { //code block to ensure proper run/stop functionality
                        runEnable = false;
                        loopIsRunning = false;
                        return;
                    } else
                        runEnable = true;

                    loopIsRunning = true;
                    Thread t = new Thread() { //runs data collection loop in separate thread
                        public void run() {
                            while (runEnable) {
                                new PullData().execute();
                                try {
                                    Thread.sleep(1000, 0); //execute at 1Hz //@TODO pull refresh speed from user prefs
                                }catch(Exception e){System.err.println(e.getMessage());}
                            }
                        }
                    };
                    t.start();
                }
            });
        }
        return rootView;
    }

    /**
     * @brief    SaveRFData
     *
     *           AsyncTask that gets data from MySQL datbase and displays it on map
     */
    private class SaveRFData extends AsyncTask<String, Void, String> {
        private Exception exception; //@TODO: assigned but never accessed
        public RFData RFMember;
        public boolean AddComplete = false;
        //public boolean AddErr = false;

        //--------------------------------------------------------------------------------------------------------------

        /**
         * @brief doInBackground
         * <p/>
         * Gets the data from the MySQL server then calls onPostExecute
         */
        protected String doInBackground(String... parameters) {
            try {
                AddComplete = false;
                if (RFMember != null) {
                    /// Pull from database the data that matches this range
                    RFFieldSQLDatabase RFFieldDatabase = new RFFieldSQLDatabase();

                    /// Connect to test server (for now), if not connected return null
                    if (RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com")) {
                        /// Store data to database, return the results
                        boolean status = RFFieldDatabase.AddNewEntry(RFMember);
                        if (status) return "Success";
                        else return "Failed to add";
                    } else return "Not Connected";
                } else return "Null Data";

            } catch (Exception e) {
                /// Exception processing, display error and then return null
                System.out.println("Err: " + e.getMessage());
                this.exception = e;
                return e.getMessage();
            }
        }
    }

    /**
     * @fn sendHTTPdata
     * @brief Method for sending JSON lines from local DB to server
     */
    /*protected void sendHTTPdata(JSONObject json, String serverAddress)
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
	}*/
}
