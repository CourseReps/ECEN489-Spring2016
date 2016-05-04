# Project 3
--

## Synopsis

The objetive of this project is to build the APP which can recognize the speech and translate the words. Another interesting feature of this APP is that it can send message to other persons who use the same APP on their mobile device. Once you know each other's IP addresses, you can use this app as chat tool. What's more, even you two speak different languages, you can use the APP translate what he other person say and listen to the translation. Finally, while translating the words, the APP will also record these words to the web server. This functionality will definitely help you review the words you do not know.

###Speech to Text (Voice recognition)
--
Actually, android comes with an inbuilt feature speech to text through which you can provide speech input to your app. In the background how voice inout works is, the speech inout will be streamed to a server. On the server voice will be converted to text and finally text will be sent back to our app.

Basically, I did two things:
* Starting RecognizerIntent: (ACTION_RECOGNNIZE_SPEECH) simply takes user's speech inout and returns it to same activtiy. (EXTRA_LANGUAGE)Set the input language; (EXTRA_PROMPT)Text prompt to show to the user when asking them to speak.
* Receiving the speech response: Once the speech input is done we have to catch the response in onActivityResult and take appropriate action needed.

###Translate
--
I utilize Google Translate API to implement this functionality. There are a bunch of documents and tutorials about how to use this API. Basically, following these steps:
* Select or create a Cloud Platform Console(Like our task 4 google fusion table)
* Enable billing for your Project
* Enable the API and any related services
* Get API key
* Using a URL of the following form: https://www.googleapis.com/language/translate/v2?parameters
* Reference website: https://cloud.google.com/translate/docs/

To simplify this app, I only set 5 language options for users to choose from. Actually, the number of languages it can translate can be up to 90.

###Tomcat server
--
Basically, I apply what we have learned from assigment 4.
There are two tutorials once I learned: 
https://tomcat.apache.org/tomcat-7.0-doc/building.html; http://veereshr.com/Android/AndroidToServlet 

###Communication(early version)
--
To finish this part, I utilize the socket to achieve the communication. The sockets of android work exactly as they do in Java.

To do so, we need first to establish a server: ServerSocket server = new ServerSocket(portNumber, queueLength); When you open this app, it will start a thread to build this server and wait for connections. Then, if you click SEND button, this will call another activity which performance as a client. You can send a message to another person who is using the app at the same time(you need to know the server IP address). The message will show on the EDITTEXT for translation, which means the one who get the message can translate the message to the languages he or she like. Also, the message can be entered by dictating or typing.

###Communication(latest version)
--

This new version is able to allow several persons to chat togethor, like a chat room. What's more, you can communiate with the person who speaks an another language. This APP will be able to translate the latest chat history and speak out the result of translation.
To ahieve these functionalities, I used Google Cloud Messaging API.

