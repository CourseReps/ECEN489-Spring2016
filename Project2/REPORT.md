#Project 2

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


###Data Collection/Integration/Debugging - Thomas Branyon  

This section of the codebase started off as a class that would run a background loop to collect data. The DataCollector class was created and implemented the 'Runnable' interface to allow it to be launched in a separate thread. As the task moved forward, it became clear that it was more practical to read sensors within the MainFragment class. DataCollector was reduced to a data container, much like a C-style "struct". The data members are both public and static, allowing all parts of the app to access the most current data at any time through a local instance of the DataCollector class.
Other small modifications were made to RFData.java (added a method to populate the class's fields from a JSONObject), DataFunctions.java (modified to obtain latest data from DataCollector fields), and MainFragment (various).
In MainFragment, I pulled in some of John's methods and used them in writing the button click handler for the "Send to Server" button (titled "Connect to Server" in the app, or "button_datatx" in the MainFragment source code). The send-to-server routine is handled in the "doInBackground" method of the private class SaveRFData, within MainFragment. Most of this code was John's, with some modifications to retrieve data from the local db and then iterate through these entries and send each one to the remote SQL server.  

For the "2b" part of this project, I tried to lay out the necessary remaining tasks and ensure team members were assigned to handle each one, coordinate communication and problem-solving efforts, manage the code contributions from different members of the team, and test for errors and help point to potential solutions where needed. I did less coding for this part, but did make a change in MainFragment to prevent the app from doubly allocating resources for a fragment which was causing performance issues.

### Alignment Fragment Implementation - Sam Shore, Chaance Graves
----
This portion of Project 2 focused on the Alignment fragment. Its purpose is to serve as a visual representation that the user can refer to in order to know if they're in the tolerable range for data collecting and RF mapping purposes. We originally were proposed two methods of approach to develop the visual aid tool within the Android API framework. We determined that we would use OpenGL and how to use to a modificated version of Gyroscope Explorer. After time, the Gyroscope Explorer modification was phased out for integration into the main app for a more simple Square that has a real-time gradient color response defined by the parameters of the x,y,z values from the IMU readings.

The graphic was creating using the OpenGL ES 2.0 API. The OpenGL library is intended for creating 3D graphics, which was the original intention. However this turned out to be very difficult, so I decided to create a 2D graphic. I stuck with the OpenGL API since all of the resources were already set up. The graphic utilizes: a GLSurfaceView to actually display the image and instantiate the renderer, a renderer which instantiates the square, sets up a model view projection matrix (so that the square is square regardless of the viewport), and loads the shader code and actually draws the graphic, and a Square class which lays out the image's geometry and shader code and includes the routine to draw the shape. The GLSurfaceView is included in the alignment fragment's .xml file. The rest of the alignment graphic is implemented in the alignment fragment class. The onCreate method starts a thread to periodically update the textviews showing the yaw, pitch, and roll, as well as run an updateGraphic routine. This routine pulls orientation preferences set by the user and checks the IMU readings against these values to determine if the phone is within the set tolerances. The color of the square is determined by the orientation of the phone. If it is within the set tolerances, the square is green. If the phone is just outside the set tolerances within a certain margin, the square does a gradient from green to red. Once the phone is outside of this margin, the square is red. Once the phone's orientation is checked against these preferences, the color of the square is set and it is redrawn. 

The alignment fragment also pauses automatic data collection if the phone is outside of the tolerances and the user has checked the checkbox to enable this functionality. If the checkbox is checked, the updateGraphic method will update a static boolean variable depending on whether or not the phone is in alignment. Then, in the thread in the main fragment used for automatic data collection, an if statement was added so that data is only collected if the variable is set to 'true'. Finally, toast notifications were added to alert the user when automatic data collection is paused or resumed as a result of a change in orientation.

### Asset management - Yanxiang
----
The purpose of this portion is to monitor the information provided in setting fragment. And it can maintain and change the configuration to default. Android SharedPreferences allows us to store private primitive data in the form of key-value pair. The data will be stored as XML file in shared_prefs folder under DATA/data/[application] directory. The DATA folder can be obtained by calling Environment.getDataDirectory(). 

The basic functionality is that it can maintain the changes. All changes will be automatically stored as XML file. When you reopen the app you can see the changes you made last time. In order to set everything to default, I add a reset button. Preference.OnPreferenceClickListener is an interface for a callback to be invoked when a preference is clicked. After clicking this button, the checkbox will be unchecked. The summary of the field and the editText of the UI will be changed to be default settings. 

### IMU Sensor and GPS - Akash
---
This component of the project was to collect the IMU sensor (Accelerometer, Gyro, Magnetometer )data and display them. The display refreshed every 10ms. GPS had a button and on press of button, it takes 30-40secs to get location and send coordinates to the program. It has observer pattern which notifies once the GPS values are read. 
The code is available <br> https://github.com/CourseReps/ECEN489-Spring2016/tree/master/Students/akashatnitr/Project2/

### UI/UX and Wireframe - Paul Crouther/Kyle Sparrow
---
The purpose of this component is the user interface and development of the visuals, the buttons, and the user experience of the app. The main components of the app are the windows from the MainFragment, SettingsFragment, AboutUsFragment, and MapViewFragment.
