#ECEN 489 - Project 3: Isolation Booth  

The goal of this project is to construct a "booth" equipped with an Android device and a Linux server connected to one or more directional antennas. The server and antenna(s) will monitor MAC addresses that are present. When it is determined that a MAC address belongs to a device that is within the booth, the Linux server will command the Android device to take a picture of the user within the booth and transmit it back to the server. The server will bind this picture to the MAC address detected and store it in a database.  

Participants:
- Keaton Brown
- Thomas Branyon  

### Week 1 Progress  

Thomas:  
Code written for communications on NUC Server and Android sides. Uses TCP socket to transmit compressed image data.
