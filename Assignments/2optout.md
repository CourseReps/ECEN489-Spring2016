# Opt-Out Challenge 2


## Reading Assignment (optional)

How to Program Java, 10th edition
 * Chapter 4 – Control Statements: Part 1
 * Chapter 5 – Control Statements: Part 2
 * Chapter 6 – Methods: A Deeper Look
 * Chapter 27 – Networking (Sections 27.4, 27.5, 27.6)


## Programming Challenges

Create a simple Java application that displays information about the computer it is executed on.
In particular, print date and time.
Use ```System.getProperty``` to display the following attributes: Java class path, JRE vendor name, JRE version number, Operating system architecture, Operating system name, Operating system version, User home directory, and User account name.
Some of the attribute may return null.

Create a server-client infrastructure using stream sockets.
The client should be able to access the server over the Internet, query the server for the information listed above, and display this information locally.

### Server

Establishing a simple server in Java requires five steps.
 * Create a ```ServerSocket``` object:
```java
ServerSocket server = new ServerSocket(portNumber, queueLength);
```
The variable ```portNumber``` is an admissible TCP port number and  ```queueLength``` is the maximum number of clients that can wait to connect to the server.
 *  Wait for a connection:
```java
Socket connection = server.accept();
```
In this step, the server listen indefinitely for an attempt by a client to connect.
The method returns a ```Socket``` when a connection with a client is established.
 * Manage the I/O streams associated with the socket:
```java
connection.getOutputStream();
connection.getInputStream();
```
These objects can subsequently be employed to send or receive bytes with the ```OutputStream``` method ```write``` and the ```InputStream``` method ```read```, respectively.
One can also use classes such ```ObjectInputStream``` and ```ObjectOutputStream``` to enable entire objects to be read from or written to a stream, a technique called wrapping.
 * Support the live interaction:
In the processing phase, the server and the client communicate via the ```OutputStream``` and ```InputStream``` objects.
 * Closing the connection:
The server closes the connection by invoking the ```close``` method on the streams and on the ```Socket```.


