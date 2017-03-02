// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//          RF Signal Map
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         MainActivityFragment.java
 * @brief        Project #3 - Main Fragment
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------
package edu.tamu.rfsignalmap;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import android.app.Activity;
import android.content.Context;
import android.app.Fragment;
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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import org.json.JSONObject;

//----------------------------------------------------------------------------------------------------------------------
/** @class      MainActivityFragment
 *  @brief      Main Activity Fragment - Display Captured Data
 */
public class MainActivityFragment extends Fragment implements
        View.OnClickListener, LocationListener, SensorEventListener {
    //------------------------------------------------------------------------------------------------------------------
    /// User Interface Objects
    TextView tvRSSI;                                                    /// Text View for RSSI
    TextView tvRSSI2;                                                   /// Text View for RSSI
    TextView tvRSSI3;                                                   /// Text View for RSSI
    TextView tvRSSICell;                                                /// Text View for RSSI Cell
    TextView tvLat;                                                     /// Text View for Lat
    TextView tvLong;                                                    /// Text View for Long
    TextView tvYaw;                                                     /// Text View for Yaw
    TextView tvPitch;                                                   /// Text View for Pitch
    TextView tvRoll;                                                    /// Text View for Roll
    Button btnRecord;                                                   /// Record button
    ListView lvData;                                                    /// List videw of recent data recorded
    CheckBox cbAuto;                                                    /// Check box for auto record mode

    int update_count = 0;
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    /// Serial Port - To communicate with Xbee
    private UsbSerialPort port;
    private SerialInputOutputManager mSerialIoManager;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private UsbSerialPort port_2;
    private SerialInputOutputManager mSerialIoManager_2;
    private final ExecutorService mExecutor_2 = Executors.newSingleThreadExecutor();

    private UsbSerialPort port_3;
    private SerialInputOutputManager mSerialIoManager_3;
    private final ExecutorService mExecutor_3 = Executors.newSingleThreadExecutor();
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Current RF Data Object
    //------------------------------------------------------------------------------------------------------------------
    static RFData CurrenSample = new RFData();                                 /// Current RF Data
    static boolean first_time = true;
    static private double CellRSSI = -115.0;

    //------------------------------------------------------------------------------------------------------------------
    /// Handlers
    Handler handler = new Handler();                                    /// Handler for UI tasks from Executor
    UpdateUI UIUpdateTask = new UpdateUI();                             /// Update Task
    AppPhoneStateListener AppPhoneListener;                             /// Application phone listener
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Managers
    TelephonyManager Telephony;                                         /// Telephony Manager

    //------------------------------------------------------------------------------------------------------------------
    /// Create Scheduled Executor Service
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    //------------------------------------------------------------------------------------------------------------------

    private final ArrayList<String> dataList;
    static private ArrayAdapter<String> dataListAdaptor;

    boolean online = false;
    boolean online_2 = false;
    boolean online_3 = false;
    private double XBeeRSSI = -9999.0;

    //------------------------------------------------------------------------------------------------------------------
    // SERIAL PORT LISTENER - #1
    //------------------------------------------------------------------------------------------------------------------
    private final SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener() {

        private final static String RXID = "Receive ID";
        private final static String TXID = "Transmit ID";
        private final static String RSSI = "RSSI";

        @Override
        //------------------------------------------------------------------------------------------------------------------
        /**
         * @fun SerialInputOutputManager.Listener:onRunError
         * @brief On Run Error
         */
        public void onRunError(Exception e) {
        }

        @Override
        //------------------------------------------------------------------------------------------------------------------
        /**
         * @fun SerialInputOutputManager.Listener:onNewData
         * @brief On New Data
         */
        public void onNewData(final byte[] data) {
            try {
                // Append data to the incomming string buffer
                // Read data from Teensy device (based upon project by Paul Crouther) - JSON Object being streamed
                // to serial port at 9600 bps, includes IDs and RSSI, Note: RSSI is pos, actually is negative.
                String newrx = new String(data, "UTF-8");
                if (newrx.contains("analog") == true) XBeeRSSI = -120.0;
                else {
                    JSONObject serialJSONObj = new JSONObject(newrx);
                    CurrenSample.XbeeID = serialJSONObj.getInt(RXID);
                    CurrenSample.DeviceID = serialJSONObj.getInt(TXID);
                    XBeeRSSI = -1.0 * serialJSONObj.getDouble(RSSI);
                    CurrenSample.RSSI =  -1.0 * serialJSONObj.getDouble(RSSI);
                }

            } catch (Exception e) {
            }
        }
    };


    //------------------------------------------------------------------------------------------------------------------
    // SERIAL PORT LISTENER - #2
    //------------------------------------------------------------------------------------------------------------------
    private final SerialInputOutputManager.Listener mListener_2 = new SerialInputOutputManager.Listener() {

        private final static String RXID = "Receive ID";
        private final static String TXID = "Transmit ID";
        private final static String RSSI = "RSSI";

        @Override
        //------------------------------------------------------------------------------------------------------------------
        /**
         * @fun SerialInputOutputManager.Listener:onRunError
         * @brief On Run Error
         */
        public void onRunError(Exception e) {
        }

        @Override
        //------------------------------------------------------------------------------------------------------------------
        /**
         * @fun SerialInputOutputManager.Listener:onNewData
         * @brief On New Data
         */
        public void onNewData(final byte[] data) {
            try {
                // Append data to the incomming string buffer
                // Read data from Teensy device (based upon project by Paul Crouther) - JSON Object being streamed
                // to serial port at 9600 bps, includes IDs and RSSI, Note: RSSI is pos, actually is negative.
                String newrx = new String(data, "UTF-8");
                if (newrx.contains("analog") == true) CurrenSample.RSSI2 = -120.0;
                else {
                    JSONObject serialJSONObj = new JSONObject(newrx);
                    CurrenSample.XbeeID2 = serialJSONObj.getInt(RXID);
                    CurrenSample.DeviceID2 = serialJSONObj.getInt(TXID);
                    CurrenSample.RSSI2 = -1.0 * serialJSONObj.getDouble(RSSI);
                }
            } catch (Exception e) {
            }
        }
    };

    //------------------------------------------------------------------------------------------------------------------
    // SERIAL PORT LISTENER - #3
    //------------------------------------------------------------------------------------------------------------------
    private final SerialInputOutputManager.Listener mListener_3 = new SerialInputOutputManager.Listener() {

        private final static String RXID = "Receive ID";
        private final static String TXID = "Transmit ID";
        private final static String RSSI = "RSSI";

        @Override
        //------------------------------------------------------------------------------------------------------------------
        /**
         * @fun SerialInputOutputManager.Listener:onRunError
         * @brief On Run Error
         */
        public void onRunError(Exception e) {
        }

        @Override
        //------------------------------------------------------------------------------------------------------------------
        /**
         * @fun SerialInputOutputManager.Listener:onNewData
         * @brief On New Data
         */
        public void onNewData(final byte[] data) {
            try {
                // Append data to the incomming string buffer
                // Read data from Teensy device (based upon project by Paul Crouther) - JSON Object being streamed
                // to serial port at 9600 bps, includes IDs and RSSI, Note: RSSI is pos, actually is negative.
                String newrx = new String(data, "UTF-8");
                if (newrx.contains("analog") == true) CurrenSample.RSSI3 = -120.0;
                {
                    JSONObject serialJSONObj = new JSONObject(newrx);
                    CurrenSample.XbeeID3 = serialJSONObj.getInt(RXID);
                    CurrenSample.DeviceID3 = serialJSONObj.getInt(TXID);
                    CurrenSample.RSSI3 = -1.0 * serialJSONObj.getDouble(RSSI);
                }
            } catch (Exception e) {
            }
        }
    };


    //------------------------------------------------------------------------------------------------------------------
    // RUNNABLE TASKS
    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @class UpdateUI
     * @brief User Interface Update from Executor
     */
    class UpdateUI implements Runnable {
        private int stuff = 0;
        String strDate;
        private boolean busy = false;

        //--------------------------------------------------------------------------------------------------------------
        /**
         * @brief run
         * <p/>
         * Inputs: none
         * Return: none
         * Run routine - Called on posted message from handler
         */
        public void run() {
            // Select RSSI based on what is current
            //if (XBeeRSSI <= 0.0) CurrenSample.RSSI = XBeeRSSI;
            //else CurrenSample.RSSI = CellRSSI;

            // Update data on UI
            if (tvRSSI != null)
            {   // Print data if available, if not then print na
                if ((XBeeRSSI <= 0.0) & (XBeeRSSI > -999.0))  tvRSSI.setText(String.format("%3.0f", XBeeRSSI) + "dBm");
                else tvRSSI.setText("- na -");
            }
            if (tvRSSI2 != null)
            {   // Print data if available, if not then print na
                if ((CurrenSample.RSSI2 <= 0.0)& (CurrenSample.RSSI2 > -999.0))  tvRSSI2.setText(String.format("%3.0f", CurrenSample.RSSI2) + "dBm");
                else tvRSSI2.setText("- na -");
            }
            if (tvRSSI3 != null)
            {   // Print data if available, if not then print na
                if ((CurrenSample.RSSI3 <= 0.0) & (CurrenSample.RSSI3 > -999.0)) tvRSSI3.setText(String.format("%3.0f", CurrenSample.RSSI3) + "dBm");
                else tvRSSI3.setText("- na -");
            }
            if (tvRSSICell != null) tvRSSICell.setText(String.format("%3.0f", CellRSSI) + "dBm");
            if (tvLat != null) tvLat.setText(String.format("%.8f", CurrenSample.Latitude) + "°");
            if (tvLong != null) tvLong.setText(String.format("%.8f", CurrenSample.Longitude) + "°");
            if (tvYaw != null) tvYaw.setText(String.format("%3.1f", CurrenSample.Yaw) + "°");
            if (tvPitch != null) tvPitch.setText(String.format("%3.1f", CurrenSample.Pitch) + "°");
            if (tvRoll != null) tvRoll.setText(String.format("%3.1f", CurrenSample.Roll) + "°");

            update_count++;
            // Send data to server, if in automatic mode and set
            if ((busy == false) & (update_count > 8))
            {
                update_count = 0;
                if (cbAuto != null)
                {
                    if (cbAuto.isChecked() == true)
                    {
                        busy = true;
                        /// Disable the button, until finished (from Async task)
                        btnRecord.setEnabled(false);

                        /// Get the current time (sample date / time stamp)
                        Calendar calendar = Calendar.getInstance();
                        CurrenSample.SampleDate = calendar.getTime();

                        if ((CurrenSample.RSSI < 0.0) & (CurrenSample.RSSI2 < 0.0))
                        {

                            /// Create a new Async task to save the data
                            SaveRFData SaveDataTask = new SaveRFData();
                            /// Copy over the data to the class, then execute (will finish in post event)
                            SaveDataTask.RFMember = CurrenSample;
                            SaveDataTask.execute("");
                            while (SaveDataTask.AddingData == true) {
                                try {
                                    wait(10);
                                } catch (InterruptedException e) {
                                    break;
                                }
                            }
                        }
                        busy = false;
                    }
                }
            }

        }
    }

    ;

    //------------------------------------------------------------------------------------------------------------------
    /// Update System Task - For Executor
    final Runnable SysUpdater = new Runnable() {
        //--------------------------------------------------------------------------------------------------------------

        /**
         * @brief run
         *
         *           Inputs: none
         *           Return: none
         *           Run routine - Called on schedule
         */
        public void run() {
            /// Update user interface information, post to handler
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
            final String strDate = simpleDateFormat.format(calendar.getTime());

            UIUpdateTask.strDate = strDate;
            handler.post(UIUpdateTask);
        }
    };


    public MainActivityFragment() {
        dataList = new ArrayList<>();
        CurrenSample.XbeeID = 1;
        CurrenSample.DeviceID = 2;
        CurrenSample.XbeeID2 = 1;
        CurrenSample.DeviceID2 = 2;
        CurrenSample.XbeeID3 = 1;
        CurrenSample.DeviceID3 = 2;
    }


    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief onAttach Event
     *
     *           Inputs: none
     *           Return: none
     *           On Attach of Fragment to Context - Fires 1st
     */
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief onCreate Event
     *
     *           Inputs: Saved Instance State
     *           Return: none
     *           On Creation of Fragment - Fires 2nd
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (first_time == true) {
            //Initializing the data list adaptor
            dataListAdaptor = new ArrayAdapter<>(
                    getActivity(),                    //The Current Parent Activity
                    R.layout.single_record,           //The .xml file that contains the textview
                    R.id.list_item_record,            //The id of the textview
                    dataList                          //The source of the data
            );
        }

        // Sensor Manager - Setup to get the Yaw, Pitch, and Roll
        SensorManager senSensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
        Sensor senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Location Manager - Setup to get the Latitude and Longitude
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        try {
            // Update at least every 1000ms, as with 3 meter distance
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, this);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }

        // Telephony Manager - Setup to get teh RSSI
        AppPhoneListener = new AppPhoneStateListener();
        Telephony = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        Telephony.listen(AppPhoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        /// Setup scheduled Executor
        final ScheduledFuture<?> updateHandle = scheduler.scheduleAtFixedRate(SysUpdater, 250, 250, TimeUnit.MILLISECONDS);


        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager)getActivity().getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }

        /// Open a connection to the first available driver. This is for RSSI #1
        // Try to open Port - 0
        try{
            UsbSerialDriver driver = availableDrivers.get(0);
            UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
            List<UsbSerialPort> portList = driver.getPorts();
            port = portList.get(0);

            // Open the port, create connection, set baud rate and listener...
            port.open(connection);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int BaudRate = 9600;
            port.setParameters(BaudRate, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            online = true;
            mSerialIoManager = new SerialInputOutputManager(port, mListener);
            mExecutor.submit(mSerialIoManager);
        } catch(Exception e) {
            online = false;
            System.out.println(e.getMessage());
        }

        /// Open a connection to the first available driver. This is for RSSI #2
        // Try to open Port - 1
        try{
            UsbSerialDriver driver2 = availableDrivers.get(1);
            UsbDeviceConnection connection2 = manager.openDevice(driver2.getDevice());
            List<UsbSerialPort> portList2 = driver2.getPorts();
            port_2 = portList2.get(0);
            // Open the port, create connection, set baud rate and listener...
            port_2.open(connection2);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int BaudRate = 9600;
            port_2.setParameters(BaudRate, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            online_2 = true;
            mSerialIoManager_2 = new SerialInputOutputManager(port_2, mListener_2);
            mExecutor_2.submit(mSerialIoManager_2);
        } catch(Exception e) {
            online_2 = false;
            System.out.println(e.getMessage());
        }

        /// Open a connection to the first available driver. This is for RSSI #3
        // Try to open Port - 1
        try{
            UsbSerialDriver driver3 = availableDrivers.get(2);
            UsbDeviceConnection connection3 = manager.openDevice(driver3.getDevice());
            List<UsbSerialPort> portList3 = driver3.getPorts();
            port_3 = portList3.get(0);
            // Open the port, create connection, set baud rate and listener...
            port_3.open(connection3);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int BaudRate = 9600;
            port_3.setParameters(BaudRate, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            online_3 = true;
            mSerialIoManager_3 = new SerialInputOutputManager(port_3, mListener_3);
            mExecutor_3.submit(mSerialIoManager_3);
        } catch(Exception e) {
            online_3 = false;
            System.out.println(e.getMessage());
        }

        /// Set flag
        first_time = false;
    }


    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief onCreateView Event
     *
     *           Inputs: Inflater, Container, and Saved Instance State
     *           Return: View
     *           Create View and Fragment Resources
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, parent, false);


        ListView lvData = (ListView) rootView.findViewById(R.id.lvData);  //Find the id of the target ListView
        lvData.setAdapter(dataListAdaptor);                            //Bind the adaptor to the ListView

        return rootView;
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief onStop Event
     *
     *           Inputs: none
     *           Return: none
     *           Stop Fragment
     */
    public void onStop() {
        super.onStop();
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief onPause Event
     *
     *           Inputs: none
     *           Return: none
     *           Pause Fragment
     */
    public void onPause() {
        super.onPause();
        Telephony.listen(AppPhoneListener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief onResume Event
     *
     *           Inputs: none
     *           Return: none
     *           Resume Fragment
     */
    public void onResume() {
        super.onResume();
        Telephony.listen(AppPhoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn onDestroyView
     * @brief onDestroyView Event
     *
     *           Inputs: none
     *           Return: none
     *           Destroy View
     */
    public void onDestroyView() {
        try {
            /// Shutdown the Scheduler, if not already shutdown
            if (scheduler.isShutdown() == false) scheduler.shutdown();
        } catch (Exception e) {
        }
        super.onDestroyView();
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn onDestroy
     * @brief onDestroy Event
     *
     *           Inputs: none
     *           Return: none
     *           Destroy Event - Close Port
     */
    public void onDestroy() {
        try {
            mSerialIoManager.stop();
            mSerialIoManager = null;
            online = false;
            port.close();        /// Try and close the serial port
        } catch (Exception e){
            online = false;
            System.out.println(e.getMessage());
        }
        super.onDestroy();
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //  Text Views
        // RSSI
        tvRSSI = (TextView) view.findViewById(R.id.textRSSI);
        tvRSSI.setText("");
        // RSSI2
        tvRSSI2 = (TextView) view.findViewById(R.id.textRSSI2);
        tvRSSI2.setText("");
        // RSSI3
        tvRSSI3 = (TextView) view.findViewById(R.id.textRSSI3);
        tvRSSI3.setText("");
        // RSSI3
        tvRSSICell = (TextView) view.findViewById(R.id.textRSSICell);
        tvRSSICell.setText("");
        // Lat
        tvLat = (TextView) view.findViewById(R.id.textLat);
        tvLat.setText("-");
        // Long
        tvLong = (TextView) view.findViewById(R.id.textLong);
        tvLong.setText("-");
        // Yaq
        tvYaw = (TextView) view.findViewById(R.id.textYaw);
        tvYaw.setText("-");
        // Pitch
        tvPitch = (TextView) view.findViewById(R.id.textPitch);
        tvPitch.setText("-");
        // Roll
        tvRoll = (TextView) view.findViewById(R.id.textRoll);
        tvRoll.setText("-");

        // Add Button
        btnRecord = (Button) view.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(this);

        // Auto Record Check Box
        cbAuto = (CheckBox) view.findViewById(R.id.checkBoxAuto);
    }

    //	----------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onClick
     * @brief   onClick Event
     *          Inputs: View
     *          Return: none
     *          Process click events from user
     */
    public void onClick(View view) {
        /// Process the record button
        if (view == btnRecord) {
            /// Disable the button, until finished (from Async task)
            btnRecord.setEnabled(false);

            /// Get the current time (sample date / time stamp)
            Calendar calendar = Calendar.getInstance();
            CurrenSample.SampleDate = calendar.getTime();

            /// Create a new Async task to save the data
            SaveRFData SaveDataTask =  new SaveRFData();
            /// Copy over the data to the class, then execute (will finish in post event)
            SaveDataTask.RFMember = CurrenSample;
            SaveDataTask.execute("");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onActivityCreated
     * @brief   onActivityCreated Event
     *          Inputs: savedInstanceState
     *          Return: none
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //------------------------------------------------------------------------------------------------------------------
    // GPS LOCATION
    //------------------------------------------------------------------------------------------------------------------
    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onLocationChanged
     * @brief   onLocationChanged Event
     *          Inputs: Location
     *          Return: none
     *          Get new Lat/Long and assign to current data
     */
    public void onLocationChanged(Location location) {
        CurrenSample.Latitude = location.getLatitude();
        CurrenSample.Longitude = location.getLongitude();
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn onStatusChanged
     * @brief onStatusChanged Event
     *           Inputs: provider, status, extras
     *           Return: none
     *           Not utilized
     */
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn onProviderEnabled
     * @brief onProviderEnabled Event
     *           Inputs: provider
     *           Return: none
     *           Not utilized
     */
    public void onProviderEnabled(String provider) {
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn onProviderDisabled
     * @brief onProviderDisabled Event
     *           Not utilized
     */
    public void onProviderDisabled(String provider) {
    }

    //------------------------------------------------------------------------------------------------------------------
    // SENSOR
    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onSensorChanged
     * @brief   onSensorChanged Event
     *          Sensor Changes - Get updated data
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        // Get values
        if (mySensor.getType() == Sensor.TYPE_ORIENTATION) {
            CurrenSample.Yaw = event.values[0];
            CurrenSample.Pitch = event.values[1];
            CurrenSample.Roll = event.values[2];
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onAccuracyChanged
     * @brief   Listener not implemented
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //------------------------------------------------------------------------------------------------------------------
    // Telephony Inteface
    //------------------------------------------------------------------------------------------------------------------
    private class AppPhoneStateListener extends PhoneStateListener
    {
        //------------------------------------------------------------------------------------------------------------------
        /**
         * @fn      onSignalStrengthsChanged
         * @brief   onSignalStrengthsChanged Event (Listener)
         *          Get the Signal strength from the provider, each time there is an update
         */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            super.onSignalStrengthsChanged(signalStrength);

            // If GSP (i.e. AT&T is T-Mobile) then pull the GSP data and
            // Convert it to RSSSI, else, get teh CDMA (i.e. Verizon network, or similar) data
            if (signalStrength.isGsm() == true) {
                // AT&T Phone, Based on TS 27.007 8.5
                int GSMSS = signalStrength.getGsmSignalStrength();
                if (GSMSS >= 99) CellRSSI = -113.0;
                else if (GSMSS >= 31) CellRSSI = -31.0;
                else if (GSMSS >= 2) CellRSSI = (((double) GSMSS) * 2.0) - 113.0;
                else if (GSMSS == 1) CellRSSI = -111.0;
                else CellRSSI= -113.0;
            }
            else CellRSSI = signalStrength.getCdmaDbm();

            // Copy over signtal strength data, as string (will be stored in database as such)
            CurrenSample.CellSignalStrength = signalStrength.toString();
        }

    };

    //------------------------------------------------------------------------------------------------------------------
    // RF Data Storage Interface (to database)
    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      SaveRFData
     * @brief   SaveRFData Method
     *          Stores   current data to server
     */
    private class SaveRFData extends AsyncTask<String, Void, String> {
        public Exception exception;
        public RFData RFMember;
        public boolean AddComplete = false;
        public boolean AddingData = false;
        public boolean AddErr = false;

        //--------------------------------------------------------------------------------------------------------------
        /**
         * @fn      doInBackground
         * @brief   doInBackground
         *          Gets the data from the MySQL server then calls onPostExecute
         */
        protected String doInBackground(String... parameters) {
            try {
                AddingData = true;
                AddComplete = false;
                if (RFMember != null) {                    /// Pull from database the data that matches this range
                    RFFieldSQLDatabase RFFieldDatabase = new RFFieldSQLDatabase();

                    /// Connect to test server (for now), if not connected return null
                    if (RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com") == true) {
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
                AddingData = false;
                return e.getMessage();
            }
        }


        //--------------------------------------------------------------------------------------------------------------
        /**
         * @fn      onPostExecute
         * @brief   onPostExecute
         *          Process and display the resulting records
         */
        protected void onPostExecute(String result) {
            /// Add data to log
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
            String log = "RSSI: " + String.format("%3.0f", RFMember.RSSI) + " dBm  at  " + simpleDateFormat.format(RFMember.SampleDate);
            dataList.add(0, log);

            /// Clip to 50 list items
            if (dataList.size() > 50) dataList.remove(50);
            dataListAdaptor.notifyDataSetChanged();  //Refresh the list display

            /// Reenable the command button
            btnRecord.setEnabled(true);
            if (result == "Success") AddErr = false;
            else AddErr = true;
            AddComplete = true;
            AddingData = false;
        }
    }
}


