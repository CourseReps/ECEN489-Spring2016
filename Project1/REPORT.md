#Project1

##Synopsis 
The objective of this project is to use a handheld android device with an antenna attached in order to interpolate an RF field over a geographical area. We are doing this by GPS location, Recieved Signal Strength, and 3-dimensional orientation of the device. We have a transmission antenna connected to a teensy that is sending datapackets continuously. On the otherside we have a reciever connected to a teensy that has been attached to the android device. This takes the RSSI data and transmitts it via USB to the android device. Once in the android device it is collected and pushed to a local database. After all the data we want has been collected over a large area and we have returned to a wi-fi environment we push all the data from the local database to a server. This is for data processing and so that we can have multiple devices collecting data for the database. 


##App Development  

###App and Fragmentation  
----
This is a single-activity, multi-fragment app. Basically, there's only one activity running in the app, while the three fragments in this app can be swapped in and out, in order to provide different user interfaces on the screen. The three fragments are:
* Main Fragment
* Settings Fragment
* AboutUs Fragment

The main fragment is where the app starts. It extends Fragment Class. The layout of this fragment is in: ``` /res/layout/fragment_main.xml ```. Basically, there are three buttons and a scrollable list(defined as a ListView object) on the main fragment. The format of entries in the scrollable list is defined in ``` /res/layout/single_record.xml ```. The three buttons are "refresh", "clear all" and "connecting to server". The "refresh" button triggers a new data collection, displays the lastest data at the top of the scrollable list(completed by Chaance, Keaton, Paul and Fanchao), and pushes the data to the local SQLite database(completed by Sam). The "clear all" button clears all the existing data on the screen(but does NOT affect the data in the database)(completed by Fanchao). The "connecting to server" button launches a new thread for data transmission(completed by Thomas). 
<br>
The settings fragment is for the user to set the server address. It extends PreferenceFragment Class. The format of this entry(officially called a 'preference') is defined in ``` /res/xml/pref_general.xml ```. In the main fragment, in order to connect to the server, the following code is used to read the server address set by the user:
```javascript
SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
final String serverAddr = sharedPref.getString(getString(R.string.pref_http_key), getString(R.string.pref_http_default));  //Get the server Address
```
The AboutUs fragment is just a brief, scrollable introduction to the project. It extends Fragment Class. The layout is defined in ```/res/layout/fragment_about_us.xml```. And the "about us" file is stored in ``` /res/raw/aboutus ```
<br>
The choices of these three fragments are in the main menu, which will pop out if the top right menu button is clicked. The format of the menu is defined in ``` /res/menu/menu_main.xml ```. And since the menu is set up in the main activity, it's visible in every fragment. The historical records of fragments can also be traced back by clicking on the BACK button on the left bottom corner. 
<br>
The following are the links to the code:
The main activity: https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/Core2/Project1/app/src/main/java/com/example/fanchaozhou/project1/MainActivity.java<br>
The main fragment: https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/Core2/Project1/app/src/main/java/com/example/fanchaozhou/project1/MainFragment.java<br>
The settings fragment: https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/Core2/Project1/app/src/main/java/com/example/fanchaozhou/project1/SettingsFragment.java<br>
The AboutUs fragment: https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/Core2/Project1/app/src/main/java/com/example/fanchaozhou/project1/AboutUsFragment.java<br>

###Local Android Database 
----
The local database stores received data locally on the android device using an SQLite database. This allows the device to wait until it connects to wifi and is ready to push the data to the server. The database is implemented with the DBAccess class, which extends the SQLiteOpenHelper class. A connection to the database is established when an object of the class is instantiated. This is done via the constructor, which will create or open a database with the name passed to it. When the class is instantiated, it will automatically create a data table in the database using the SQLiteOpenHelper method, onCreate(SQLiteDatabase db). The columns of the table include:
* Xbee ID - the ID of the Xbee device transmitting the signal
* Device ID - the ID of the Android device receiving the transmitted data
* RSSI - the strength of the received signal
* Latitude - the Latitude coordinate of the Android device at the time it receives the signal
* Longitude - the Latitude coordinate of the Android device at the time it receives the signal
* Yaw - The yaw of the Android device at the time it receives the signal
* Pitch - The pitch of the Android device at the time it receives the signal
* Roll - The roll of the Android device at the time it receives the signal
* Sample Date - The time and date at which the signal is received
* Sent - An internal field to indicate whether or not the data has been sent to the server

