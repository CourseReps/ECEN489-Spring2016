##Isolationbooth

###Description

###Partitions
*    App  
        The app is very simplistic in its function and in its UI. The UI is completely blank since there is no user. The app keeps the phone screen on and waits for a request from the server in our case the intel NUC. Once a request is received, the android device takes a photo using the camera class and sends that photo back to the server. The app is now done with its job.
*    Server
		The server is broken up into a few parts.
		-    TCPdump
		-    Database
		-    Location Algorithm
		-    Phone server interaction

		####TCPdump

		####Database

		####Location Algorithm
		The location algorithm determines if there is a device within our antenna array and if so what is its MAC address. It does this by going through the database of RSSI, MAC, Timestamp. The database is split into the three tables Wlan0, Wlan1, Wlan2. The algorithm takes the first entry in the first table and looks through all the other tables for an entry with the same mac address. Once they have been found it takes their RSSI's, calculates the magnitude, and stores that into a map of RSSI and MAC. After iterating through all of the tables it goes through the map and finds the maximum rssi magnitude and retrieves the MAC address associated with that magnitude. If this magnitude is above a certain threshold the device is considered within the antenna array the algortihm runs the retrieve photo program on the server.

		####Phone Server Interaction

###Problems Encountered

