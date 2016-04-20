package com.example.fanchaozhou.project3;

import android.app.Activity;
import android.net.Uri;

import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;
import com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_contrib.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Fanchao Zhou on 4/18/2016.
 */
public class MyFaceRecognizer{

    private Activity mActivity;
    private FaceRecognizer faceRecognizer;
    private RECOGNIZER_TYPE type;

    public enum RECOGNIZER_TYPE{
        EIGENVECTOR,
        FISHER,
        LBPH
    }

    public MyFaceRecognizer(Activity mActivity, RECOGNIZER_TYPE type){
        this.mActivity = mActivity;
        this.type = type;
    }

    public void train(ArrayList<DBRecord> recList){
        int cnt;
        int[] labels = new int[ recList.size() ];
        CvMat[] photoMatArray = new CvMat[ recList.size() ];
        MatVector photoMatVector = new MatVector(recList.size());

        switch(type){
            case EIGENVECTOR:
                faceRecognizer = createEigenFaceRecognizer();
                break;
            case FISHER:
                faceRecognizer = createFisherFaceRecognizer();
                break;
            case LBPH:
                faceRecognizer = createLBPHFaceRecognizer();
                break;
        }

        for(cnt = 0; cnt < photoMatArray.length; cnt++){
            CvMat grayImg = cvLoadImageM(Uri.parse(recList.get(cnt).fullsizePhotoUri).getPath(), CV_LOAD_IMAGE_GRAYSCALE);
            photoMatArray[ cnt ] = grayImg.clone();
            cvEqualizeHist(grayImg, photoMatArray[cnt]);
        }
        photoMatVector.put(photoMatArray);

        for(cnt = 0; cnt < labels.length; cnt++){
            labels[ cnt ] = (int)recList.get(cnt).typeID;
        }

        faceRecognizer.train(photoMatVector, labels);
    }

    public int predict(File faceImgFile){
        int label;
        CvMat face = cvLoadImageM(faceImgFile.getPath(), CV_LOAD_IMAGE_GRAYSCALE);
        CvMat grayFace = face.clone();

        cvEqualizeHist(face, grayFace);
        label = faceRecognizer.predict(grayFace);
        return label;
    }
}