DBAccess contains methods to store data, retreive data that has not yet been sent to the server, retreive all stored data, and clear the table. These methods are listed and explained below:
```javascript
public boolean addData(JSONObject data)
```
This method accepts a JSON object parameter. The JSON object should include all of the data to be stored in the database, excluding the sent field. The method parses the object and uses the ContentValues and SQLiteDatabase classes to insert the data into the database. If the add is successful, the method will return true.
```javascript
public JSONArray getUnsentData()
```
This method uses the SQLiteDatabase and Cursor classes to perform a query on the database for all rows that are not marked as sent. It then stores each row in a JSON object (again, excluding the sent field). Each JSON object is then stored in a JSON array, which is returned by the method. The rows in the database that were retreived are marked as sent with an sql update.
```javascript
public JSONArray getAllData()
```
This method functions the same as getUnsentData, except it queries for all data rather than just the unsent data.
```javascript
public void clearData()
```
clearData uses the SQLiteDatabase method delete() to delete all rows from the table.

The full class can be found here: https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/Core2/Project1/app/src/main/java/com/example/fanchaozhou/project1/DBAccess.java

###USB Connection  
----

###HTTPpost  
----
The HTTP POST routine takes a given JSONObject (which contains a line of data from the local database) and pushes it to the remote server using an HTTP POST request. The user must specify the remote server URL in the app's settings. This routine was provided as a single function placed in the MainFragment code and was called from a button click handler. This button was provided for the user to specify when to push all unsent data to the remote database. On a click event, the button handler obtains an array of all unsent database lines and loops through and sends each one to the server. The POST method is launched in a separate thread using Java's standard Thread object.  

Send data method, placed in MainFragment:
```javascript
protected void sendHTTPdata(JSONObject json, String serverAddress)
```



###DATA Collection and Formatting  
----
This section of the app collects all the data from the different parts of the app and formats it into a JSON object with the correct data types.
```javascript
private static String transmitID = "";
    private static float RSSI = 0;
    private static String receiveID = "";
    private static double latitude = 0;
    private static double longitude = 0;
    private static DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static float yaw = 0;
    private static float pitch = 0;
    private static float roll = 0;
```
```javascript
pulldata(transmitID, RSSI, receiveID, orient);
            JSONObject dbdata = new JSONObject();
            dbdata.put("XbeeID", transmitID);
            dbdata.put("RSSI", RSSI);
            dbdata.put("DeviceID", receiveID);
            dbdata.put("Latitude", latitude);
            dbdata.put("Longitude", longitude);
            dbdata.put("Yaw", yaw);
            dbdata.put("Pitch", pitch);
            dbdata.put("Roll", roll);
            dbdata.put("SampleDate", timestampst);
            data2.addData(dbdata);
```
It is split up into two different functions. A pulldata function and a pushtodb function. The pull data function returns an array of strings in order to easily display updated information on the user interface. The pushtodb function creates a JSON object and pushes it to the local database with the correct data types.

###IMU and GPS Data  

In this section, we will discuss the function that will call the GPS (Global Positioning System) Data and IMU (Inertial Measurement Unit) data. Android platforms have the data GPS and IMU (under SensorManager) within the System Fuctions. As you can see below is the class written noted as GPSTracker.java.
From this class, the latitude and longitude was able to be pulled and integrated into the main core app. 

```
```java
package com.example.chaance.gpstracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000, 1, this);

    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "New Latitude: " + location.getLatitude()    // <------- This data was pulled
                + "New Longitude: " + location.getLongitude();	  // <------- This data was pulled

        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
```
And from the AndroidMansifest.xml file that was integrated into the core app, we needed provide permission for GPS to be accessed.
```
	<uses-permission android:name="android.permission.INTERNET" />
    	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
----
##Antenna Setup  

The configuration of XBEE

* The function set should be XBee Pro 802.15.4 10ef.
* The PAN ID and the channel of two XBees should be the same.
* We set both XBee as end device.
* Radio module operating modes: 1. Application Transparent (AT) operating mode. 2. API operating mode. 3. API escaped operating mode 
(We set API mode to API escaped operating mode) : API escaped operating mode (AP = 2) works similarly to API mode. The only 		difference is that when working in API escaped mode, some bytes of the API frame specific data must be escaped.
* Address: 
```javascript
	Destination Address High of Transmitter: 0013A200
	Destination Address Low of Transmitter: 40C556C5
	Destination Address High of Receiver: 0013A200
	Destination Address Low of Receiver: 40C556CE
	16-bit Source Address of Receiver: 0817
	16-bit Source Address of Receiver: default
```
Data
```javascript
{
ID of Transmitter: 78(Defined in Transmitter)
ID of Receiver: 87(Defined in Receiver)
Random number: 0 ~ 255
RSSI
} 
```
The communication setup

