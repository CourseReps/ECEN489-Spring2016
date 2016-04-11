// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//          RF Signal Map
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         MainActivityFragment.java
 * @brief        RF Signal Main Activity - Main Fragment
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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//----------------------------------------------------------------------------------------------------------------------
/** @class      MainActivityFragment
 *  @brief      Main Activity Fragment - Display Captured Data
 */
public class MainActivityFragment extends Fragment implements
        View.OnClickListener, LocationListener, SensorEventListener {
    //------------------------------------------------------------------------------------------------------------------
    /// User Interface Objects
    TextView tvRSSI;                                                    /// Text View for RSSI
    TextView tvLat;                                                     /// Text View for Lat
    TextView tvLong;                                                    /// Text View for Long
    TextView tvYaw;                                                     /// Text View for Yaw
    TextView tvPitch;                                                   /// Text View for Pitch
    TextView tvRoll;                                                    /// Text View for Roll
    Button btnRecord;                                                   /// Record button
    ListView lvData;                                                    /// List videw of recent data recorded
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Current RF Data Object
    //------------------------------------------------------------------------------------------------------------------
    static RFData CurrenSample = new RFData();                                 /// Current RF Data
    static boolean first_time = true;

    int count = 0;


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

        //--------------------------------------------------------------------------------------------------------------

        /**
         * @brief run
         * <p/>
         * Inputs: none
         * Return: none
         * Run routine - Called on posted message from handler
         */
        public void run() {
            // Update data on UI
            if (tvRSSI != null) tvRSSI.setText(String.format("%3.0f", CurrenSample.RSSI) + " dBm");
            if (tvLat != null) tvLat.setText(String.format("%.8f", CurrenSample.Latitude) + "°");
            if (tvLong != null) tvLong.setText(String.format("%.8f", CurrenSample.Longitude) + "°");
            if (tvYaw != null) tvYaw.setText(String.format("%3.1f", CurrenSample.Yaw) + "°");
            if (tvPitch != null) tvPitch.setText(String.format("%3.1f", CurrenSample.Pitch) + "°");
            if (tvRoll != null) tvRoll.setText(String.format("%3.1f", CurrenSample.Roll) + "°");
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
            UIUpdateTask.stuff = UIUpdateTask.stuff + 1;
            handler.post(UIUpdateTask);
        }
    };


    public MainActivityFragment() {
        dataList = new ArrayList<>();

        //runEnable = false;
        //loopIsRunning = false;
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
//        Sensor senAccelerometer = senSensorManager.getOrientation()
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
//        PhoneStateListener  phoneListener = new PhoneStateListener();
        AppPhoneListener = new AppPhoneStateListener();
        Telephony = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        Telephony.listen(AppPhoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);




        /// Setup scheduled Executor
        final ScheduledFuture<?> updateHandle = scheduler.scheduleAtFixedRate(SysUpdater, 250, 250, TimeUnit.MILLISECONDS);

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
        ;
        super.onDestroyView();
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

        //   ListView lv = (ListView) view.findViewById(R.id.lvSome);
        //    lv.setAdapter(adapter);
    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     onClick
    //      Inputs:	    View
    //     Outputs:	    none
    //  Description:    onClick Event
    //	----------------------------------------------------------------------------------------------------------------
    public void onClick(View view) {
        /// Process the record button
        if (view == btnRecord) {
            /// Disable the button, until finished (from Async task)
            btnRecord.setEnabled(false);

            /// Get the current time (sample date / time stamp)
            Calendar calendar = Calendar.getInstance();
            CurrenSample.SampleDate = calendar.getTime();
            CurrenSample.XbeeID = 1;
            CurrenSample.DeviceID = 2;

            /// Create a new Async task to save the data
            SaveRFData SaveDataTask =  new SaveRFData();

            /// Copy over the data to the class, then execute (will finish in post event)
            SaveDataTask.RFMember = CurrenSample;
            SaveDataTask.execute("");
        }
    }


    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
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
     * @fn onLocationChanged
     * @brief onLocationChanged Event - Get new Lat/Long and assign to current data
     *           Inputs: Location
     *           Return: none
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
    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn onSensorChanged
     * @brief onSensorChanged Event
     *           Sensor Changes - Get updated data
     */
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        // Get values
        if (mySensor.getType() == Sensor.TYPE_ORIENTATION) {
            CurrenSample.Yaw = event.values[0];
            CurrenSample.Pitch = event.values[1];
            CurrenSample.Roll = event.values[2];
        }
    }

    /**
     * @fn onAccuracyChanged
     * @brief not implemented
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //------------------------------------------------------------------------------------------------------------------
    // Telephony Inteface
    //------------------------------------------------------------------------------------------------------------------
    private class AppPhoneStateListener extends PhoneStateListener
    {
        // Get the Signal strength from the provider, each tiome there is an update
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm() == true) {
                // AT&T Phone, Based on TS 27.007 8.5
                int GSMSS = signalStrength.getGsmSignalStrength();
                if (GSMSS >= 99) CurrenSample.RSSI = -113.0;
                else if (GSMSS >= 31) CurrenSample.RSSI = -31.0;
                else if (GSMSS >= 2) CurrenSample.RSSI = (((double) GSMSS) * 2.0) - 113.0;
                else if (GSMSS == 1) CurrenSample.RSSI = -111.0;
                else CurrenSample.RSSI = -113.0;
            }
            else CurrenSample.RSSI = signalStrength.getCdmaDbm();

           // System.out.println(GSMSS + ";"  + signalStrength.getEvdoDbm() +"; " + signalStrength.getCdmaDbm() +  signalStrength);
        }

    };

    //------------------------------------------------------------------------------------------------------------------
    // RF Data Storage Interface (to database)
    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn SaveRFData
     * @brief SaveRFData Method
     * Stores current data to server
     */
    private class SaveRFData extends AsyncTask<String, Void, String> {
        public Exception exception;
        public RFData RFMember;
        public boolean AddComplete = false;
        public boolean AddErr = false;

        //--------------------------------------------------------------------------------------------------------------

        /**
         * @brief doInBackground
         * Gets the data from the MySQL server then calls onPostExecute
         */
        protected String doInBackground(String... parameters) {
            try {
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
                return e.getMessage();
            }
        }


        //--------------------------------------------------------------------------------------------------------------

        /**
         * @brief onPostExecute
         * <p/>
         * Process and display the resulting records
         */
        protected void onPostExecute(String result) {
            UIUpdateTask.stuff = 0;

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
        }
    }
}


