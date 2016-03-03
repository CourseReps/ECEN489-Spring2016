package com.example.fanchaozhou.project1;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
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
public class MainFragment extends Fragment {

    private ArrayList<String> dataList = new ArrayList<>();
    private ArrayAdapter<String> dataListAdaptor;
    private DBAccess dbHandle;
    private UsbSerialPort port;
    private int HTTP_SEND_STATUS = 0;
    public final static int BUFSIZE = 64;
    public final static int BAUDRATE = 9600;

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
                port.setParameters(BAUDRATE, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
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
            port.close();
        } catch (Exception e){
            System.out.println(e);
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
                    // TODO:Add code for DATA COLLECTION(From IMU, GPS and Serial) and PUSHING DATA INTO LOCAL DATABASE HERE
                    //I've already added them below for testing, but I don't know if I did this correctly.
                    byte buffer[] = new byte[ BUFSIZE ];
                    String transmitID = null;
                    try {
                        port.read(buffer, BUFSIZE);
                        transmitID = new String(buffer, "UTF-8");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    DataFunctions dataFunc = new DataFunctions(getActivity());
                    //String transmitID = "txid";
                    float RSSI = 6;
                    String receiveID = "rxid";
                    float[] imu = {0,1,2};
                    ArrayList<String> data = dataFunc.pulldata(transmitID, RSSI,receiveID,imu);
                    dataFunc.pushtodb(dbHandle);
                    dataList.add(0,
                            "Transmitter ID: " + data.get(0) + "\n" +
                                  //  "Receiver ID: " + data.get(2) + "\n" +
                                    "TimeStamp: " + data.get(5) + "\n" +
                                  //  "RSSI: " + data.get(1) + "\n" +
                                    "Orientation: " + data.get(3) + "\n" +
                                    "Location: " + data.get(4)
                    );

                    dataListAdaptor.notifyDataSetChanged();
                }
            });
            final Button button_datatx = (Button)rootView.findViewById(R.id.button_datatx);
            button_datatx.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    final String serverAddr = sharedPref.getString(getString(R.string.pref_http_key), getString(R.string.pref_http_default));  //Get the server Address
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
