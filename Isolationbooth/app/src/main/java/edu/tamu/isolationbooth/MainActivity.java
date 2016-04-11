package edu.tamu.isolationbooth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;

public class MainActivity extends AppCompatActivity {

    final int portnumber = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainloop();
    }

    private void mainloop()
    {
        while(true)
        {
            try {
                ServerSocket server = new ServerSocket(portnumber, 1);
                Socket connection = server.accept(); //wait for connect
                ObjectOutputStream os = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(connection.getInputStream());
                String cmd = "";
                while (!cmd.equals("get_pic"))
                    cmd = (String) is.readObject();

                //DO IMAGE CAPTURE HERE
                //@TODO

                //read picture file and send
                //File root = Environment.getExternalStorageDirectory();
                ImageView IV = (ImageView) findViewById(R.id.imgview);
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
}
