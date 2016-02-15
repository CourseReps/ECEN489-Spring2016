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
Data should be stored in SQLite table on Android local storage  
On wifi connection, this routine will connect to the Tomcat server and send new rows from local DB to the remote one with HTTP POST  
Use same JSON format as Android DB
