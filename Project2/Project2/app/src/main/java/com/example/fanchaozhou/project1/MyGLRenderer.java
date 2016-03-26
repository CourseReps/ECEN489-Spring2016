package com.example.fanchaozhou.project1;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @class MyGLRenderer
 *
 * @brief This class renders the opengl graphics.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    // creating square object and define colors
    private Square square;

    // creating mvp matrix
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    /**
     * @fn onSurfacecreated
     * @brief This routine sets the background color and initializes the shape to be drawn
     */
    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        // set bg color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        // initialize shapes
        square = new Square();
    }

    /**
     * @fn onDrawFrame
     * @brief This routine renders the graphic. It redraws the background, creates an mvp matrix
     * (so that the square is square regardless of the viewport dimensions), and then draws the square
     */
    public void onDrawFrame(GL10 unused){
        // redraw bg color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        /* define camera view */
        // set the camera position
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // calculate projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // draw square
        square.draw(mMVPMatrix);
    }

    /**
     * @fn onSurfaceChanged
     * @brief This adjusts the mvp matrix when the dimensions of the gl viewport change.
     */
    public void onSurfaceChanged(GL10 unused, int width, int height){
        GLES20.glViewport(0, 0, width, height);

        // populate projection matrix with width/height ratio
        float ratio = (float) width/height;
        // apply projection transformation
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    /**
     * @fn loadShader
     * @brief loadShader creates a shader and compiles the shader code used to create the square.
     * It returns the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a shader type (GLES20.GL_VERTEX_SHADER) or (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
