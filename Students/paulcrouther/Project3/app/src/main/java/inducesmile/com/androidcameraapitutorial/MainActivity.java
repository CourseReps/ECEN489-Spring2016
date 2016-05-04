package inducesmile.com.androidcameraapitutorial;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    private ImageSurfaceView mImageSurfaceView;
    private Camera camera;

    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;

    private String udpOutputData = "hello";
    //Boolean sendUdp = false;
    //private String s = "hello from app";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cameraPreviewLayout = (FrameLayout)findViewById(R.id.camera_preview);
        capturedImageHolder = (ImageView)findViewById(R.id.captured_image);

        camera = checkDeviceCamera();
        mImageSurfaceView = new ImageSurfaceView(MainActivity.this, camera);
        cameraPreviewLayout.addView(mImageSurfaceView);




    //UdpSendThread.start();
        Button captureButton = (Button)findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                try {
                    // get server name
                    //final InetAddress serverAddr = InetAddress.getByName("192.168.43.197");
                    final InetAddress serverAddr = InetAddress.getByName("172.31.99.109"); //pc
                    //final InetAddress serverAddr = InetAddress.getByName("172.31.99.46");//tablet at starbucks

                    Log.d("UDP", "C: Connecting...");
                    // create new UDP socket
                    final DatagramSocket socket = new DatagramSocket();
                //sendUdp = true;
                // prepare data to be sent
                byte[] buf = udpOutputData.getBytes();

                // create a UDP packet with data and its destination ip & port
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 8000);
                Log.d("UDP", "C: Sending: '" + new String(buf) + "'");

                // send the UDP packet
                socket.send(packet);
                    //camera.takePicture(null, null, pictureCallback);

                socket.close();

                Log.d("UDP", "C: Sent.");
                Log.d("UDP", "C: Done.");

                }

                catch (Exception e) {
                    Log.e("UDP", "C: Error", e);

                }
                try {
                    Thread.sleep(100);
                }

                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
                Runnable picRunnable = new Runnable() {
                    @Override
                    public void run() {
                        camera.takePicture(null, null, pictureCallback);

                    }
                };
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(picRunnable,0,1, TimeUnit.SECONDS);
                //}




                //sendUdp(s);
                //sendUdp = false;
                //System.out.println("On Click" + sendUdp.toString());
                //camera.takePicture(null, null, pictureCallback);

            }
        });


    }


    private Camera checkDeviceCamera(){
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }
    //Thread picThread = new Thread() {
        PictureCallback pictureCallback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap == null) {
                    Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                try{
                    //final InetAddress serverAddr = InetAddress.getByName("192.168.42.20");
                    //final InetAddress serverAddr = InetAddress.getByName("192.168.43.197");
                    final InetAddress serverAddr = InetAddress.getByName("10.202.107.58");
                    //final InetAddress serverAddr = InetAddress.getByName("172.31.99.109"); //pc
                    //final InetAddress serverAddr = InetAddress.getByName("172.31.99.46");//tablet at starbucks

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

                //camera.takePicture(null, null, pictureCallback);
                    //System.out.println("Picture callback" + sendUdp.toString());
                    capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 300, 200));
                    //sendUdp(s);
                    //sendUdp = false;
                    //sendUdp(s);
                    //}

            }
        };
    //};

    /*private void oldOpenCamera() {
        //Camera mCamera = null;
        try {
            //mCamera = Camera.open(1);
            camera.open(1);
        }
        catch (RuntimeException e) {
            Log.e("UDP", "failed to open front camera");
        }
    }

    private void newOpenCamera() {
        if (mThread == null) {
            mThread = new CameraHandlerThread();
        }

        synchronized (mThread) {
            mThread.openCamera();
        }
    }
    private CameraHandlerThread mThread = null;
    private static class CameraHandlerThread extends HandlerThread {
        android.os.Handler mHandler = null;

        CameraHandlerThread() {
            super("CameraHandlerThread");
            start();
            mHandler = new android.os.Handler(getLooper());
        }

        synchronized void notifyCameraOpened() {
            notify();
        }

        void openCamera() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //oldOpenCamera();
                    notifyCameraOpened();

                }
            });
            try {
                wait();
            }
            catch (InterruptedException e) {
                Log.w("UDP", "wait was interrupted");
            }
        }
    }*/

    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*android.os.Handler mainHandler = new android.os.Handler(Looper.getMainLooper());

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            mainHandler.post(myRunnable);
        } // This is your code
    };*/


    /*
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // Get the Camera instance as the activity achieves full user focus
        //if (checkDeviceCamera() == null) {
            //initializeCamera(); // Local method to handle camera init
        //}
        sendUdp = false;
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        //if (checkDeviceCamera() != null) {
          //  checkDeviceCamera().release();
            //camera = null;
        //}
        //Thread.interrupt();
        sendUdp = true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //MyThread.interrupt();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /*public void initializeCamera() {
        camera.startPreview();
    }*/
    //-----UDP send thread
    //public class UdpSendThread extends AsyncTask<String, Void, String> {
    //class UdpSendThread implements Runnable {
    /*Thread UdpSendThread = new Thread(){
    //Thread udpSendThread = new Thread(new Runnable() {

        @Override
        public void run() {
        //protected String doInBackground(String... idpAddr){


            while (sendUdp == true) {

                try {
                    Thread.sleep(100);
                }

                catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                if (sendUdp) {

                    try {

                        //camera.takePicture(null, null, pictureCallback);
                        // get server name
                        InetAddress serverAddr = InetAddress.getByName("192.168.43.197");
                        //InetAddress serverAddr = InetAddress.getByName("172.31.99.109");
                        Log.d("UDP", "C: Connecting...");

                        // create new UDP socket
                        DatagramSocket socket = new DatagramSocket();

                        // prepare data to be sent
                        byte[] buf = udpOutputData.getBytes();

                        // create a UDP packet with data and its destination ip & port
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 8000);
                        Log.d("UDP", "C: Sending: '" + new String(buf) + "'");

                        // send the UDP packet
                        socket.send(packet);
                        //camera.takePicture(null, null, pictureCallback);

                        socket.close();

                        Log.d("UDP", "C: Sent.");
                        Log.d("UDP", "C: Done.");

                        //Runnable task = getTask();
                        //new Handler(Looper.getMainLooper()).post(task);


                    }

                    catch (Exception e) {
                        Log.e("UDP", "C: Error", e);

                    }

                    try {
                        Thread.sleep(100);
                    }

                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    sendUdp = false;

                }

            }
        }

    };
    //UdpSendThread.start();


    public void sendUdp(String udpMsg) {

        udpOutputData = udpMsg;
        sendUdp = true;
        //Thread.interrupted();
        //sendUdp = false;

    }*/

}
