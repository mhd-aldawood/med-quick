package com.example.kotlintest.screens.ecg.views;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.microedition.khronos.opengles.GL10;

public class ECGRenderer implements GLSurfaceView.Renderer {

    // Lead data queue (incoming ECG signals)
    public final ConcurrentLinkedQueue<Short> waveQueue = new ConcurrentLinkedQueue<>();

    private int screenWidth, screenHeight;

    private FloatBuffer vertexBuffer;
    private float[] vertices;

    private int program;
    private int positionHandle;
    private int colorHandle;

    private float scrollX = 0f;
    private float xStep = 0.002f;        // Horizontal speed
    private int maxPoints = 5000;

    private int leadCount = 6;
    private float[] centerYs;

    private float gain = 10f;
    private float pixelsPerMM = 8f;
    private float amplitudeScale = 1f;

//    @Override
//    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//
//    }

    @Override
    public void onSurfaceCreated(GL10 gl10, javax.microedition.khronos.egl.EGLConfig eglConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);

        setupShader();
        setupBuffers();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenWidth = width;
        screenHeight = height;

        GLES20.glViewport(0, 0, width, height);

        centerYs = new float[leadCount];
        float perLead = 2f / leadCount;

        for (int i = 0; i < leadCount; i++) {
            centerYs[i] = 1f - (i * perLead + perLead / 2);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        scrollX -= xStep;
        if (scrollX < -2f) scrollX = 0f;

        drawECG();
    }


    /**
     * GPU buffer setup
     */
    private void setupBuffers() {
        vertices = new float[maxPoints * 2];

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
    }

    /**
     * Draw all leads
     */
    private void drawECG() {
        GLES20.glUseProgram(program);

        while (waveQueue.size() >= leadCount) {
            for (int i = 0; i < leadCount; i++) {
                Short v = waveQueue.poll();
                float yVal = v * 0.001f * gain * pixelsPerMM * amplitudeScale;

                pushPoint(centerYs[i], yVal);
            }
        }

        vertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(
                positionHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                2 * 4,
                vertexBuffer
        );

        GLES20.glUniform4f(colorHandle, 0f, 1f, 0f, 1f);

        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertices.length / 2);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    /**
     * Add new point into GPU buffer
     */
    private void pushPoint(float centerY, float value) {
        float y = centerY - (value / screenHeight);

        for (int i = 2; i < vertices.length; i++)
            vertices[i - 2] = vertices[i];

        vertices[vertices.length - 2] = scrollX;
        vertices[vertices.length - 1] = y;

        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }


    /**
     * Shader Setup
     */
    private void setupShader() {
        String vertexShaderCode =
                "attribute vec2 vPosition;" +
                        "void main() {" +
                        "  gl_Position = vec4(vPosition, 0.0, 1.0);" +
                        "}";

        String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";

        int vShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        GLES20.glLinkProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
    }

    private int loadShader(int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
