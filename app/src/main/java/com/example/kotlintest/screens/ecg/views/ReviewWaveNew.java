package com.example.kotlintest.screens.ecg.views;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Process;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Vertical-scrolling version of original ReviewWave:
 * <p>
 * TIME AXIS  : vertical (bottom → top)
 * AMPLITUDE  : horizontal around center of each column
 * LABELS     : bottom of each column
 * <p>
 * — Same logic, variable names, drawing rules, batching,
 * sampling, clearing strips, everything preserved exactly.
 */
public class ReviewWaveNew extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = ReviewWave.class.getSimpleName();

    private int mLeadCount = 6;

    // scaling
    private float perMillmeter = 8;
    public static final int SAMPLE_RATE = 1000;

    private int mViewWidth;
    private int mViewHeight;

    private float amplitudeScale = 1.0f;

    public void setAmplitudeScale(float s) {
        amplitudeScale = Math.max(0, s);
    }

    private float paddingTop = 30;
    private float paddingBottom = 30;
    private float paddingLeft = 0;
    private float paddingRight = 0;

    private Paint mPaint;
    private SurfaceHolder mHolder;

    // waveform drawing
    private float oneStep; // was oneXwidth (now vertical step)
    private int gain = 10;
    private int jumpPoint;

    // column rectangles (one per lead)
    private RectF[] mLeadRectFs;

    // center X for amplitude
    private float[] mCenterXs;

    // history (previous coordinate)
    private float[] mLastAmpXs;  // previous X coordinate
    private float[] mLastTimeYs; // previous Y coordinate

    private float[] mCurAmpXs;
    private float[] mCurTimeYs;

    // wave data
    private ConcurrentLinkedQueue<Short> mWaveDatas;
    private String[] mLeadNames;

    private boolean refreshWave = false;
    private int mLeadNameWidth;
    private float mFontHeight;

    public ReviewWaveNew(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        mLeadNames = new String[]{"I", "II", "III", "aVR", "aVL", "aVF"};

        perMillmeter = 0.014f * 200 * dm.widthPixels / (20.5f * 25);
        jumpPoint = 1;

        // SAME LOGIC: this is now vertical step (Y)
        oneStep = perMillmeter * 25f / 200 * jumpPoint;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(2f);

        float textSize = 15 * dm.scaledDensity;
        mPaint.setTextSize(textSize);
        mLeadNameWidth = (int) mPaint.measureText("aVF");

        FontMetrics fm = mPaint.getFontMetrics();
        mFontHeight = (fm.descent - fm.ascent);

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    // -------------------------------------------------------------------------
    // Prepare columns exactly like the old "meanSort()" but rotated
    // -------------------------------------------------------------------------
    private void prepareColumns() {

        mLeadRectFs = new RectF[mLeadCount];
        mCenterXs = new float[mLeadCount];

        mLastAmpXs = new float[mLeadCount];
        mLastTimeYs = new float[mLeadCount];

        mCurAmpXs = new float[mLeadCount];
        mCurTimeYs = new float[mLeadCount];

        float availableWidth = mViewWidth - paddingLeft - paddingRight;
        float columnWidth = availableWidth / mLeadCount;

        float top = paddingTop;
        float bottom = mViewHeight - paddingBottom;

        for (int i = 0; i < mLeadCount; i++) {
            float left = paddingLeft + i * columnWidth;
            float right = left + columnWidth;

            RectF r = new RectF(left, top, right, bottom);
            mLeadRectFs[i] = r;

            float cx = (left + right) / 2f;
            mCenterXs[i] = cx;

            // start at bottom (time)
            mLastTimeYs[i] = r.bottom;
            mCurTimeYs[i] = r.bottom;

            // amplitude center
            mLastAmpXs[i] = cx;
            mCurAmpXs[i] = cx;
        }
    }

    // -------------------------------------------------------------------------
    // THE DRAW LOOP — SAME LOGIC, BUT AXES ROTATED
    // -------------------------------------------------------------------------
    private void drawWave() {
        if (mWaveDatas == null || mWaveDatas.isEmpty()) return;

        if (mLeadRectFs == null && mViewHeight > 0 && mViewWidth > 0) {
            prepareColumns();
            drawLabelsFull();
        }

        if (mWaveDatas.size() < mLeadCount) return;

        Canvas canvas = mHolder.lockCanvas(null);
        if (canvas == null) return;

        // DO NOT CLEAR FULL — old logic did partial strips.
        // We ONLY overwrite where we draw.

        for (int batch = 0; batch < 8; batch++) {

            for (int i = 0; i < mLeadCount; i++) {

                Short val = mWaveDatas.poll();
                if (val == null) continue;

                RectF rect = mLeadRectFs[i];

                float lastY = mLastTimeYs[i];
                float newY = lastY - oneStep;

                // reached top → clear that column and restart at bottom
                if (newY < rect.top) {
                    canvas.save();
                    canvas.clipRect(rect);
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    canvas.restore();

                    // re-draw label
                    drawLabel(canvas, i, rect);

                    lastY = rect.bottom;
                    newY = rect.bottom - oneStep;

                    mLastAmpXs[i] = mCenterXs[i];
                }
                /*
                *                 if (newY < rect.top) {

                    // If lead 0 wrapped → clear entire screen
                    if (i == 0) {
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                        // Reset all leads to bottom
                        for (int k = 0; k < mLeadCount; k++) {
                            RectF rr = mLeadRectFs[k];
                            mLastTimeYs[k] = rr.bottom;
                            mLastAmpXs[k]  = mCenterXs[k];
                        }

                        // Redraw labels after clearing
                        for (int k = 0; k < mLeadCount; k++) {
                            drawLabel(canvas, k, mLeadRectFs[k]);
                        }
                    }

                    // Reset individual lead pointer
                    lastY = rect.bottom;
                    newY  = rect.bottom - oneStep;

                    mLastAmpXs[i] = mCenterXs[i];
                }
*/


                // amplitude → horizontal offset
                float ampPx = val * 0.001f * gain * perMillmeter * amplitudeScale;
                float newX = mCenterXs[i] + ampPx;

                // draw line from last point to new point (history preserved)
                canvas.drawLine(
                        mLastAmpXs[i], lastY,
                        newX, newY,
                        mPaint
                );

                mLastAmpXs[i] = newX;
                mLastTimeYs[i] = newY;
            }
        }

        mHolder.unlockCanvasAndPost(canvas);
    }

    // -------------------------------------------------------------------------
    // LABELS
    // -------------------------------------------------------------------------
    private void drawLabelsFull() {
        Canvas c = mHolder.lockCanvas();
        if (c == null) return;
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        for (int i = 0; i < mLeadCount; i++) drawLabel(c, i, mLeadRectFs[i]);

        mHolder.unlockCanvasAndPost(c);
    }

    private void drawLabel(Canvas c, int i, RectF rect) {
        float textWidth = mPaint.measureText(mLeadNames[i]);
        float x = rect.left + (rect.width() - textWidth) / 2f;
        float y = rect.bottom - 5;
        c.drawText(mLeadNames[i], x, y, mPaint);
    }

    // -------------------------------------------------------------------------
    // PUBLIC API
    // -------------------------------------------------------------------------
    public void setEcgDataBuf(ConcurrentLinkedQueue<Short> b) {
        mWaveDatas = b;
    }

    public void startRenderer() {
        if (refreshWave) return;
        refreshWave = true;

        new Thread(() -> {
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
            while (refreshWave) {
                drawWave();
                try {
                    Thread.sleep(8);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    public void stopRenderer() {
        refreshWave = false;
    }

    // -------------------------------------------------------------------------
    // SURFACE CALLBACKS
    // -------------------------------------------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mViewHeight = getHeight();
        mViewWidth = getWidth();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mViewWidth = w;
        mViewHeight = h;
        mLeadRectFs = null;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopRenderer();
    }
}
