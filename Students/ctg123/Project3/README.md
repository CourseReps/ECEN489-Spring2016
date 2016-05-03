### Lunar finder App

## Project Overview


This app will track the position of the Moon via a 3D rendering and would be able to update information to the User such as Ecliptic Latitude and Longitude, Phase, and distance. A Javascript based program was written in conjuction with obtaining the aforementionded values, then be able to allow the user to open the camera screen to get an accurate representation of the location of the moon basesd's on the calculator's results. This app would be useful for any astronomy enthusiast or anybody wanting to get a sense of where thm Moon can be agorithmatically.

## What data will be recorded?

The data that this app will be collecting will be provided by a 3rd party website called MoonCalc.org. We can get the moon postions for moon rising, selected time, and Moonset and other kep Lunar data.
This will then be recorded in real-time and posted in JSON Format to an HTML Server.

## Features and API's used
- Augmented Reality
- WebView application
- GPS location of the device

Below is the Classes used for Augmented Reality 

## Project Checklist
- Framework for Augmented reality activity and Simple two button interface was created.
- 3D rendered sphere model was successfully created on placed on the Surfaceview of the phone screen.
- The Javascript application for calculating the defined Moon data was successfully utilized to return values.

## Further Implementation for the app
- 2D image wrapping of sphere indicating the phases from the calculator.
- Save the data to a .txt file for the user to access in a later time.
- A more defined robust method to convert ecliptic cooridinates to phone orientation data parameters. (yaw, pitch, roll)
