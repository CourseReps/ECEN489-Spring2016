##Isolation Booth

###Description
The isolation booth is an instrument that can be used to bind MAC addresses of phones to pictures of their users. 3 directional antennas are used to monitor a small area of space and are connected to separate wireless interfaces on a Linux computer. An Android phone is used as a sort of "webcam" in the booth. When the central computer determines that a certain device is in the small space monitored by the antennas, it sends a command to the Android phone, instructing it to take and send a picture, and then pairs up the picture with the detected MAC address.

###Partitions
*    App  
        The app is very simplistic in its function and in its UI. The UI is completely blank since there is no user. The app keeps the phone screen on and waits for a request from the server in our case the Intel NUC. Once a request is received, the android device takes a photo using the camera class and sends that photo back to the server. The app is now done with its job.
*    Server

###Problems Encountered
*    Keaton was being mean
*    It's all Keaton's fault

