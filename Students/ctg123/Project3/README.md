## Lunar finder App

This app will track the position of the Moon and also indicate where the user is relative to the moon.  When attempting to identify the position, the app will post real-time data that will logged to a server every 20 seconds.
The user will then be able to see where to point to the moon with their device by following a small circular graphic that will represent the most up-to-date positon of the moon.
The app will work in any orientation, both vertical and horizontal.

## What data will be recorded?

The data that this app will be collecting will be provided by a 3rd party website called MoonCalc.org. We can get the moon postions for moon rising, selected time, and Moonset and other kep Lunar data.
This will then be recorded in real-time and posted in JSON Format to an HTML Server.

## JSON Format Preview:
```
Moon rising:
Moon peak:
Moon set:
Moon distance:
Moon elevation:

```
## UI Framework:

The app will have a simple button to collect data and show the MoonCalc google map via Webview. When the user aligns with the moon from their relative position, a circle graphic will appear showing the phase (full, new, waxing cresent, etc.)
