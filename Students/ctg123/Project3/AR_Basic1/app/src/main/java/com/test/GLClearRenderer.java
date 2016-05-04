package com.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLClearRenderer implements Renderer {
	
	 private Sphere mSphere = new Sphere();
     private float mSphereRotation;
    // Ambient light
    private final float[] mat_ambient = { 0.25f, 0.25f, 0.25f, 0.25f }; // 0.2f, 0.3f, 0.4f, 1.0f
    private FloatBuffer mat_ambient_buf;
    // Parallel incident light
    private final float[] mat_diffuse = { 0.4f, 0.6f, 0.8f, 1.0f };
    private FloatBuffer mat_diffuse_buf;
    // The highlighted area
    private final float[] mat_specular = { 0.2f * 0.25f, 0.2f * 0.25f, 0.2f * 0.25f, 1.0f }; // 0.2f * 0.4f, 0.2f * 0.6f, 0.2f * 0.8f, 1.0f
    private FloatBuffer mat_specular_buf;

    public void onDrawFrame( GL10 gl ) {
        // This method is called per frame, as the name suggests.
        // For demonstration purposes, I simply clear the screen with a random translucent gray.
        //float c = 1.0f / 256 * ( System.currentTimeMillis() % 256 );
        //gl.glClearColor( c, c, c, 0.5f );
        //gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);        
        gl.glLoadIdentity();

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);

        // Texture of Material
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient_buf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse_buf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mat_specular_buf);
        // Specular exponent 0-128 less rough
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 96.0f);

        gl.glTranslatef(0.0f, 0.0f, -10.0f);
        gl.glRotatef(mSphereRotation, 0.25f, 0.25f, 0.25f);
            
        mSphere.draw(gl);
           
        gl.glLoadIdentity();                                    
            
        mSphereRotation -= 0.15f;


    }
 
    public void onSurfaceChanged( GL10 gl, int width, int height ) {
        // This is called whenever the dimensions of the surface have changed.
        // We need to adapt this change for the GL viewport.
        gl.glViewport(0, 0, width, height);
        //gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
 
    public void onSurfaceCreated( GL10 gl, EGLConfig config ) {
        // No need to do anything here.
    	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);

        initBuffers();
    }

    private void initBuffers() {
        ByteBuffer bufTemp = ByteBuffer.allocateDirect(mat_ambient.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        mat_ambient_buf = bufTemp.asFloatBuffer();
        mat_ambient_buf.put(mat_ambient);
        mat_ambient_buf.position(0);

        bufTemp = ByteBuffer.allocateDirect(mat_diffuse.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        mat_diffuse_buf = bufTemp.asFloatBuffer();
        mat_diffuse_buf.put(mat_diffuse);
        mat_diffuse_buf.position(0);

        bufTemp = ByteBuffer.allocateDirect(mat_specular.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        mat_specular_buf = bufTemp.asFloatBuffer();
        mat_specular_buf.put(mat_specular);
        mat_specular_buf.position(0);
    }
}