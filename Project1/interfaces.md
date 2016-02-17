##Use this page to document interface definitions  

###Datastructure


###XBee to Microcontroller  
JSON

```
{
  "Xbee ID":"Device ID",
  "Strength":"Decibels",
  "Connected Device":"Device ID"
}
```

###Microcontroller to Android  

JSON

```
{
  "Xbee ID":"Device ID",
  "Strength":"Decibels",
  "Connected Device":"Device ID"
}
```

###Android DB Format  
JSON

```
{
  "Xbee ID":"Device ID",
  "Strength":"Decibels",
  "Connected Device":"Device ID",
  "GPS":"Location",
  "Orientation":"",
  "Timestamp":"<UNIX time>"
}
``` 

###Android to Web Server  
Use same JSON format as Android DB for each entry  
Process:
* Android connects to WiFi
* App sends GET request to server asking for most recent entry stored in remote DB
* Server sends response to app with timestamp of most recent entry
* App starts at newest entry in local DB not contained on server
    * App sends next line as JSON via POST to server
    * App waits for ACK from server (string "ACK")
    * If more new entries, continue, else done.

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
