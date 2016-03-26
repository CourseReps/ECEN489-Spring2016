package com.example.fanchaozhou.project1;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @class MyGLSurfaceView
 *
 * @brief MyGLSurfaceView is a container for the gl graphic. It gives the graphic a viewport
 * on the screen and sets the renderer
 */
public class MyGLSurfaceView extends GLSurfaceView {

    /**
     * @fn MyGLSurfaceView
     * @brief MyGLSurfaceView constructor. This creates the GLSurfaceView, sets the OpenGL ES version,
     * and sets the renderer
     */
    public MyGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);

        final MyGLRenderer mRenderer;

        // OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        // Set renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

}
