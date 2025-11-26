package com.example.kotlintest.screens.ecg.views

import android.content.Context
import android.opengl.GLSurfaceView


class ECGGLSurfaceView(context: Context?) : GLSurfaceView(context) {
    val renderer: ECGRenderer

    init {
        setEGLContextClientVersion(2) // OpenGL ES 2.0
        this.renderer = ECGRenderer()
        setRenderer(this.renderer)

        // Render continuously (~60/120fps depending on device)
        setRenderMode(RENDERMODE_CONTINUOUSLY)
    }
}
/**How TO use
 *
 *      glView = new ECGGLSurfaceView(this);
 *         setContentView(glView);
 *
 *         // Example: Push data continuously
 *         new Thread(() -> {
 *             Random r = new Random();
 *             while (true) {
 *                 // generate fake ECG samples
 *                 for (int i = 0; i < 6; i++) {
 *                     glView.getRenderer().waveQueue.add((short) r.nextInt(2000));
 *                 }
 *
 *                 try { Thread.sleep(2); } catch (Exception ignore) {}
 *             }
 *         }).start();
 * */
