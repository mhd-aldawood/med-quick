package com.example.kotlintest.screens.ecg.views;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.util.concurrent.ConcurrentLinkedQueue;

public class VerticalECGView extends GLSurfaceView {

    public final VerticalECGRenderer renderer;

    public VerticalECGView(Context ctx, ConcurrentLinkedQueue<Short> queue) {
        super(ctx);

        setEGLContextClientVersion(2);

        renderer = new VerticalECGRenderer(queue);
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
