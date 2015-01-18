package com.apfelrost.wischiwaschi;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kris on 17.01.2015.
 */


public class GLRenderer implements GLSurfaceView.Renderer
{

    private boolean mSurfaceCreated;
    private int mWidth;
    private int mHeight;
    private Triangle triangle;

    public GLRenderer()
    {
        mSurfaceCreated = false;
        mWidth = -1;
        mHeight = -1;
    }

    @Override
    public void onSurfaceCreated(GL10 notUsed,
                                 EGLConfig config)
    {
        mSurfaceCreated = true;
        mWidth = -1;
        mHeight = -1;
        triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 notUsed, int width,
                                 int height)
    {
        if (!mSurfaceCreated && width == mWidth
                && height == mHeight)
        {
            return;
        }

        mWidth = width;
        mHeight = height;

        //  onCreate(mWidth, mHeight, mSurfaceCreated);
        mSurfaceCreated = false;
    }

    @Override
    public void onDrawFrame(GL10 notUsed)
    {
        GLES20.glClearColor(1f,0f,0f,1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        this.triangle.draw();
    }

    public static int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}