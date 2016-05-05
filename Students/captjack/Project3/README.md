#Secret Barcode Messenger

Please read to get a full understanding of the scope and limitations of this project

###HOW TO RECREATE / WHERE TO FIND MY CHANGES
#### ZXING Android Application
This is a very large application, but all of my changes can be found with one file. The **Main Activity** under the **sample module** has all of my functions commented. Check out these guys to get the base application without my changes. https://github.com/zxing/zxing 
#### Web Server
This is will be slighty more complicated because github wouldn't let me upload the entire project because file names were too long...
At the base, this is an **Express App** created using **Node.js** look online for getting this set up. I programmed using IntelliJ. This guy has very helpful tutorials for beginners. https://www.youtube.com/playlist?list=PL6gx4Cwl9DGBMdkKFn3HasZnnAqVjzHn_ What I have done is taken the essential files (aka not the auto-generated files that no-one ever touches) and put them here. That would be all of the .ejs and .js files as well as the www file that you run to start the server. 


###Project Overview
The goal of this project is to provide a means of secret communication between users by means of encoded QR codes.
When using the phone the user must first log in, then they can create their message with its intented recipients. 
The QR code is then generated on the phone, and then sent online to the web application.

When using the web application users must first log in, and then they are directed to their chat history page where
the QR images are rendered. 

Users can then scan the QR images of messages intedted for them to see. If they have logged onto the website, and additionally logged into the phone, they can then read the message after scanning on their phone from the web application.

####Android specifics
#####QR Generation
Here is a JSON creating from the user input
```
{
   "Sender": "Kyle",
   "Receivers": [
      "Bob",
      "Joe"
   ],
   "Message": "Hello",
   "Time": "12:00:00 4-7-1016"
}
```
Right now a simple shift cipher is used to encode ONLY the message before the QR is generated

```
Message": "khoor"
```

Then THE QR image is generated for this the entire JSON String and sent to the web application

#####QR Scanning
Upon scanning the phone check users name to see if they indeed one of the intended receivers.

   -if not display "Nothing to see here" 
   
   -if yes then display message


####Web Application
The Web application is run by Express using Node.js. The brains of the web application are all written in javascript and the pages are rendered with html and embedded javascript.

There is some primative cookie and session mangement on the web application to keep users authenticated when they navigate. 
There is also a tool to upload images from your computer to the website as well (instead of directly from the phone).

###Accomplishments and Failures
* successfully integrated ZXing Application to generate QR codes
* created a proof of concept encryption system
* created a web application that uses proper session and cookie management, and proper authentication with a MySQL database (weak auth and management code will be used a a teaching tool for the cyber security club, will be made stronger in the furture).

* failed to succesfully receive QR image sent from phone
* failed to retreive image that was uploaded via the website (i.e. I was able to receive the picture, but failed to display it back)
* The android application does not use the MySQL databse for authentication




