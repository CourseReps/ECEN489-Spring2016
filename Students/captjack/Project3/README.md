#Secret Barcode Messanger

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

