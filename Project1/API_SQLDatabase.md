#API Structure for SQL Database for Project 1  

##Class Name: RFFieldSQLDatabase
###Purpose of RFFieldSQLDatabaseclass:
This class provides the interface between the calling fucntion and a MySQL database and Google Fusion Tables.  
There are various functions to retrieve data based upon provided criteria such as 
Latitude and Longitude, Device ID, etc...

##Methods

###ConnectToDatabase
Purpose: Establishes a connection to the database<BR>  
Input: Host Address for MySQL server, Use Google Fusion Tables<BR>
Return: Success (TRUE) or Failure (FALSE) as boolean<BR><BR>
Example: RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com", true);

###DisconnectDatabase
Purpose: Disconnects the connection to the database<BR>  
Input: none<BR>
Return: Success (TRUE) or Failure (FALSE) as boolean<BR><BR>
Example: RFFieldDatabase.DisconnectDatabase();

###AddNewEntry
Purpose: Insert new data to table<BR>  
Input: JSON String, Use Google Fusion Tables<BR>
Return: Success (TRUE) or Failure (FALSE) as boolean<BR><BR>
Example: RFFieldDatabase.AddNewEntry("{"SampleNumber":-1,"XbeeID":456,"DeviceID":1234,"RSSI":100.0,"Latitude":30.75992,"Longitude":-96.222885,"Yaw":10.0,"Pitch":20.0,"Roll":30.0,"SampleDate":"Feb 19, 2016 1:56:51 PM"}", true);

###ListDataByEntryID
Purpose: Get RF Data Entry by Record Number<BR>  
Input: Sample Number / Record Number (int)<BR>
Return: JSON String<BR><BR>
Example: RFFieldDatabase.ListDataByEntryID(1);

###ListDataByDevice
Purpose: Get RF Data Entries by Devicd ID<BR>  
Input: Device ID (int)<BR>
Return: JSON String, array list of the RF Data Class<BR><BR>
Example: RFFieldDatabase.ListDataByDevice(1234);

###ListDataByRSSI
Purpose: Get RF Data Entries by RSSI<BR>  
Input: RSSI value (float), >= (true) or < (false)<BR>
Return: JSON String, array list of the RF Data Class<BR><BR>
Example: RFFieldDatabase.ListDataByRSSI(56.0F, true);

###ListDataByGeoArea
Purpose: Get RF Data Entries by Lat/Long Extents.  Sending 0,0,0,0 will return all records<BR>  
Input: Starting Latitude (float), Starting Longitude (float), Ending Latitude (float), Ending Longitude (float)<BR>
Return: JSON String, array list of the RF Data Class<BR><BR>
Example: RFFieldDatabase.ListDataByGeoArea(30.0F,-95.5F,31.2F,-97.0F);






