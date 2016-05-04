package com.test;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CameraProjectActivity extends Activity{
    /** Called when the activity is first created. */
    //@Override

    private  TextView text, text1, text2,text3, text4, text5;

    public void onCreate( Bundle savedInstanceState ) {

        // Next, we disable the application's title bar...
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        // ...and the notification bar. That way, we can use the full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraview);


        // Now let's create an OpenGL surface.
        GLSurfaceView glView = new GLSurfaceView( this );
        // To see the camera preview, the OpenGL surface has to be created translucently.
        // See link above.
        glView.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
        glView.getHolder().setFormat( PixelFormat.TRANSLUCENT );
        // The renderer will be implemented in a separate class, GLView, which I'll show next.
        glView.setRenderer( new GLClearRenderer() );
        // Now set this as the main view.
        setContentView( glView );
     
        // Now also create a view which contains the camera preview...
        CameraView cameraView = new CameraView( this );
        // ...and add it, wrapping the full screen size.
        addContentView( cameraView, new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
        // You can use a Framelayout to hold the surfaceview
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(glView);

        // Here we declare the parameters to be seen on the screen with the 3D rendered model.
        //text = (TextView) findViewById(R.id.distance);
        //text1 = (TextView) findViewById(R.id.ecliptic_lat);
        //text2 = (TextView) findViewById(R.id.ecliptic_long);
        //text3 = (TextView) findViewById(R.id.distance);
        //text4 = (TextView) findViewById(R.id.distance);
        //text5 = (TextView) findViewById(R.id.distance);

        // Then create a layout to hold everything, for example a RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.addView(frameLayout);
        relativeLayout.addView(text);  // show the distance in km
        relativeLayout.addView(text1); // show the Ecliptic latitude of the moon.
        relativeLayout.addView(text2); // show the Ecliptic longitude of the moon.
        //relativeLayout.addView(text3); // Yaw?
        //relativeLayout.addView(text4); // Pitch?
        //relativeLayout.addView(text5); // Roll?

    }
public void openCamera(View view){
    Intent intent = new Intent(this, CameraProjectActivity.class);
    }

}