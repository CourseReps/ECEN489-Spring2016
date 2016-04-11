package edu.tamu.isolationbooth;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tbranyon on 4/9/16.
 */
public class ServerThread implements Runnable
{
    private Activity mActivity;
    private final int portnumber = 2000;

    public ServerThread(Activity a)
    {
        mActivity = a;
    }

    public void run()
    {
        try {
            ServerSocket server = new ServerSocket(portnumber, 1);
            server.setReuseAddress(true);
            Socket connection = server.accept(); //wait for connect
            ObjectOutputStream os = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(connection.getInputStream());
            String cmd = "";
            while (!cmd.equals("get_pic"))
                cmd = (String) is.readObject();

            //@TODO get picture

            //read picture file and send
            //File root = Environment.getExternalStorageDirectory();
            ImageView IV = (ImageView) mActivity.findViewById(R.id.imgview);
            Bitmap bmp = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/Screenshots/Screenshot_2016-04-08-13-47-08.png");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
    }
}
