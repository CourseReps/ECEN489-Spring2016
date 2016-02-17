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

Use this document to define/document interfaces:  
[Interface Definition Document](https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Project1/interfaces.md)

##Project areas  

###Necessary:  
XBee <-> Microcontroller interface XBee - Pilot Sensing - Yanxiang  
Microcontroller <-> Android Serial interface - Paul  
App Architecture - Keaton  
App ASynch and ui fragmentation - Fanchao  
Android - Inject into Local DB - Sam    
Android - IMU,GPS - Chaance  
Android -> Server - Thomas  
Tomcat master server  - Capt Commit  
Tomcat -> DB interaction Optional: Push to fusion table - John  
Algroithmic global reference system for database - Akash  

###Extra:  
3D Printing
