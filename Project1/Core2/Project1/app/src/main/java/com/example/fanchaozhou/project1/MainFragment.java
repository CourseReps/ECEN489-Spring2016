package com.example.fanchaozhou.project1;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
public class MainFragment extends Fragment implements SensorEventListener {

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

    public MainFragment(){
        dataList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null){
            dataListAdaptor = new ArrayAdapter<String>(
                    getActivity(),                    //The Current Parent Activity
                    R.layout.single_record,           //The .xml file that contains the textview
                    R.id.list_item_record,            //The id of the textview
                    dataList                          //The source of the data
            );

            dbHandle = new DBAccess(this.getActivity());
            senSensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
            senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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

    @Override
    public void onStart() {
        super.onStart();

        dataListAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            port.close();        //Release the serial port
        } catch (Exception e){
            System.out.println(e);
        }
    }

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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class PullData extends AsyncTask<Void, Void, ArrayList<String>>{
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

            ArrayList<String> data = dataFunc.pulldata(transmitID, rssi, receiveID, x, y, z);
            dataFunc.pushtodb(dbHandle);

            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> data) {
            dataList.add(0,
                    "Transmitter ID: " + data.get(0) + "\n" +
                            "Receiver ID: " + data.get(2) + "\n" +
                            "TimeStamp: " + data.get(5) + "\n" +
                            "RSSI: " + -Double.parseDouble(data.get(1)) + " dBm\n" +
                            "Orientation: " + data.get(3) + "\n" +
                            "Location: " + data.get(4)
            );

            dataListAdaptor.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState == null){
            ListView list = (ListView)rootView.findViewById(R.id.list);  //Find the id of the target ListView
            list.setAdapter(dataListAdaptor);                            //Bind the adaptor to the ListView

            final Button button_clear_all = (Button)rootView.findViewById(R.id.button_clear_all);
            button_clear_all.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dataList.clear();
                    dataListAdaptor.notifyDataSetChanged();
                }
            });

            final Button button_refresh = (Button)rootView.findViewById(R.id.button_refresh);
            button_refresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new PullData().execute();
                }
            });

            final Button button_datatx = (Button)rootView.findViewById(R.id.button_datatx);
            button_datatx.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
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
    
    //tbranyon: Method for sending JSON lines from local DB to server
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
