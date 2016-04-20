package com.example.fanchaozhou.project3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Fanchao Zhou on 4/2/2016.
 */

public class MainActivity extends AppCompatActivity {

    private static final String FD_FILE_NAME = "haarcascade_frontalface_alt.xml";
    protected static ArrayList<DBRecord> recordList = null;
    protected static ArrayList<DBType> typeList = null;
    protected static boolean isInTraining;
    protected static MyFaceRecognizer faceRecognizer = null;
    protected static MyFaceDetector faceDetector = null;

    private RecordDBHelper dbHelper = new RecordDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        faceDetector = new MyFaceDetector(this, getResources().openRawResource(R.raw.haarcascade_frontalface_alt), FD_FILE_NAME);
        faceRecognizer = new MyFaceRecognizer(this, MyFaceRecognizer.RECOGNIZER_TYPE.LBPH);

        recordList = new ArrayList<>();
        typeList = new ArrayList<>();
        isInTraining = true;
        dbHelper.readAllTypes(typeList);
        dbHelper.readAllRecs(recordList);

        if(savedInstanceState == null){//The app always starts from the page where the user can choose to take a photo
            ImageFragment imageFrag= new ImageFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, imageFrag)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dbHelper.close();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
