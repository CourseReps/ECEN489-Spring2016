#Project2

##Synopsis 
The objective of this project is to use a handheld android device with an antenna attached in order to interpolate an RF field over a geographical area. We are doing this by GPS location, Recieved Signal Strength, and 3-dimensional orientation of the device. We have a transmission antenna connected to a teensy that is sending datapackets continuously. On the otherside we have a reciever connected to a teensy that has been attached to the android device. This takes the RSSI data and transmitts it via USB to the android device. Once in the android device it is collected and pushed to a local database. After all the data we want has been collected over a large area and we have returned to a wi-fi environment we push all the data from the local database to a server. This is for data processing and so that we can have multiple devices collecting data for the database. 


###Fusion Table / Database Integration and GoogleMap Display - John Lusher II
----
For this portion of the application I focused on the development of a fragment (MapsViewFragment) that would:
* Connect to the users Google Account and create and use a Fusion Table to store the collected data.
* Enable adding of data to the SQL database by just passing a RF Data Member
* Query data from the database by several member functions and display the results of the query on a GoogleMap.

For this development I ran into issues with Google OAuth 2.0 and could not in the time frame given for the project sucessfully get the fragement working. Therefore, after dicussion with Dr. Huff, it was decided to utilize a MySQL server for Project 2 with the idea that a solution to the OAuth tokens would be determined in the future.

To begin development, I utlized the same MySQL on a Linux server that I had created for Project 1.  I used the database (ECEN_RF_Fields) and a table in the database (RF_Fields).  In this tabled I utilized all of the columns that corresponded for the data that we wanted to collect per sample.  I then, using the JDBC API, created the functions  to _connect, add, and query the database._  I created a class that would be the data structure for the for holding and data within the calling Java functions.  This class had the following members (in pseudo code):

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

This class libary was shared with the Android Application team such that the data and expected fields would be consistent between the other fragments.  Also developed was a Lat/Long class that would compute the Lat/Long from a given refernece position along with a Bearing and Distance.  This was used to compute the region for the query command to pull data given a radius from a given location on the map (i.e. Map OnClick).  Asynchronous tasks were created for the adding of data and the quering of data so that the fragment would not cause the application to stall or lockup due to connection issues.  

Overall the development effort for this class library went smoothly (for the MySQL version) and a test class was created to exercise all functions in their various modes.  Random sample data was generated and added to the database as well as queried.  

Initial integration of the fragment witht he project took some debugging and modification of the type of frament being used with the GoogleMap.  Once this integration hurdle was delt with the intergration with the rest of the project was fairly smooth with only required minimal debugging, mostly dealing with the format of the sample date.

Below are a few examples of how to use the functions created in this class libary:
```javascript
// Update the table given a Lat/Long and Radius (in feet)
public void UpdateMap(float RefLatitude, float RefLongitude, float Radius);
```

```javascript
// Async class to addd data to the MySQL database
private class SaveRFData extends AsyncTask<String, Void, String>
```
The source files for the fragment are:<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project2/Project2/app/src/main/java/com/example/fanchaozhou/project1/MapsViewFragment.java<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project2/Project2/app/src/main/java/com/example/fanchaozhou/project1/RFData.java<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project2/Project2/app/src/main/java/com/example/fanchaozhou/project1/LatLong.java<br>
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project2/Project2/app/src/main/java/com/example/fanchaozhou/project1/RFFieldSQLDatabase.java<br>


The remaining portion of my time was spent working on the OAuth debugging.  I had eventual success in getting data added to a user's Fusion Table on an Android device. This took examinging many example projects and meshing together various techniques to get this to work.  The source files for the OAuth test project that finally worked are located at:<br>
https://github.com/CourseReps/ECEN489-Spring2016/tree/master/Students/johnlusher/TestOauthLogin<br>


