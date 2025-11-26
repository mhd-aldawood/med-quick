package com.example.kotlintest.screens.ecg.views;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VerticalECGRenderer implements GLSurfaceView.Renderer {

    // --- ECG data queue ---
    private final ConcurrentLinkedQueue<Short> ecgQueue;

    // --- OpenGL handles ---
    private int program;
    private int vPositionHandle;
    private int colorHandle;
    private int matrixHandle;

    // --- Projection matrix ---
    private final float[] projectionMatrix = new float[16];

    // --- drawing buffers (line strip) ---
    private static final int MAX_POINTS = 4096;
    private FloatBuffer vertexBuffer;

    // --- vertical mode parameters ---
    private int leadCount = 6;
    private float[] centerX;     // X center of each lead column
    private float[] lastX;       // previous X position
    private float[] lastY;       // previous Y position

    private float viewWidth, viewHeight;
    private float normalizeW, normalizeH;

    private float amplitudeScale = 1.0f;
    private int gain = 10;
    private float perMM = 8;

    private float pointerY;   // moves bottom → top
    private float stepY = 0.006f;  // vertical speed = 1 pixel worth in NDC

    public VerticalECGRenderer(ConcurrentLinkedQueue<Short> inputQueue) {
        this.ecgQueue = inputQueue;

        ByteBuffer bb = ByteBuffer.allocateDirect(MAX_POINTS * 2 * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
    }

    // --------------------- SHADERS ---------------------
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec2 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vec4(vPosition, 0.0, 1.0);" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    // --------------------- INIT ---------------------
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 1);

        int vShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        GLES20.glLinkProgram(program);

        vPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        matrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
    }

    // --------------------- SIZE CHANGE ---------------------
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        viewWidth = width;
        viewHeight = height;
        GLES20.glViewport(0, 0, width, height);

        float aspect = (float) width / height;
        Matrix.orthoM(projectionMatrix, 0,
                0, width,     // left → right
                0, height,    // bottom → top
                -1, 1);

        centerX = new float[leadCount];
        lastX = new float[leadCount];
        lastY = new float[leadCount];

        float columnWidth = width / (float) leadCount;

        for (int i = 0; i < leadCount; i++) {
            centerX[i] = (i * columnWidth) + (columnWidth / 2f);
            lastX[i] = centerX[i];
            lastY[i] = 0;
        }

        pointerY = 0;
    }

    // --------------------- MAIN DRAW LOOP ---------------------
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);
        GLES20.glUniform4f(colorHandle, 0f, 1f, 0f, 1f); // GREEN ECG

        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, projectionMatrix, 0);

        // Move pointer (bottom→top)
        pointerY += 3;  // 3 pixels frame movement
        if (pointerY > viewHeight) {
            pointerY = 0;
        }

        // For each lead column
        for (int i = 0; i < leadCount; i++) {

            Short val = ecgQueue.poll();
            if (val == null) continue;

            float amp = val * 0.001f * gain * perMM * amplitudeScale;

            float x = centerX[i] + amp;
            float y = pointerY;

            // Prepare line vertices
            vertexBuffer.clear();
            vertexBuffer.put(lastX[i]);
            vertexBuffer.put(lastY[i]);

            vertexBuffer.put(x);
            vertexBuffer.put(y);
            vertexBuffer.position(0);

            GLES20.glEnableVertexAttribArray(vPositionHandle);
            GLES20.glVertexAttribPointer(
                    vPositionHandle,
                    2,
                    GLES20.GL_FLOAT,
                    false,
                    0,
                    vertexBuffer
            );

            GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
            GLES20.glDisableVertexAttribArray(vPositionHandle);

            // update last positions
            lastX[i] = x;
            lastY[i] = y;
        }
    }
}
