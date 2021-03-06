/**
 * @file MainFragment.java
 *
 * @brief This file contains the brains behind the Data Display Fragment
 *
 **/
package edu.tamu.ecen489s2016.project2;

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
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private double latitude = 0;
    private double longitude = 0;
    private LocationManager locationManager;

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

        if (mySensor.getType() == Sensor.TYPE_ORIENTATION) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            //pitch = atan(event.values[0]/sqrt((Int)(y)^2+z^2));

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
        latitude = location.getLatitude();
        longitude = location.getLongitude();

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
            String transmitID = "5";
            String receiveID = "6";
            double rssi = 0;
            DataFunctions dataFunc = new DataFunctions(getActivity());
            byte buffer[] = new byte[ BUFSIZE ];

            try {
                if(port != null){
                    port.read(buffer, BUFSIZE);
                    serialJSONData = new String(buffer, "UTF-8");
                    try{
                        JSONObject serialJSONObj = new JSONObject(serialJSONData);
                        receiveID = serialJSONObj.getString(RXID);
                        transmitID = serialJSONObj.getString(TXID);
                        rssi = serialJSONObj.getDouble(RSSI);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            } catch
                    (IOException e) {
                System.out.println(e);
            }

            ArrayList<String> data = dataFunc.pulldata(transmitID, rssi, receiveID, x, y, z, latitude,longitude);
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
