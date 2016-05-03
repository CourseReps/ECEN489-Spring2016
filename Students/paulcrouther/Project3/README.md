#Motion Detection

##This is the project for motion detection from the camera. 

##Features and APIs:

Features
-Motion detection (Dhash and PIL) based on JPEG (from Camera API, Bitmap) from gray scale information.

APIs

Android:
-Camera API

Python:
-PIL
-Socket

Using Camera API we create a thread to allow for image sending through UDP with the pictureCallback:
```javascript
Runnable picRunnable = new Runnable() {
                    @Override
                    public void run() {
                        camera.takePicture(null, null, pictureCallback);

                    }
                };
                
```
Set the 1 Hz image sending frequency to take a picture:
```javascript

                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(picRunnable,0,1, TimeUnit.SECONDS);
```
We use pictureCallback to convert the RAW image to Bitmap format, and then converted to JPEG and resized to 300x200 px.
This is picture then sent the the Python server through UDP. We are limited to 64kB on UDP.

```javascript

PictureCallback pictureCallback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap == null) {
                    Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                try{

                    final InetAddress serverAddr = InetAddress.getByName("IP ADDRESS");

                    Log.d("UDP", "C: Connecting...");
                    DatagramSocket socket = new DatagramSocket();
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap,300,200,true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] buff = baos.toByteArray();

                    // create a UDP packet with data and its destination ip & port
                    DatagramPacket packet = new DatagramPacket(buff, buff.length, serverAddr, 8000);
                    Log.d("UDP", "C: Sending: '" + (buff.length) + "'");

                    // send the UDP packet
                    socket.send(packet);

                    socket.close();

                    Log.d("UDP", "C: Sent.");
                    Log.d("UDP", "C: Done.");

                    }

                    catch (Exception e) {
                    Log.e("UDP", "C: Error", e);

                    }
                    try {
                    Thread.sleep(50);
                    }

                    catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    }
                    capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 300, 200));

            }
        };

```


Using a listener:

```javascript
DngCreatorrawImageReader.setOnImageAvailableListener(new OnImageAvailableListener() {
    @Override
    public void onImageAvailable(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        DngCreator dngCreator = new DngCreator(cc, mCaptureResult);
        // can set some metadata, like orientation, here
        dngCreator.writeImage(new FileOutputStream("filePath"), image);
        dngCreator.close();
        image.close();
    }
});
```
## Algorithm for image difference

Can perform image difference calculation-

Using this algorithm:

```javascript
while(preview)
imageDiff = previewLast.RAW(grayscale) - previewCurrent.RAW(grayscale);
  if(imageDiff != 0) //or some threshold
    //send a timestamp
  else()
    //do nothing
```  
  
## Server UDP (in Python)

We open a UDP socket, to listen for the image the Camera App sends:
```python
from PIL import Image
import cStringIO as StringIO
import binascii
import io
import socket

UDP_IP_DEST = IP Address
UDP_IP = '0.0.0.0'
UDP_PORT = 8000
imageList = []

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP
sock.bind((socket.gethostbyname(socket.gethostname()), UDP_PORT))
print "Open"
print str(socket.gethostbyname(socket.gethostname()))
``` 

Using the dhash function from Iconfinder:

```python
def dhash(image, hash_size = 8):
    # Grayscale and shrink the image.
    image = image.convert('L').resize(
                                      (hash_size + 1, hash_size),
                                      Image.ANTIALIAS,
                                      )
        
    pixels = list(image.getdata())
        
    # Compare adjacent pixels.
    difference = []
    for row in xrange(hash_size):
        for col in xrange(hash_size):
            pixel_left = image.getpixel((col, row))
            pixel_right = image.getpixel((col + 1, row))
            difference.append(pixel_left > pixel_right)
    
    # Convert the binary array to a hexadecimal string.
    decimal_value = 0
    hex_string = []
    for index, value in enumerate(difference):
        if value:
            decimal_value += 2**(index % 8)
        if (index % 8) == 7:
            hex_string.append(hex(decimal_value)[2:].rjust(2, '0'))
            decimal_value = 0

    return ''.join(hex_string)
``` 
The function performs grayscaling and then takes the pixels and turns them into a hex hash for each image.

We then store the image in imageList, a list of image hashes, indexed by the order in the queue received (1 Hz per image):

This is image in queue is then used to see if the previous image stored is different.

```python
for i in range(0,180):
        data, addr = sock.recvfrom(65536) # buffer size is 64kB
        if data:
        
            jpgFile = Image.open(io.BytesIO(data))
            #import StringIO
            print "received: " + str(jpgFile.bits) + " " +  str(jpgFile.size) + " " +  str(jpgFile.format) + " " + dhash(jpgFile) + "\n"
            sock.sendto("I got the message", (UDP_IP_DEST, UDP_PORT))
            imageList.append(dhash(jpgFile))
            print "count: " + str(i) + " current image hash: " + str(imageList[i])
            if(i > 0):
                print "count: " + str(i-1) + " previous image hash: " + str(imageList[i-1])
                currvalue = int(imageList[i],32)
                prevalue = int(imageList[i-1],32)
                print "Difference between the two hex values: " + str(abs(currvalue - prevalue))
                if (abs(currvalue - prevalue) < (264452523040700131969024)/2):
                    print "Same image."
                elif (((264452523040700131969024)/2 + 1) < abs(currvalue - prevalue) < (2644525230407001319690240)/6):
                    print "Some motion detected"
                elif (((2644525230407001319690240)/6 + 1) < abs(currvalue - prevalue)):
                    print "A lot of motion detected!!!"

``` 

## References

Image difference
http://blog.iconfinder.com/detecting-duplicate-images-using-python/

Android Camera API Tutorial
http://inducesmile.com/android/android-camera-api-tutorial/


  
