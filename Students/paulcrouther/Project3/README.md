#Motion Detection

##This is the project for motion detection from the camera. 

Using Camera2 API, can make a file with the image, with RAW or YUV:

http://willowtreeapps.com/blog/camera2-and-you-leveraging-android-lollipops-new-camera/

```javascript

ImageReader rawImageReader = ImageReader.newInstance(rawWidth, rawHeight, ImageFormat.RAW_SENSOR, 1);
rawImageReader.setOnImageAvailableListener(new OnImageAvailableListener() {
    @Override
    public void onImageAvailable(ImageReader reader) {
        // save raw
    }
});
 
ImageReader jpegImageReader = ImageReader.newInstance(jpegWidth, jpegHeight, ImageFormat.JPEG, 1);
jpegImageReader.setOnImageAvailableListener(new OnImageAvailableListener() {
    @Override
    public void onImageAvailable(ImageReader reader) {
        // save jpeg
    }
});
 
Surface previewSurface = new Surface(mPreviewSurfaceTexture);
Surface rawCaptureSurface = rawImageReader.getSurface();
Surface jpegCaptureSurface = jpegImageReader.getSurface();
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
## Algorithm

Can perform a few different tasks:

- Grab an image from preview
- Use RAW or YUV format to collect picture information
- Use the grayscale from the YUV or RAW image to get the peaks from motion
- Perform a comparison of the RAW/YUV grayscale values of the previous preview image with the current.
- Store temporarily the images and perform a diff:

```javascript
while(preview)
imageDiff = previewLast.RAW(grayscale) - previewCurrent.RAW(grayscale);
  if(imageDiff != 0) //or some threshold
    //send a timestamp
  else()
    //do nothing
```  
  
## Server side

Send the timestamp and a "Motion detected String" in JSON format to a server.
Looking at REST API, and some other options.

## Objectives/Deliverables

* Finish the algorithm
* Work on sending to server
* Experiment with values

## Future Stuff

* Maybe integrate actual object detection with the OpenCV ML framework
* Make things look pretty


  
