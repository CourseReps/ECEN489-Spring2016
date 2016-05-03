##Isolation Booth

###Description
The isolation booth is an instrument that can be used to bind MAC addresses of phones to pictures of their users. 3 directional antennas are used to monitor a small area of space and are connected to separate wireless interfaces on a Linux computer. An Android phone is used as a sort of "webcam" in the booth. When the central computer determines that a certain device is in the small space monitored by the antennas, it sends a command to the Android phone, instructing it to take and send a picture, and then pairs up the picture with the detected MAC address.

###Partitions
*    App  
        The app is very simplistic in its function and in its UI. The UI is completely blank since there is no user. The app keeps the phone screen on and waits for a request from the server in our case the Intel NUC. Once a request is received, the android device takes a photo using the camera class and sends that photo back to the server. The app is now done with its job.
*    Server
		The server is broken up into a few parts.  
		-    TCPdump  
		-    Database  
		-    Location Algorithm  
		-    Phone server interaction  

	####TCPdump  
	A bash script uses tcpdump to monitor the three different wireless interfaces. The script cycles through the three non-overlapping channels in the 2.4GHz band (1,6,11) and stores the output of tcpdump to a file. A java program runs through this output and picks out the MAC addresses with strong RSSI value and stores them to the database.  

	####Database
	We are using SQLite for our database. Our database is made up of three tables. One for each wireless interface. Within each table enteries are logged with RSSI, MAC address, and Unix Timestamp. The database entries are stored from parsing TCPDump for each of the wireless interfaces.
	####Location Algorithm  
	The location algorithm determines if there is a device within our antenna array and if so what is its MAC address. It does this by going through the database of RSSI, MAC, Timestamp. The database is split into the three tables Wlan0, Wlan1, Wlan2. The algorithm takes the first entry in the first table and looks through all the other tables for an entry with the same mac address. Once they have been found it takes their RSSI's, calculates the magnitude, and stores that into a map of RSSI and MAC. After iterating through all of the tables it goes through the map and finds the maximum rssi magnitude and retrieves the MAC address associated with that magnitude. If this magnitude is above a certain threshold the device is considered within the antenna array the algortihm runs the retrieve photo program on the server.

	####Phone Server Interaction
	The Android phone acts as a TCP server in this application, opening a port and listening for a connection. The NUC "server" (the TCP client in this case), connects to the phone, requests a picture, and then receives the picture data over TCP.

###Problems Encountered
-Determining if a device is within the area proved to be difficult. We occasionally get routers that have a high enough RSSI to be considered in our area. This can be easily fixed by blacklisting known routers in the area.  
-Attempting to access the database for read and write purposes at the same time. The way to deal with this would be to use a real database.


