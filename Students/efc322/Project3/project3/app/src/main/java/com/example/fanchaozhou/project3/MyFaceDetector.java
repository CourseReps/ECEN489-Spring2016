package com.example.fanchaozhou.project3;

import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_objdetect.CascadeClassifier;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Fanchao Zhou on 4/17/2016.
 */
public class MyFaceDetector{

    /*
    Flag=0: The classifier skips all the flat areas
    Flag=1: Only the photo(not the classfier) is scaled during the classification
    Flag=2: The classifier only returns the biggest object
    */
    private static final int FLAG = 2;
    private static final int BUF_SIZE = 2048;
    private static final int MIN_NEIGHBORS = 2;
    private static final double SCALOR = 1.05;
    private Context mContext;
    private CascadeClassifier faceDetector;

    public MyFaceDetector(Context curContext, final InputStream isFdXmlFile, final String fdFileName){

        mContext = curContext;

        Thread xmlReadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // load cascade file from application resources
                    File fdXmlFile =  new File(mContext.getCacheDir(), fdFileName);
                    FileOutputStream osFdXmlFile = new FileOutputStream(fdXmlFile);

                    byte[] buffer = new byte[ BUF_SIZE ];
                    int bytesRead;
                    int totalBytes = 0;
                    while ((bytesRead = isFdXmlFile.read(buffer)) != -1) {
                        osFdXmlFile.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;
                    }
                    isFdXmlFile.close();
                    osFdXmlFile.close();

                    faceDetector = new CascadeClassifier(fdXmlFile.getPath());
                    fdXmlFile.delete();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        xmlReadingThread.start();
    }

    public CvRect detectFace(File image){
        CvMat imageMtx = cvLoadImageM(image.getPath(), CV_LOAD_IMAGE_GRAYSCALE);
        CvMat grayImageMtx = imageMtx.clone();
        CvRect face = new CvRect();
        CvSize minSize = new CvSize();
        CvSize maxSize = new CvSize();

        minSize.height(imageMtx.rows() / 5);
        minSize.width(imageMtx.cols() / 5);
        maxSize.height(imageMtx.rows() / 6 * 5);
        maxSize.width(imageMtx.cols() / 6 * 5);

        cvEqualizeHist(imageMtx, grayImageMtx);

        if(faceDetector == null){
            return null;
        }else{
            faceDetector.detectMultiScale(
                    grayImageMtx,
                    face,
                    SCALOR,
                    MIN_NEIGHBORS,
                    FLAG,
                    minSize,
                    maxSize);

            return face;
        }
    }
}
