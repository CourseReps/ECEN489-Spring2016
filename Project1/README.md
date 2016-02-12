#Project1 

3-d printed mount attached to the back of an Android phone.  
On the 3-d printed mount we are attaching a XBee with an SMA Antenna attachment and a Teensy Microcontroller.  
A USB cable will be connected from the phone to the teensy.  
This will log the recieved signal strength, the GPS location, orientation of phone.  
We will need to use the sensor API to get orientation of the phone.  
We then store information into local sqlite database.  
Upon request should be able to push its data to a tomcat server.  
Pushes data only when wifi enabled.  

TomcatServer  
JAX-WS or JSON
  
The server should be able to log info into a database for multiple phones.  

We should then be able to construct a tensor field map of the area.  


Time frame: 2 weeks starting Feb 15  

##Project areas  

###Necessary:  
XBee - Pilot Sensing  
Microcontroller  
XBee <-> Microcontroller interface  
Microcontroller <-> Android Serial interface  
---
App Architecture  
Android - Local DB  
Android -> Server  
---
Tomcat master server  
Tomcat -> DB interaction Option: Push to fusion table  
Algroithmic global reference system for database  

###Extra:  
3D Printing
