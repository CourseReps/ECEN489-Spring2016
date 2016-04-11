#Face Identification on Android

##Overview

This is an app for face identification in a static photo from a finite set of human faces. The user uses this app to take a picture of a human face, and the app picks from the pre-defined set the person whose face most fits this picture, and displays his name on the screen. The app runs in two modes: training mode and running mode. In the training mode, the user needs to take photos of people's faces that he wants the app to recognize. The classification algorithm will be trained based on these samples, so in the running mode, a new photo comes in, and the app is able to tell whose face it is, hopefully.

##Technical Details

###UI Framework

The UI consists of 3 fragments: the MainFragment, the NameListFragment and the SamplesFragment. On the MainFragment, the user can switch between the training mode and the running mode, add new names, take photos, and read the classification result. Of course, there are some restrictions on the relations between these elements, like if the user hasn't added any name, then it's not allowed to take a photo, as there are no names for the photo to be associated with. On the NameListFragment, the user can read all the names he's already added. And on the SamplesFragment, the user can read all the sample photos for each name in a list.

###Data Storage-SQLite Database and External Storage

The app needs somehow save the previous samples, so the user does not need to re-take sample photos every time he opens the app. I use SQLite Database to store sample info. and name info.. So basically, there are two tables in the database, and the RECORD_TABLE which is for training samples, and TYPE_TABLE which is for names. They are binded by a foreign key constraint that requires every name for a new sample should be an existing name in the name table. One important entry stored in the RECORD_TABLE is the path where the full-size photo is stored(a path points to somewhere on the external storage), so when the app starts training, it's able to read all the full-size photos from the external storage first.

###Face Recognition API-JavaCV

A face recognition API was provided in OpenCV 2.4(It's surprising that in OpenCV 3.1, the recognition algorithm is not provided at all). OpenCV is an open source image processing package written in C/C++. So, in order to use it on android, we need to use some Java-wrapped version of OpenCV. However, the Java wrapper provided by the official provider of OpenCV does not support face recognizer(The FaceRecognizer class is in the package, while no training altorighm is supported). So, here, I leverage another Java wrapper provided by Google, called JavaCV, which is provided in a github repository named bytedeco. This Java-wrapped version of OpenCV provides the Java interface to do the training of the FaceRecognizer class.

###Tomcat Server

Up to now, I haven't come up with what to do on the server side. 

##Schedule

Week 1: UI, data storage

Week 2: JavaCV

Week 3: Tomcat Server, final Integration, probably some extensions(Such as face recognition in a live video stream).
