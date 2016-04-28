package com.example.fanchaozhou.project3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
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
        faceRecognizer = new MyFaceRecognizer(MyFaceRecognizer.RECOGNIZER_TYPE.LBPH);

        File trainingXmlFile = new File(getFilesDir(), getString(R.string.fr_file_name));
        try{
            FileInputStream fileInputStream = new FileInputStream(trainingXmlFile);
            faceRecognizer.load(trainingXmlFile.getPath());
            fileInputStream.close();
        } catch (Exception e){
            System.out.println(e);
        }

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
