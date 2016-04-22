Face Recognizer 1.0:

This is an offline android app that can detect and recognize a face from a finite set of pre-stored faces. Basic face recognition is 
working. But no server is involed yet.

Face Recognizer 1.1:

Some new features are added:

1. The data from the illumination sensor is pulled, so when it's too dim, taking photos will be disallowed.
 
2. The training file of the face recognizer is saved into the internal storage every time the training is complete, so the app can load the file when it's opened. Also, the hash values of the sample list and name list are stored into a sharedpreferences file. So, when the app is switched to the running mode in the future, the two hash values are computed first to see if any of the two lists has been changed.
