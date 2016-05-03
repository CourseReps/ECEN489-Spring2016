### Lunar finder App

## Project Overview


This app will track the position of the Moon via a 3D rendering and would be able to update information to the User such as Ecliptic Latitude and Longitude, Phase, and distance. A Javascript based program was written in conjuction with obtaining the aforementionded values. From there it will be able to allow the user to open the camera screen to get an accurate representation of the location of the moon basesd's on the calculator's results. This app would be useful for any astronomy enthusiast or anybody wanting to get a sense of where the Moon can be agorithmatically.


## Features and API's used
- Augmented Reality with Camera API and OpenGL ES2.
- WebView application. 
- GPS location of the device.
- Orientation sensor API.

 
## Project Checklist
- Framework for Augmented reality activity and Simple two button interface was created.
- 3D rendered sphere model was successfully created on placed on the Surfaceview of the phone screen.
- The Javascript application for calculating the defined Moon data was successfully utilized to return values.

## Further Implementation for the app
- 2D image wrapping of sphere indicating the phases from the calculator - 8 phases can be rendered with a swith/case statment on top of the the Sphere.
- Save the data to a .txt file for the user to access in a later time.  
- A more defined robust method to convert ecliptic cooridinates to phone orientation data parameters. (yaw, pitch, roll)
