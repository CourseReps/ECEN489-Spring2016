/**
 * Created by Kyle Sparrow on 2/19/2016.
 * last updated: 3/3/2016
 * Project 1 for ECEN 489
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ArrayList<ArrayList<String>> entries;
    private boolean connection = false;
    private boolean entry = false;
    private int test =0;
    private int sampleNumber =0;

    public FirstServlet()
    {
        super();
        entries = new ArrayList<>();
    }


    //-------------------------doGet and doPost for Project 1--------------------------------------
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("test");
        response.setContentType("text/html");
        PrintWriter os = response.getWriter();
        os.println("<h1>This is Our Project 1 Server</h1>");
        os.println("<table border=\"1\">");
        os.println("<tr>");
        os.println("<th>Sample Number</th><th>XBee</th><th>Device</th><th>RSSI</th><th>Latitude</th><th>Longitude</th><th>Yaw</th><th>Pitch</th><th>Roll</th><th>Timestamp</th></tr>");
        for(ArrayList<String> entry : entries)
        {
            String sampleCount = entry.get(0);                       //could delete this entirely, leave it for clarity
            String DeviceID = entry.get(1);
            String RSSI = entry.get(2);
            String XbeeID = entry.get(3);
            String Latitude= entry.get(4);
            String Longitude = entry.get(5);
            String Yaw = entry.get(6);
            String Pitch = entry.get(7);
            String Roll = entry.get(8);
            String Timestamp = entry.get(9);

            os.println("<tr>");
            os.println("<td>" + sampleCount + "</td>");
            os.println("<td>" + XbeeID  + "</td>");
            os.println("<td>" + DeviceID  + "</td>");
            os.println("<td>" + RSSI + "</td>");
            os.println("<td>" + Latitude + "</td>");
            os.println("<td>" + Longitude + "</td>");
            os.println("<td>" + Yaw + "</td>");
            os.println("<td>" + Pitch + "</td>");
            os.println("<td>" + Roll + "</td>");
            os.println("<td>" + Timestamp + "</td>");
            os.println("</tr>");
        }
        os.println("</table>");
        os.println("<p>test: "+test+"</p>");

        os.println("<p>connection status: "+connection+"</p>");
        os.println("<p>entry status: "+entry+"</p>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        test =1;
        System.out.println("POST received");
        BufferedReader is = request.getReader();

        /* convert input string from JSON */
        JSONObject receiveJson = null;

        try {
			/*Here is where we could add checks on the data to ensure we are reading a json,
			but there is no need in this current application. */
            receiveJson = new JSONObject(is.readLine());
        } catch (JSONException e) {
            e.printStackTrace();
        }
		/* each GET command from phone will eb a single json, so we cna close now */

        is.close();

        /* This section is for reformatting the JSON string we receive...
        ////////////////////////////////////////////////////////////////////////////////////////*/
        String tempdev = null;
        int dev = 0;

        String tempxb = null;
        int xb = 0;

        sampleNumber++;
        test = 2;
        try {                                               //This TRy Block is where this always fails
            tempdev = receiveJson.getString("DeviceID");
            dev = Integer.parseInt(tempdev);                //BEWARE of ID's that are Alphanumeric!
            receiveJson.remove("DeviceID");
            receiveJson.put("DeviceID", dev);
            test=4;
            tempxb = receiveJson.getString("XbeeID");
            xb = Integer.parseInt(tempxb);                  //BEWARE of ID's that are Alphanumeric!
            receiveJson.remove("XbeeID");
            receiveJson.put("XbeeID", xb);

            receiveJson.put("SampleNumber", sampleNumber);
            test =3;
        } catch (JSONException e) {
            e.printStackTrace();                            // NO IDEA WHERE THIS EVEN PRINTS OUT
        }

        /*here is where I need to call John's functions, need to double check with him
		to ensure these are the correct calls and arguments...
		TEST JSON AND testString used for debugging,
        ///////////////////////////////////////////////////////////////////////////////////////////////*/
        // TEST JSON
        JSONObject testJson = null;
        try {
            testJson.put("SampleNumber", -1);
            testJson.put("XbeeID", 456);
            testJson.put("DeviceID", 1234);
            testJson.put("RSSI", 32.189316);
            testJson.put("Latitude", 30.618547);
            testJson.put("Longitude", -96.34142);
            testJson.put("Yaw", 104.00709);
            testJson.put("Pitch", 256.773);
            testJson.put("Roll", 22.144665);
            testJson.put("SampleDate","Feb 29, 2016 2:37:38 PM");
            test =4;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        test =5;

        String testString = "{\"SampleNumber\":-1,\"XbeeID\":456,\"DeviceID\":1234,\"RSSI\":32.189316,\"Latitude\":30.618547,\"Longitude\":-96.34142,\"Yaw\":104.00709,\"Pitch\":256.773,\"Roll\":22.144665,\"SampleDate\":\"Feb 29, 2016 2:37:37 PM\"}";
        RFFieldSQLDatabase  db = new RFFieldSQLDatabase();
        connection = db.ConnectToDatabase("localhost");
        entry = db.AddNewEntry(receiveJson.toString());             //Make sure to switch the entry when debugging
        test=6;
        ////////////////////////////////////////////////////////////////////////////////////////*/


        /* This section is for display..not neededJSONObject receiveJson = null;, will be helpful for debugging
		   will remove if array memory allocation cause significant overhead with
		   many samples...no idea if that is even a real problem here...*/


        String sampleNumberString = Integer.toString(sampleNumber);
        String deviceID = null;
        String rssi = null;
        String xbeeID= null;
        String latitude= null;
        String longitude= null;
        String yaw= null;
        String pitch= null;
        String roll= null;
        String date= null;

        ArrayList<String> thisEntry = new ArrayList<>();

        try {
            deviceID = receiveJson.getString("DeviceID");                   //getString() will coerce incoming data
            rssi = receiveJson.getString("RSSI");
            xbeeID = receiveJson.getString("XbeeID");
            latitude = receiveJson.getString("Latitude");
            longitude = receiveJson.getString("Longitude");
            yaw = receiveJson.getString("Yaw");
            pitch = receiveJson.getString("Pitch");
            roll = receiveJson.getString("Roll");
            date = receiveJson.getString("SampleDate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        thisEntry.add(sampleNumberString);
        thisEntry.add(xbeeID );
        thisEntry.add(deviceID );
        thisEntry.add(rssi);
        thisEntry.add(latitude);
        thisEntry.add(longitude);
        thisEntry.add(yaw);
        thisEntry.add(pitch);
        thisEntry.add(roll);
        thisEntry.add(date);

        entries.add(thisEntry);                 // Adding this array of strings into anther array
    }

}