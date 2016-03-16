package io.github.samshore.project2t;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sam on 3/14/2016.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    // creating square object and define colors
    private Square square;
    private float red[] = {0.85f, 0.0f, 0.0f, 1.0f};
    private float green[] = {0.57843137f, 0.83921569f, 0.0f, 1.0f};

    // creating mvp matrix
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        // set bg color
        GLES20.glClearColor(0.935f, 0.935f, 0.935f, 1.0f);

        // initialize shapes
        square = new Square();
    }

    public void onDrawFrame(GL10 unused){
        // redraw bg color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        /* define camera view */
        // set the camera position
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // calculate projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // draw square
        square.draw(green, mMVPMatrix);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height){
        GLES20.glViewport(0, 0, width, height);

        // populate projection matrix with width/height ratio
        float ratio = (float) width/height;
        // apply projection transformation
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){

        // create a shader type (GLES20.GL_VERTEX_SHADER) or (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
