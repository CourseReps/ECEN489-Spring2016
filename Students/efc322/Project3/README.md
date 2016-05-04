#Face Identification on Android

##Overview

This is an app for face identification in a static photo from a finite set of faces. The user can use this app to take a picture of a human face, and the app picks from the pre-defined set the person whose face most fits this picture, and displays his name on the screen. The app runs in two modes: training mode and running mode. In the training mode, the user needs to take photos of people's faces that he wants the app to recognize. The classification algorithm will be trained based on these samples, so in the running mode, when a new photo is taken, and the app is able to tell whose face it is, hopefully.

##Technical Details

###UI Framework

The UI consists of 3 fragments: the MainFragment, the NameListFragment and the SamplesFragment. On the MainFragment, the user can switch between the training mode and the running mode, add new names, take photos, and read the classification result. On the NameListFragment, the user can read all the names he's already added. And on the SamplesFragment, the user can read all the sample photos for every name as a list.

###Data Storage-SQLite Database and External Storage

The app needs somehow save the previous samples, so the user does not need to re-take sample photos every time he opens the app. I use SQLite Database to store sample info. and name info.. So basically, there are two tables in the database, the RECORD_TABLE for training samples, and TYPE_TABLE for names. They are binded by a foreign key constraint that requires every name for a new sample be an existing name in the name table. One important entry stored in the RECORD_TABLE is the path where the full-size photo is stored(a path points to somewhere on the external storage), so when the app starts training, it's able to read all the full-size photos from the external storage first.

###Face Recognition API-JavaCV

A face recognition API was provided in OpenCV 2.4(It's surprising that in OpenCV 3.1, the recognition algorithm is not provided at all). OpenCV is an open source image processing package written in C/C++. So, in order to use it on android, we need to use some Java-wrapped version of OpenCV. However, the Java wrapper provided by the official provider of OpenCV does not support face recognizer(The FaceRecognizer class is in the package, while no training altorighm is supported). So, here, I leverage another Java wrapper provided by Google, called JavaCV, which is provided in a github repository named bytedeco. This Java-wrapped version of OpenCV provides the Java interface to do the training of the FaceRecognizer class.

Tutorials on how to set up OpenCV on Android: https://www.youtube.com/watch?v=JIHfqzTdOcQ

bytedeco repository on Github: https://github.com/bytedeco

OpenCV documentation on Face Recognizer: http://docs.opencv.org/2.4/modules/contrib/doc/facerec/index.html

###Server

I set up a Tomcat Server to collect all the photos taken in the running mode. The server receives the photos and their corresponding classification results, stores the photos on the hard drive, and the file path and the result in MySQL Database. The server can feed back these photos and results by a GET method.

Problems encountered in debugging the server:

1. The server cannot decode the Base64-encoded photo corretly.

Solution: 

On the android device, when encoding a photo by Base64, the flags need to be set as NO_WRAP and URL_SAFE.

2. The server cannot receive some photos at all.
 
Solution: 

It turns out some photos are over-sized, for some reason. And, Tomcat server imposes the limit on the maximum size of the data in POST Method(2MB by default), which I use to send photos to the server from the android device. To increase this value, I configure the 'maxPostSize' parameter in the connector to be 10MB, and a 'connector' is in the $TOMCAT_HOME/conf/server.xml file. A connector contains all the configurations of a HTTP connection, including the port number. 

But still, this is not the most fundamental solution to the problem. The reason why some photos are correctly received by the server and some others are not, is that when an app retrieves a full-size photo from the camera by an Intent object, the size of the photo varies, according to the quality of the photo. And this is why some photos are oversized. 

So, one way to solve this problem is to read the photo into a Bitmap after it's returned from the camera, resize it and save it into the original file. The other way is to configure the camera first, but the methods to do this are deprecated since API 21.

References: Tomcat Official Documentations: https://tomcat.apache.org/tomcat-6.0-doc/config/http.html

##Schedule

Week 1: UI, data storage

Week 2: JavaCV

Week 3: Tomcat Server, final Integration, probably some extensions(Such as face recognition in a live video stream).
