#Project 3 - RF Signal Strength Mapping Tool - John Lusher II

##Synopsis 
This project will take the captured signal strength from a Digi International XBee device, the current geographical location (Lat/Long), 
the orientation of the antenna and current time/date and store the store the data into a MySQL database located on a remote server.  This application will be deployed on an Android device, such as the Nexus 6P.  The application shall be able to store the sampled data to a SQL database on a remote server (MySQL).  Also, this application will display the current RSSI, location, and orientation data.  The application will be stable and easy to use.  
 
### Improvements from previous projects (Project 1 and Project 2)

First, the application was recreated from a new project whereas all legacy modules and code were rebuilt as needed and issues corrected.  A new GUI was created that more closely
matched what I felt was the intended use case.  This project focused on the application being stable and able to be used in a research application immediately.  The issues with the application
causing itself to crash which rotated, paused, shutdown, or consuming device resoures was examined an a streamlined approach was taken to ensure that the application did not behave in this way.  
All code was commented and was written to be easy to make modification and additions to it quickly.  

There are two main sections of the application, the main view fragment and the GoogleMap Display with the RF data overlayed.  

### Main View Fragment (Data View)
----
This view was designed to provide the user of the application a current feedback of the signal strength (Cellular or Xbee), the current location of the device (Lat/Long), and the Roll, Pitch, and Yaw of
the device.  Upon selection by the user, by a pushbutton, the user can cause the current data to be stored on the MySQL server and later processing. 

This section utilizes  the following APIs:

*MySQL Connector
Provides interface to connect to MySQL database and run SQL commands to insert new records and query records.

*USB Serial Connector 
Provides USB to Serial interface for Android Applications.  Allows application to open port and create a listener to receive streaming input data, in this application the RSSI data.

*RF Field SQL Database
Class to provide simple interface to SQL database being provided functions to connect and add new data based upon the RF Data class.

*RF Data Class
Simply the class in which all data for a record is stored, this includes the IMU data, RSSI, sample date, Lat/Long

*Telephony Manger / Phone State
Provides the ability to get the RSSI of the various bands available to the cellular radio.

*Location Manager
Provides the ability to get the geo-location of the device.  

*Sensor Manager
Provides the ability to get the sensor (IMU) information for the device.  In this case we are interested in the Yaw, Pitch, and Roll.

The development was fairly smooth, starting from scratch, however, as has been typical with learning Android application development, most examples that one uses as a template are either 
incomplete or incorrect.  Much time was spent developing an application that was solid and would not crash when performing basic functions.

Data can be stored or any MySQL server, however for development I utlized the same MySQL on a Linux server that I had created for Project 1.  I used the database (ECEN_RF_Fields) and a table in the database (RF_Fields).  In this tabled I utilized all of the columns that corresponded for the data that we wanted to collect per sample.  I then, using the JDBC API, created the functions  to _connect, add, and query the database._  I created a class that would be the data structure for the for holding and data within the calling Java functions.  This class had the following members (in pseudo code):

* Sample Number – Representing the record number in the databse
* Xbee ID – Representing an identification number for a particular Xbee radio
* Device ID – Representing an identification number for a particular phone or tablet retrieving the data
* RSSI – Signal strength of the sample in dBm
* Latitude – The Latitude position of the sample (in decimal degrees)
* Longitude – The Longitude position of the sample (in decimal degrees)
* Yaw – The orientation of the transmitting device (in degrees)
* Pitch – The orientation of the transmitting device (in degrees)
* Roll – The orientation of the transmitting device (in degrees)
* Cellular Signal - The JSON object representing the signal strength of the cellular radio
* Sample Date – The timestamp of when the sample was acquired

The source files for the fragment are:<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/johnlusher/Project_3/RFSignalMap/app/src/main/java/edu/tamu/rfsignalmap/MainActivityFragment.java<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/johnlusher/Project_3/RFSignalMap/app/src/main/java/edu/tamu/rfsignalmap/RFData.java<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/johnlusher/Project_3/RFSignalMap/app/src/main/java/edu/tamu/rfsignalmap/RFFieldSQLDatabase.java<br>


### GoogleMap Display
----
The purpose of this portion is to provide the user feedback of the collected data on the server based upon the selected region.  

This section utilizes  the following APIs:

*Google Map API
Googles’ API for interfacing their Google Map and integrate into an Android Application

*Latitude and Longitude
Class to generate latitude and longitude based upon a starting location and given a distance and bearing.  This is used for determine an area for the map viewer to pull data from the SQL database.

*RF Data Class
Simply the class in which all data for a record is stored, this includes the IMU data, RSSI, sample date, Lat/Long


Issues again were discovered with integrating the Map Fragment with the application in such a way as to be stable and not crash the application.  End the end this was a simple fix
involving the way the fragment was initialized.  


The source files for the fragment are:<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/johnlusher/Project_3/RFSignalMap/app/src/main/java/edu/tamu/rfsignalmap/MapsViewFragment.java<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/johnlusher/Project_3/RFSignalMap/app/src/main/java/edu/tamu/rfsignalmap/RFData.java<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/johnlusher/Project_3/RFSignalMap/app/src/main/java/edu/tamu/rfsignalmap/LatLong.java<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/johnlusher/Project_3/RFSignalMap/app/src/main/java/edu/tamu/rfsignalmap/RFFieldSQLDatabase.java<br>


### Continuing development effort
Current I have been focused on implmenting the HackRF One into the application for further enhancement and use.  Also, further development of a reporting and analysis tool to read the data
from the datbase and build a heatmap is being developed.