```javascript
void setup() {
	  // put your setup code here, to run once:
	  Serial.begin(baud);
	  Serial3.begin(baud);
	  xbee.setSerial(Serial3);
	  pinMode(ledPin, OUTPUT);
}
```	
XBee- arduino API:
https://github.com/leoyyx2009/xbee-arduino
* Send data:
```javascript
  Tx16Request Tx16 = Tx16Request(0x0817, payload, sizeof(payload));
  xbee.send(Tx16);
```
* Receive data:
```javascript
  xbee.readPacket();
  xbee.getResponse().getRx16Response(rx16);
```
* Source code of transmitter:
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/XBEE/src%20of%20TX/TX.ino
* Source code of receiver:
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/XBEE/src%20of%20RX/RX.ino

##Server Implementation and Data Processing

###Tomcat Server  
----

###Tomcat DB Interaction - John Lusher II 
----
For this portion of the project I focused on the development of a Java class library that could be called by a Java application and would:
* Connect to a MySQL database given a host address (i.e. localhost)
* Enable adding of data to the SQL database by just passing a JSON formatted string
* Query data from the database by several member functions and return a JSON formatted string.
* Provide optional connectivity to a Google Fusion Table.

To begin development, I first installed MySQL on a Linux server that I have and then I created a new database (ECEN_RF_Fields) and a table in the database (RF_Fields).   In this tabled I created all of the columns that corresponded for the data that we wanted to collect per sample.  I then, using the JDBC API, created the functions to _connect, add, and query the database._  I created a class that would be the data structure for the JSON elements and for holding and manipulation of data within the calling Java applications.  This class had the following members (in pseudo code):

* Sample Number – Representing the record number in the databse
* Xbee ID – Representing an identification number for a particular Xbee radio
* Device ID – Representing an identification number for a particular phone or tablet retrieving the data
* RSSI – Signal strength of the sample in dBm
* Latitude – The Latitude position of the sample (in decimal degrees)
* Longitude – The Longitude position of the sample (in decimal degrees)
* Yaw – The orientation of the transmitting device (in degrees)
* Pitch – The orientation of the transmitting device (in degrees)
* Roll – The orientation of the transmitting device (in degrees)
* Sample Date – The timestamp of when the sample was acquired

This class libary was shared with the Android Application team such that the JSON data and expected fields would be consistent between the application and the server.

Overall the development effort for this class library went smoothly and a test class was created to exercise all functions in their various modes.  Random sample data was generated and added to the database as well as queried.  A NUC from Dr. Huff was obtained and I proceed to install Ubuntu Server v15.10 along with Tomcat 8 and a MySQL database.  The server was tested with the test application and it performed as expected. 

Integration of the Java class library with the interpolation algorithm was fairly smooth and only required minimal debugging.  

Below are a few examples of how to use the functions created in this class libary:
```javascript
// Connect to localhost database and also the Google Fusion Table
RFFieldDatabase.ConnectToDatabase("localhost" , true);
```

```javascript
// Add new data via a JSON string
RFFieldDatabase.AddNewEntry(jsondata, true);
```

```javascript
// Query a range of Lat/Long sample data
jsondata = RFFieldDatabase.ListDataByGeoArea(30.0F, -97.0F, 31.0F, -96.0F);
```

For more detail on the API of the class please go here:<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/API_SQLDatabase.md <br>

The source files for the class libary are:<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/WebApp/RFDatabase/src/RFFieldSQLDatabase.java <br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/WebApp/RFDatabase/src/RFData.java <br>

The library is tested using these files: <br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/WebApp/RFDatabase/src/Main.java <br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/WebApp/RFDatabase/src/LatLong.java <br>


###Interpolation Algorithm
----

The source code for the Algorithm and Mapping Visualization is under <br>
https://github.com/CourseReps/ECEN489-Spring2016/tree/master/Students/akashatnitr/Project1/ECEN689Project1 <br>

The major parts of the project done by me were
* GUI based on Applet and Map library
* Algorithms to estimate the RSSI at any distance from the transmitter
* Getting the data from SQL database and create RFData objects  and pushing into the hashmap
* Distance matrix of all points and finding the 3 nearest distance points from the point of interest and finding the RSSI
* Point markers at each point with color shadding for RSSI values
<br> <br>
The UI has components 
* Latitude - Enter the latitute for which the RSSI values need to be computed
* Longitude - Enter the longitude for which the RSSI values need to be computed
* The text label show the location and RSSI values
* You have to add location for transmitter by default it takes the EIC location.
