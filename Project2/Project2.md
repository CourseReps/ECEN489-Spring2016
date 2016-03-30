# Project 2b  
Technical work needs to be integrated/polished/finished by Wednesday, 30 March at 3:50PM.  
Any individual work that is done outside of the app should be finished no later than Monday night to leave time for integration and testing.

## Individual Tasks
* Project Lead/Integration and Testing - Thomas
* OAuth/Fusion Tables/Maps - John
* ~~Debug screen orientation change crash - Paul~~ Done.
* Debug back button crash - Kyle
* ~~Debug app exit crash - Yanxiang~~ Done.
* ~~Performance improvements (fix crashes due to slowness) -~~ Fixed?
* ~~Implement dynamic data collection based on user settings - Fanchao~~ Done.
* ~~Stop recording data when orientation error is outside of tolerance - Sam~~ Done.
* Code organization/cleaning, UI polishing - Keaton
* ~~Get internal RSSI - Fanchao~~ Done.

-----------------------------------------------------
-----------------------------------------------------


#Project 2  

Due Friday 3/11/16

###Overview  
* Set up Android app to be used with Teensy/XBee combo to collect signal strength data
* Create server image that can be downloaded and set up to work with app
* Phone will now push to user's Fusion Table
* Set up NUC Access Point
* NUC server will push to Fusion Table
* Should be able to do this with many devices in parallel

###Phone App  
* Get internal RSS (new)  
* Get XBee RSS  
* Get GPS location  
* Get IMU data  
* Concatenate into JSON  
* Send to Fusion Table, plot on map (new)  
* Send to server, server sends to Fusion Table, plot on map (new)  

###Data Acquisition  
* Source 1 - XBee-based sensor tool with NUC or Android Phone  
* Source 2 - Teensy-controlled antenna for Access Point  

###Action Items  
* Previous app functionality needs to be solidified  
    * Server needs to receive data  
    * GPS data needs to be fixed  
    * UI needs to be polished  
    * Add visual feedback for orientation  
* Add mapping/Fusion Table functionality on server and phone side  

##Phone App Storyboarding Notes  
On open, "create table if not exists" (Fusion Table) (Use user's Google account)  

Screens:
* About
* Settings
    * Selection of data fields desired for live display
        * GPS
        * Local RSS
        * External RSS
        * Gyro
        * Magnetometer
        * Compass
    * Server settings
        * Send rate (fixed values)
        * Server Address
        * Port Number
        * Source 1 (AP) MAC Address
        * Source 2 (XBee) ID
    * Option to initialize/create table
        * Creates if not exists
        * If exists, prompts user if it is desired to clear the table
    * Option to specify table name (default "Test")
    * Allow user to specify tolerable deviation from perfect orientation in theta and phi directions
* Data
    * Displays on open
    * Shows live update of sensor strength and orientation
    * Graphical tool to help user determine if phone is oriented properly (e.g. "bubble level"-style graphic with indicator)
    * Orientation use on/off
    * Measures RSS when orientation is within some tolerance
    * Button for manual collect/send, Enable switch for turning on auto stream
    * Indicate connection status with WiFi and XBee sensor
        * Shows device ID of XBee/AP
* Display
    * Data from Fusion Table (tabs to switch between)
        * As map
        * As table
    * Allow user to select from Fusion Tables in dropdown

![project2photo1](https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/keabro/project2/20160304_150621.jpg)
![project2photo2](https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/keabro/project2/20160304_154712.jpg)
![project2photo3](https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/keabro/project2/20160304_160047.jpg)

##Individual Tasks  
* Doxygen - Keaton
* Graphic (orientation) - Sam, Chaance
* Fusion map - John
* UI - Kyle
* Data collection - Thomas
* IMU, gps etc. - Akash
* Wireframe then UI - Paul
* threading - fanchao
* asset manager - yangxiang
