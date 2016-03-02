#Project1

##Synopsis  


##App Development  

###App/Fragmentation  

###Local Android Database  
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

###USB Connection  

###HTTPpost  

###DATA Collection and Formating  

###IMU and GPS Data  

##Antenna Setup  


##Server Implementation and Data Processing

###Tomcat Server  

###Tomcat DB Interaction - John Lusher II 
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