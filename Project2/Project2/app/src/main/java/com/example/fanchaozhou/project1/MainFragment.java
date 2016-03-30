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
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
import android.support.design.widget.Snackbar;
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

import org.json.JSONArray;
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
    private final static String RXID = "Receive ID";
    private final static String TXID = "Transmit ID";
    private final static String RSSI = "RSSI";
    private Sensor senMagnetometer;

    private DataCollector dataStruct;

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

        //Adding the AlignmentFragment
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        //AlignmentFragment Al = new AlignmentFragment();

        //FT.add(R.id.align_fragment, Al);
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


        }

    }

    /**
     * @fn onStart
     * @brief Populate the list with currently collected data when switching back to the main fragment
     */
    @Override
    public void onStart() {
        super.onStart();
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

        dataListAdaptor.notifyDataSetChanged(); //@TODO this line causes an exception on app close
    }

    @Override
    public void onStop(){
        super.onStop();

        try {
            port.close();        //Release the serial port
        } catch (Exception e){
            System.out.println(e);
        }
    }
    /**
     * @fn onDestroy
     * @brief closes serial port
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * @fn onSensorChanged
     * @brief gets the new imu values
     */

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

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
            DataCollector.transmitID = "5"; //@TODO do not hardcode these, get from environment
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

            WifiManager wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
            DataCollector.wifiRSSI = wifiManager.getConnectionInfo().getRssi();
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
            SharedPreferences displaySettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String displayStr = "Transmitter ID: " + data.get(0) + "\n" +
                    "Receiver ID: " + data.get(2) + "\n" +
                    "TimeStamp: " + data.get(5);
            if(displaySettings.getBoolean(getString(R.string.pref_gyro_key), false)){
                displayStr += ("\nOrientation: " + data.get(3));
            }
            if(displaySettings.getBoolean(getString(R.string.pref_rss_s1_key), false)){
                displayStr += ("\nRSS Src1(External RSSI): " + -Double.parseDouble(data.get(1)));
            }
            if(displaySettings.getBoolean(getString(R.string.pref_gps_changes_key), false)){
                displayStr += ("\nLocation: " + data.get(4));
            }
            if(displaySettings.getBoolean(getString(R.string.pref_mag_key), false)){
                displayStr += ("   \nMagnetic Field: " + data.get(6));
            }
            if(displaySettings.getBoolean(getString(R.string.pref_rss_s2_key), false)){
                displayStr += ("\nRSS Src2(Internal RSSI): " + data.get(7));
            }
            synchronized (dataList){
                dataList.add(0, displayStr);             //Add the new data to the top of the list
                dataListAdaptor.notifyDataSetChanged();  //Refresh the list display
            }
        }
    }

    /**
     * @fn onCreateView
     * @brief adds button click listeners, creates http connection, sends data to server
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState == null) {

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
                    new    PullData().execute();
                }
            });

            final Button button_datatx = (Button) rootView.findViewById(R.id.button_datatx);
            button_datatx.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {  //Button handler for triggering a data transmission
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity()); //@TODO prefs need to be dealt with in send data routine (JSON prefs)
                    new SaveRFData().execute();
                }
            });

            final Button button_dataCollect = (Button) rootView.findViewById(R.id.run_button);
            button_dataCollect.setOnClickListener(new View.OnClickListener() { //tbranyon: click listener for run/stop button
                public void onClick(View v) {
                    if (loopIsRunning) { //code block to ensure proper run/stop functionality
                        runEnable = false;
                        loopIsRunning = false;
                        Snackbar.make(v, "stopped", 700 ).show(); // create a snackbar notification to notify data collection status
                        return;
                    } else {
                        runEnable = true;
                        Snackbar.make(v, "collecting data...", 700 ).show(); //It says a number can't be hardcoded here, but it doesn't seem to cause any issues
                    }

                    loopIsRunning = true;
                    Thread t = new Thread() { //runs data collection loop in separate thread
                        public void run() {
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            while (runEnable) {
                                if(DataCollector.aligned) { // only run when aligned if the checkbox is checked
                                    new PullData().execute();
                                    try {
                                        Thread.sleep(1000, 0); //execute at 1Hz //@TODO pull refresh speed from user prefs
                                    } catch (Exception e) {
                                        System.err.println(e.getMessage());
                                    }
                                }
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
        public boolean AddErr = false;

        //--------------------------------------------------------------------------------------------------------------

        /**
         * @brief doInBackground
         * <p/>
         * Gets the data from the MySQL server then calls onPostExecute
         */
        protected String doInBackground(String... parameters) {
            try {
                AddComplete = false;
                if (RFMember == null) {
                    RFMember = new RFData();
                }
                if (RFMember != null) {
                    /// Pull from database the data that matches this range
                    RFFieldSQLDatabase RFFieldDatabase = new RFFieldSQLDatabase();

                    /// Connect to test server (for now), if not connected return null
                    if (RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com")) {
                        /// Store data to database, return the results
                        JSONArray jsonlist = dbHandle.getUnsentData();
                        for(int x = 0; x < jsonlist.length(); ++x)
                        {
                            try {
                                RFMember.assignValuesFromJSON(jsonlist.getJSONObject(x));
                                boolean status = RFFieldDatabase.AddNewEntry(RFMember);
                                if(!status)
                                {
                                    AddErr = true;
                                    break;
                                }
                            }catch(Exception e){System.err.println(e);}
                        }

                        if (!AddErr){
                            AddComplete = true;
                            return "Success";
                        }
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
}
