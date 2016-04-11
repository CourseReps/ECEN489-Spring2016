# Group Messaging Version

When the QR Code is Made:
```
{
   "Sender": "Kyle",
   "Receivers": [
      "Bob",
      "Joe"
   ],
   "Message": "Hi Guys",
   "Time": "12:00:00 4-7-1016"
}
```
Upon scanning check users name to see if they indeed one of the intended receivers.

   -if not display "Nothing to see here" 
   
   -if yes then display message

If i want to funcationaly of displayingt the message on the server only when all the receivers have received the message, then the server will have to store who has read the message in order to prevent the users from having to send out additional messages.

UPON Scanning message. User will append to the JSON
```
{
   "Sender": "Kyle",
   "Receivers": [
      "Bob",
      "Joe"
   ],
   "Message": "Hi Guys",
   "Time": "12:00:00 4-7-1016",
   "Scanned": {
      "Name": "Joe",
      "Time": "12:00:00 4-7-1026"
   }
}
```
The Server Will read in the array of intented receivers and search from the names in the Scanned object. If all of the intened receivers have not added their name to the list, (they have not scanned and read the message), then the server will not display the message yet. 

Now when Bob Receives the message
```
{
   "Sender": "Kyle",
   "Receivers": [
      "Joe",
      "Bob"
   ],
   "Message": "Hi Guys",
   "Time": "12:00:00 4-7-1016",
   "Scanned": {
      "Name": [
         "Joe",
         "Bob"
      ],
      "Time": [
         "12:01:00 4-7-2016",
         "12:02:00 4-7-2016"
      ]d
   }
} 
```
Server will then display the message with the times and group members.
Ideally the server will have the ability to store multiple conversations like this.

What is left to define is the stucture of the Server that will store all of this information. 
* All i need is an array of JSON objects. 
* This is the simpliest and easiest way to store the information.
* The array trivial to traverse and the json object is also trivail to parse
* Therefore displaying the information on the screen is trivial 

Now How does the server start up another conversation? Very similar to how it starts the first one.
* Read in the JSON object
* Parse through it until it reads all the names (Sender: and all Receivers: If These names do not Match EXACTLY with a JSON object already in storage then this is a new conversation
*  Some notes: Sender1 does not have to = Sender2. The Overall list of names must match instead.
