package com.example.fanchaozhou.project3;

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

    private FaceRecognizer faceRecognizer;

    public enum RECOGNIZER_TYPE{
        EIGENVECTOR,
        FISHER,
        LBPH
    }

    public MyFaceRecognizer(RECOGNIZER_TYPE type){
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
    }

    public void train(ArrayList<DBRecord> recList){
        int cnt;
        int[] labels = new int[ recList.size() ];
        CvMat[] photoMatArray = new CvMat[ recList.size() ];
        MatVector photoMatVector = new MatVector(recList.size());

        for(cnt = 0; cnt < photoMatArray.length; cnt++){
            CvMat grayImg = cvLoadImageM(Uri.parse(recList.get(cnt).fullsizePhotoUri).getPath(), CV_LOAD_IMAGE_GRAYSCALE);
            photoMatArray[ cnt ] = grayImg.clone();
            cvEqualizeHist(grayImg, photoMatArray[cnt]);
        }
        photoMatVector.put(photoMatArray);

        for(cnt = 0; cnt < labels.length; cnt++){
            labels[ cnt ] = (int)recList.get(cnt).typeID;
        }

        faceRecognizer.update(photoMatVector, labels);
    }

    public int predict(File faceImgFile){
        int label;
        CvMat face = cvLoadImageM(faceImgFile.getPath(), CV_LOAD_IMAGE_GRAYSCALE);
        CvMat grayFace = face.clone();

        cvEqualizeHist(face, grayFace);
        label = faceRecognizer.predict(grayFace);
        System.out.println(label);
        System.out.println(faceImgFile.getPath());
        return label;
    }

    public void load(String fileName){
        faceRecognizer.load(fileName);
    }

    public void save(String fileName){
        faceRecognizer.save(fileName);
    }
}
