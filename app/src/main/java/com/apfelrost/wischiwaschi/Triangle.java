package com.apfelrost.wischiwaschi;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Kris on 18.01.2015.
 */
public class Triangle {

    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    private final static int COORDS_PER_VERTEX = 3;
    private final static float triangleCoords[] =
            {   // in counterclockwise order:
                    0.0f,  0.622008459f, 0.0f, // top
                    -0.5f, -0.311004243f, 0.0f, // bottom left
                    0.5f, -0.311004243f, 0.0f  // bottom right
            };

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    // shader stuff
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    int program;

    public Triangle()
    {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                this.triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        this.vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        this.vertexBuffer.put(this.triangleCoords);
        // set the buffer to read the first coordinate
        this.vertexBuffer.position(0);

        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, this.vertexShaderCode);
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, this.fragmentShaderCode);

        this.program = GLES20.glCreateProgram();
        GLES20.glAttachShader(this.program, vertexShader);
        GLES20.glAttachShader(this.program, fragmentShader);
        GLES20.glLinkProgram(this.program);
    }

    public void draw()
    {
        GLES20.glUseProgram(this.program);
        int positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                                        false, this.vertexStride, this.vertexBuffer) ;

        int colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
