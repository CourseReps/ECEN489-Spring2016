# Project 3

## Synopsis

The objetive of this project is to build the APP which can recognize the speech and translate the words. Another interesting feature of this APP is that it can send message to other persons who suse the same APP on their mobile device. Once you know each other's IP addresses, you can use this app as chat tool. What's more, even you two speak different languages, you can use the APP translate what he other person say and listen to the translation. Finally, while translating the words, the APP will also record these words to the web server. This functionality will definitely help you review the words you do not know.

###Speech to Tet(Voice recognition)
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
