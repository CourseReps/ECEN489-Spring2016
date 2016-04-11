package com.example.kyle.secretmessenger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView capturedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_pic_lay);

        capturedPhoto = (ImageView) findViewById(R.id.image);

//        Paint myPaint = new Paint();
//        myPaint.setColor(Color.rgb(0, 0, 0));
//        myPaint.setStrokeWidth(10);
//        drawRect(100, 100, 200, 200, myPaint);
//        myPaint.setStyle(Paint.Style.STROKE);


    }

    public void takePhoto(View view){
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK){
            //Toast.makeText(this, "Picture taken successfully",Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            Bitmap photoBitMap = (Bitmap) extras.get("data");
            capturedPhoto.setImageBitmap(photoBitMap);
        }
    }
}
