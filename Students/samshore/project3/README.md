#Project 3

For project 3, I am creating an Android app that will allow a user to control a computer mouse cursor by tilting the android device. To do this, the Android device will collect orientation readings from its IMU sensor. The device’s y-axis rotation will be used to move the cursor left and right, while its x-axis rotation will be used to move the cursor up and down. If this is accomplished successfully, I will attempt to implement additional features, such as left and right click using touch buttons. The app will perform a simple filter on this data and send values over a network socket to a server hosted on the target computer. On the server side, a java program will receive messages from the Android device and store them in a buffer. The program will then parse the message and determine what mouse action is to be performed. The actions will be performed using Java’s Abstract Window Toolkit. The mouse cursor speed will be determined by the rotation data sent by the phone, and the cursor acceleration will be limited by the data filtering.



###DOWNLOADS
https://www.dropbox.com/sh/yj029xbw49hrwj2/AABQ7Qc8ISj4ZunN9NTAEVe9a?dl=0

This folder contains the android app apk, the server jar file, and the pong client with its dependencies.
