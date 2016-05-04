# Project 3
--

## Overview

The objective of this project is to build the APP which can recognize the speech and translate the words. Another interesting feature of this APP is that it can send messages to a group of people. You can use this app as chat tool. What's more, even if you speak different languages, you can use this APP translate what the other persons say and listen to the result of translation. 
###Speech to Text (Voice recognition)
--
Actually, android comes with an inbuilt feature speech to text through which you can provide speech input to your app. In the background how voice inout works is, the speech inout will be streamed to a server. On the server voice will be converted to text and finally text will be sent back to our app.

Basically, I did two things:
* Starting RecognizerIntent: (ACTION_RECOGNNIZE_SPEECH) simply takes user's speech inout and returns it to same activtiy. (EXTRA_LANGUAGE)Set the input language; (EXTRA_PROMPT)Text prompt to show to the user when asking them to speak.
* Receiving the speech response: Once the speech input is done we have to catch the response in onActivityResult and take appropriate action needed.

###Translation
--
At the beginning, I utilize Google Translate API to implement this functionality. There are a bunch of documents and tutorials about how to use this API. Basically, following these steps:
* Select or create a Cloud Platform Console(Like our task 4 google fusion table)
* Enable billing for your Project
* Enable the API and any related services
* Get API key
* Using a URL of the following form: https://www.googleapis.com/language/translate/v2?parameters
* Reference website: https://cloud.google.com/translate/docs/

To simplify this app, I only set 5 language options for users to choose from. Actually, the number of languages it can translate can be up to 90.

For the latest version, I start to use Microsoft Translate API, because this one is free while the Google Translate API may cost some money. It is still very easy to use it by following these steps:

* Sign-in to Azure DataMarket with a Microsoft Account: Create a Microsoft Account (username and password) which allows you to sign-in to Azure DataMarket.

* Subscribe to the Microsoft Translator API: Subscribe to the plan that meets your monthly volume user requirements. Each offer has a free subscription plan available to all users. The free plan has the same features and functionalities as the paid plans and does not have an expiration date.

* Register your application and create your Client ID and Client Secret: The Client ID and Client Secret values are used to authenticate your service when you call the Translator API from your application. Take note of the Client ID and Client Secret fields and keep these values confidential. You will need these when you develop your app.

`    public String translate(String text) throws Exception{`
        `Translate.setClientId("leoyyx2009"); //Change this`
        `Translate.setClientSecret("a6OqAafzSYis4ZKMWd+EWgE4g5NvwhGnJ9faDcEhCyM="); //change`
        `String translatedText = "";`
        `translatedText = Translate.execute(text, Language.ENGLISH);`
        `return translatedText;`

    `}`

###Tomcat server(old version)
--
Basically, I apply what we have learned from assignment 4.
There are two tutorials once I learned: 
https://tomcat.apache.org/tomcat-7.0-doc/building.html; http://veereshr.com/Android/AndroidToServlet 

###Communication(old version)
--
To finish this part, I utilize the socket to achieve the communication. The sockets of android work exactly as they do in Java.

To do so, we need first to establish a server: ServerSocket server = new ServerSocket(portNumber, queueLength); When you open this app, it will start a thread to build this server and wait for connections. Then, if you click SEND button, this will call another activity which performance as a client. You can send a message to another person who is using the app at the same time(you need to know the server IP address). The message will show on the EDITTEXT for translation, which means the one who get the message can translate the message to the languages he or she like. Also, the message can be entered by dictating or typing.

###Communication(latest version)
--

This new version is able to allow several persons to chat together, like a chat room. What's more, you can communicate with the person who speaks an another language. This APP will be able to translate the latest chat history and speak out the result of translation.

To achieve these functionalities, I used Google Cloud Messaging API. Google Cloud Messaging for Android (GCM) is a service that allows you to send data from your server to your users' Android-powered device and also to receive messages from devices on the same connection.

In order to use GCM, we need to go through the following steps:

* Register with Google Console and enable GCM: 1.Obtain Sender ID (Project Number); 2.Obtain Server API Key

* Integrate GCM into our Android app: 1.Register for an instance ID and generate a token; 2.Transmit the token to our web server; 3.Register a GCM Receiver to handle incoming messages; 4.Register an InstanceID Listener Service to handle updated tokens

* Develop HTTP Server with GCM endpoints: 1.Endpoint for registering a user with a registration token; 2.Endpoint for sending a push notification to a specified set of registration tokens.
