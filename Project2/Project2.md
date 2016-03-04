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
