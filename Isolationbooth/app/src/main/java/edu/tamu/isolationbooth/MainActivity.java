package edu.tamu.isolationbooth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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

        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);

        TextView ipstring = (TextView) findViewById(R.id.ip);

        ipstring.setText(ipAddress);

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
        while(true) {
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
                Thread.sleep(1000);
                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "MyCameraApp");
                File pic = new File(mediaStorageDir.getPath() + File.separator + "IMG_.jpg");
                InputStream picstream = new FileInputStream(pic);
                Bitmap bmp = BitmapFactory.decodeStream(picstream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] buf = stream.toByteArray();
                os.writeInt(buf.length); //write message length
                os.write(buf);
                //shutdown
                os.close();
                is.close();
                //Thread.sleep(2000);
                connection.close();
                server.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}



