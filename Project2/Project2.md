#Project 2  

Due Friday 3/11/16

##Overview  
* Set up Android app to be used with Teensy/XBee combo to collect signal strength data  
* Create server image that can be downloaded and set up to work with app  

* Phone will now push to user's Fusion Table  
* Set up NUC Access Point  
* NUC server will push to Fusion Table  
* Should be able to do this with many devices in parallel  

##Phone App  
* Get internal RSS (new)  
* Get XBee RSS  
* Get GPS location  
* Get IMU data  
* Concatenate into JSON  
* Send to Fusion Table, plot on map  
* Send to server, server sends to Fusion Table, plot on map    

##Data Acquisition  
* Source 1 - XBee-based sensor tool with NUC or Android Phone  
* Source 2 - Teensy-controlled antenna for Access Point  

##Action Items  
* Previous app functionality needs to be solidified  
    * Server needs to receive data  
    * GPS data needs to be fixed  
    * UI needs to be polished  
    * Add visual feedback for orientation  
* Add mapping/Fusion Table functionality on server and phone side  

