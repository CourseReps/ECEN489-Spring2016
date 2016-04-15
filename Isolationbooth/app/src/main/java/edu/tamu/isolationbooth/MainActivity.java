package edu.tamu.isolationbooth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public Camera mCamera;
    public CameraPreview mCameraPreview;
    private final int portnumber = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);
        Thread t = new Thread(){
            public void run()
            {
                serverThread();
            }
        };
        t.start();
    }
    private Camera getCameraInstance () {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {System.err.println(e);}
        return camera;
    }

    public void serverThread()
    {
        //while(true) {
            try {
                ServerSocket server = new ServerSocket(portnumber, 1);
                server.setReuseAddress(true);
                Socket connection = server.accept(); //wait for connect
                final ObjectOutputStream os = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(connection.getInputStream());
                String cmd = "";
                while (!cmd.equals("get_pic"))
                    cmd = (String) is.readObject();
                System.out.println("Command received");

                Camera.PictureCallback mPicture = new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            File mediaStorageDir = new File(
                                    Environment
                                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                    "MyCameraApp");
                            File picturefile = new File(mediaStorageDir.getPath() + File.separator
                                    + "IMG_.jpg");
                            FileOutputStream picture = new FileOutputStream(picturefile);
                            picture.write(data);
                            picture.close();
                        } catch (Exception e) {System.err.println(e);}
                    }
                };

                mCamera.takePicture(null, null, mPicture);
                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "MyCameraApp");
                Bitmap bmp = BitmapFactory.decodeFile(mediaStorageDir.getPath() + File.separator
                        + "IMG_.jpg");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] buf = stream.toByteArray();
                os.writeInt(buf.length); //write message length
                os.write(buf);
                //shutdown
                os.close();
                is.close();
                connection.close();
                server.close();
            } catch (Exception e) {
                System.out.println(e);
            }

        //}
    }
}



