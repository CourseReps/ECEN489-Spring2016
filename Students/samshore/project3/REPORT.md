#Project 3 Report


##OVERVIEW

This project allows a user to control their computer's mouse cursor by tilting an Android device. An app running on the Android device is able to read the device's orientation, filter the orientation data for varying levels of sensitivity, and send this data to a target computer. Once the user presses the button to send data to the computer, the phone will periodically store orientation data in a list, filter this data, and then send this data to the target computer via a UDP socket. The computer will be running a Java program that is listening for the incoming packets. Once it receives a packet, it will convert the device rotations into cursor movement. The program was further extended to allow the Java program to forward incoming messages to a secondary server running the same program when multiple devices connect at the same time. This part of the project was created with the idea that it would be used to play a sort of randomized LAN Pong game. All users connect to the same machine, but some are sent to the secondary machine. Each machine runs a LAN Pong client and the two machines play against each other. However, users do not know which machine is receiving their mouse movements. 


###ANDROID APP 

source code can be found here:
https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/samshore/project3/ClientApp/app/src/androidTest/java/io/github/samshore/clientapp/ApplicationTest.java

The Android application is fairly simplistic and consists of only one activity. The GUI includes to text fields: one for the IP address of the target machine, and one for the desired filter length (a longer filter will result in a less sensitive, more "viscous", response from the cursor). Below these text fields are two buttons. The connect button begins the process of recording data and sending data to the server, whereas the disconnect button stops this process. At the top of the app, there is a small textview that indicates whether or not the device is sending data.

All of the work is done once the user presses the connect button. This runs a routine that pulls values from the text fields, creates a weighted rectangular windowing filte (this is discussed more below), and launches a thread that does the following:
* Adds pitch and roll values to a list of previous values
* Filters the newly received data 
* Stores filtered pitch and roll values in a byte array
* Creates and sends a datagram packet with this byte array, the user input IP address, and a specified port number
This thread runs every 50 ms.

The filter created by the app is simply a linear windowing filter with a length specified by the user. The filter did not have to be very sofisticated, because it was only meant to reduce some of the faster and more spastic device movements. As I tested the app, however, I found I preferred a filter length 1-5 i.e. little to no filtering. In my opinion, the delay caused by a longer filter was more annoying than sharp cursor movements (which were not nearly as bad as I thought they might be anyway). Plots of filter of varying lengths and their frequency responses are shown below:

![filters](https://cloud.githubusercontent.com/assets/16807182/14992243/c3c314c8-112a-11e6-995b-2e473570180f.jpg)


###SERVER PROGRAM

Most of the work in this project is done by the server-side program. This program opens a socket connection and receives incoming packets. It redirects them to the secondary server if neccessary. Then the program will decode the packets and convert the orientation values received from the phone into mouse cursor movements using Java's abstract window toolkit. The program consists of five classes:
* UDPServer
* UDPServerThread
* UDPServerThread1
* CursorThread
* Users

These classes are explained below:

**UDPServer** - https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/samshore/project3/AndroidMouse/src/UDPServer.java
	
This is the main class. It first prompts the user if it is running on machine 1 (the main server) or machine 2 (the secondary server). It also prompts for the expected number of users for timing purposes. Depending on whether the user inputs machine 1 or machine 2, the program then starts UDPServerThread or UDPServerThread1, respectively. It then scans for any user input to close all open sockets and stop the program.

**UDPServerThread** - https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/samshore/project3/AndroidMouse/src/UDPServerThread.java
	
This class defines the server thread that receives data from the Android device, as well as redirects data packets to the secondary server if necessary. It first opens a socket on a specified port for receiving packets, and opens another for sending packets to the second machine. The thread then listens for/receives incoming packets. When a packet is received, the IP of its origin is checked. This IP is checked against a list of users containing the IPs and "team" (remember, I made this part with the Pong game in mind) of all current users. If the user does not yet exist on that list, a team will be assigned depending on whether there are an even or odd number of users currently, and then a user object will be created using this data. This user object is then added to the list of users. If the user does exist, the team number associated with the current packet's origin IP is extracted. If the user is on the team associated with the second server, a new packet is created with the received data and it is sent to machine 2. Otherwise, the data from the received packet is decoded and the pitch and roll values are recorded. If the device's orientation is within 5 degrees of zero, it will be rounded down to zero in order to make it easier to keep the mouse cursor steady.

**ServerThread1** - https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/samshore/project3/AndroidMouse/src/ServerThread1.java
	
This class functions exactly the same as the previouse one, except it does not check for packets' IP addresses or user teams. It simply decodes received packets and records rotation values.

**CursorThread** - https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/samshore/project3/AndroidMouse/src/CursorThread.java

This thread utilizes Java's abstract window toolkit to move the computer's mouse cursor. The class takes the dimensions of the display and uses these to set boundaries for the cursor. In addition, it sets the cursor in the center of the screen initially. Once the rotation values are updated by the server thread, the cursor thread will translate these rotation values into x and y offsets. This was done initially by considering the possible angle values sent by the phone, the dimensions of a common monitor, and the rate at which the position is updated. From here the calculation was just tweaked by trial and error until the cursor movement felt comfortable and easy to control. These calculated offsets are added to the cursor's current coordinates. The cursor is then moved to the updated coordinate. This thread repeats every 50 ms.

**Users** - https://github.com/CourseReps/ECEN489-Spring2016/blob/master/Students/samshore/project3/AndroidMouse/src/Users.java

This is a simple class used to store the origin IP address and team number of each user.



