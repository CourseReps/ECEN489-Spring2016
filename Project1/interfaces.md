##Use this page to document interface definitions  

###Datastructure
JSON

```
{
  "ID":"TeamName",
  "IRRange":"xxx",
  "PumpRate":"xxx",
  "FlowRate":"xxx",
  "SolenoidState":"ON/OFF",
  "Timestamp":"Unix epoch",
  "CurrentIP":"xx.xxx.xxx.xx"
}
```

###XBee to Microcontroller  
[info here]  

###Microcontroller to Android  
[info here]  

###Android DB Format  
[info here]  

###Android to Web Server  
Data should be stored in SQLite table on Android local storage
On wifi connection, this routine will connect to the Tomcat server and send new rows from local DB to the remote one with HTTP POST
