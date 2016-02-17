##Use this page to document interface definitions  

###Datastructure


###XBee to Microcontroller  
JSON

```
{
  "Xbee ID":"Device ID",
  "Strength":"Decibels",
  "Conected Device":"Device ID"
}
```

###Microcontroller to Android  

JSON

```
{
  "Xbee ID":"Device ID",
  "Strength":"Decibels",
  "Conected Device":"Device ID"
}
```

###Android DB Format  
JSON

```
{
  "Xbee ID":"Device ID",
  "Strength":"Decibels",
  "Conected Device":"Device ID",
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
