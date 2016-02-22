##Use this page to document interface definitions  

###Datastructure


###XBee to Microcontroller  
JSON

```
{
  "Transmit ID":"Device ID",
  "RSSI":"Decibels",
  "Receive ID":"Device ID"
}
```

###Microcontroller to Android  

JSON

```
{
  "Transmit ID":"Device ID",
  "RSSI":"Decibels",
  "Receive ID":"Device ID"
}
```

###Android DB Format  
JSON

```
{
  "Transmit ID":"Device ID",
  "RSSI":"Decibels",
  "Receive ID":"Device ID"
  "GPS":"Location",
  "IMU":"Orientation",
  "Timestamp":"<UNIX time>"
}
``` 

###Android to Web Server  
Use same JSON format as Android DB for each entry  
Process:
* Android connects to WiFi
* App iterates through local database, sending each new entry to server via HTTP POST
* Sent entries are deleted  
 
```
//sends data over HTTP to server and removes old entries. Must be run in separate thread.
void sendData(JSONObject json)
```

###Web Server to Database Interface Class
Uses sames JSON format as Android DB for each entry
Process:
* Webserver gets new entry of data
* Webserver calls method "AddNewEntry" method with JSON data passed
    * Database interface class will then parse JSON data, add it to the appropiate fields in the database
    * Database interface class will return with Success/Failure as a boolean flag

###Interpolation Algorithm to Database Interface Class
Uses sames JSON format as Android DB for each entry
Process:
* Interpolation Algorithm calls the following methods as the Interpolation Algorithm requires
    * ListDataByEntryID:
        * Input: Entry ID
        * Return: Record (JSON)
    * ListDataByDevice:
        * Input: Xbee Device ID
        * Return: Collection of records (JSON)
    * ListDataByGeoArea:
        * Input: Start/Ending Locations [i.e. define the geo-location area)
        * Return: Collection of records (JSON)
    * ListDataByRSSI:
        * Input: RSSI value, Search direction (true: values >= RSSI returned, false: values < RSSI returned)
        * Return: Collection of records (JSON)
