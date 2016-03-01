#Project1

##Synopsis  


##App development  

###App/Fragmentation  

###Local Database  

###USB connection  

###HTTPpost  

###DATA collection and formating  

###IMU and GPS Data  

##Antenna setup  


##Server Implementation  and Data processing

###Tomcat Server  

###Tomcat DB interaction - John Lusher II 
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
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/API_SQLDatabase.md

The source files for the class libary are:
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/WebApp/RFDatabase/src/RFFieldSQLDatabase.java
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/WebApp/RFDatabase/src/RFData.java

The library is tested using these files:
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/WebApp/RFDatabase/src/Main.java
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/WebApp/RFDatabase/src/LatLong.java


###Interpolation Algorithm
