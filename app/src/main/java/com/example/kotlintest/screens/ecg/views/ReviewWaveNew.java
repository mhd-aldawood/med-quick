package com.example.kotlintest.screens.ecg.views;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Process;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
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
    // 病例对应的是多少导联：12或18
    private int mLeadCount = 6;//12;

    // 每毫米8个像素
    private float perMillmeter = 8;

    // 采样率
    public static final int SAMPLE_RATE = 1000;

    // 计算多少个像素画一个点
    private float twoPointerDistance;
    // 导联名称所占宽度
    private int mLeadNameWidth;
    private float perColumnWidth;

    private float amplitudeScale = 1.0f; // 1.0 = current size, 0.5 = half height

    public void setAmplitudeScale(float scale) {
        this.amplitudeScale = Math.max(0f, scale);
    }

    private int maxHeight;
    private int mViewWidth;
    private int mViewHeight;
    private int mRhythmLeadTextColor;
    private int mNormalLeadTextColor;
    private int mWaveColor;
    private Paint mPaint;
    private float mFontHeight;


    private float paddingTop = 30;
    private float paddingBottom = 0;
    private float paddingLeft = 0;
    private float paddingRight;

    private RectF[] mLeadRectFs;

    // 保存波形数据
    private ConcurrentLinkedQueue<Short> mWaveDatas;
    // 导联名称
    private String[] mLeadNames;

    private SurfaceHolder mHolder;
    private boolean refreshWave = false;

    // 定标系数是440
    private final int wmvscale = 440;
    private Context mContext;

    public ReviewWaveNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        float scaledDensity = dm.scaledDensity;// 缩放密度

        mLeadNames = new String[]{"I", "II", "III", "aVR", "aVL", "aVF",};// "V1", "V2", "V3", "V4", "V5", "V6"};
//        perMillmeter = dm.densityDpi / 25.4f;
        perMillmeter = 0.014f * 200
                * dm.widthPixels / (20.5f * 25);
        jumpPoint = 1;
        //假设 8个像素代表1mm，走速为25mm/s,采样率1000，每隔5个点画一个
        oneXwidth = perMillmeter * 25f / 200 * jumpPoint;
        mLastX = 0;
        Log.e(TAG, "oneXwidth:" + oneXwidth + ",scaledDensity:" + scaledDensity + ",dm.heightPixels:" + dm.heightPixels + ",dm.widthPixels:" + dm.widthPixels + ",dm.densityDpi:" + dm.densityDpi);
        twoPointerDistance = 100 * perMillmeter / SAMPLE_RATE;

        // 设置画笔属性
        mPaint = new Paint();
        mPaint.setStrokeWidth((int) scaledDensity);

        mPaint.setTextAlign(Paint.Align.LEFT);
        float textSize = 15 * scaledDensity;
        mNormalLeadTextColor = Color.parseColor("#00cc00");
        mRhythmLeadTextColor = Color.YELLOW;

        mWaveColor = Color.GREEN;
        mPaint.setTextSize(textSize);
        mPaint.setAntiAlias(true);
        mLeadNameWidth = (int) mPaint.measureText("aVR");
        // 右边空隙2个字符宽度
        paddingRight = mPaint.measureText("a");
        mLeadNameWidth += paddingRight + 5;
        FontMetrics fontMetrics = mPaint.getFontMetrics();
        mFontHeight = (fontMetrics.descent - fontMetrics.ascent) / 3 * 2;
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mPaint.setColor(Color.GREEN);

    }

    //8个像素代表1mm，走速为25mm/s,采样率1000，每隔5个点画一个
    float oneXwidth;
    float mLastX;

    float[] mLastYs = new float[12];
    float[] mLasterYs = new float[12];
    float[] mCurYs = new float[12];
    //10mm/mv 8个像素代表1mm
    int gain = 10;
    int jumpPoint;

    /**
     * 绘制波形
     */
    int n = 0;

    private void drawWave() {

        if (mWaveDatas == null || mWaveDatas.isEmpty()) return;

        // ----------------------------------------------------
        // 1) Layout not prepared yet → prepare vertical layout
        // ----------------------------------------------------
        if (mLeadRectFs == null && mViewWidth > 0) {

            meanSort(); // your vertical sorter (6 columns)

            // We start drawing from the bottom
            mLastY = mViewHeight - mLeadNameHeight - 2;

            // Reset last X buffers (avoid garbage lines)
            for (int i = 0; i < mLeadCount; i++) {
                mLastXs[i] = mCenterLineXs[i];
                mLasterXs[i] = mCenterLineXs[i];
            }

            // Draw labels once
            Canvas canvas = mHolder.lockCanvas(
                    new Rect(0, mViewHeight - mLeadNameHeight - 10,
                            mViewWidth,
                            mViewHeight)
            );

            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mPaint.setColor(Color.GREEN);

                // Center names at bottom
                for (int i = 0; i < mLeadCount; i++) {
                    float cx = mCenterLineXs[i];
                    canvas.drawText(mLeadNames[i], cx, mViewHeight - 5, mPaint);
                }

                mHolder.unlockCanvasAndPost(canvas);
            }
        }

        if (mWaveDatas.size() < 5 * mLeadCount) return;

        // ----------------------------------------------------
        // 2) Compute upward movement
        // ----------------------------------------------------
        float startY = mLastY - oneYHeight;

        // If reached the top of waveform area → wrap to bottom
        if (startY < paddingTop) {
            mLastY = mViewHeight - mLeadNameHeight - 2;
            startY = mLastY - oneYHeight;
        }

        // ----------------------------------------------------
        // 3) Compute dirty rectangle for small slice
        // ----------------------------------------------------
        int bottom = (int) mLastY;
        int top = (int) (mLastY - (oneYHeight * 5 + 20));

        // Clamp top to waveform region
        if (top < paddingTop) top = (int) paddingTop;

        Canvas canvas = mHolder.lockCanvas(new Rect(0, top, mViewWidth, bottom));
        if (canvas == null) return;

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);


        // ----------------------------------------------------
        // 4) Draw 10 vertical waveform micro-steps
        // ----------------------------------------------------
        for (int j = 0; j < 10; j++) {

            startY = mLastY - oneYHeight;

            if (startY < paddingTop) break;

            for (int i = 0; i < mLeadCount; i++) {

                Short val = mWaveDatas.poll();
                if (val == null) {
                    i--;
                    continue;
                }

                // Convert value to X-shift from center line
                mCurXs[i] = mCenterLineXs[i] + (
                        val * 0.001f * gain * perMillmeter * amplitudeScale
                );

                // ------------------------------------------------
                // First micro-step in this slice: draw join line
                // ------------------------------------------------
                if (j == 0) {
                    // First Y row (just wrapped)
                    if (mLastY >= mViewHeight - mLeadNameHeight - 5) {
                        canvas.drawPoint(mCurXs[i], startY, mPaint);

                        mLasterXs[i] = mLastXs[i];
                        mLastXs[i] = mCurXs[i];
                        continue;
                    } else {
                        // draw a line from previous slice start
                        canvas.drawLine(
                                mLasterXs[i], mLastY + oneYHeight,
                                mLastXs[i], mLastY,
                                mPaint
                        );
                    }
                }

                // ------------------------------------------------
                // Draw the normal vertical segment
                // ------------------------------------------------
                canvas.drawLine(mLastXs[i], mLastY, mCurXs[i], startY, mPaint);

                // Slide buffer forward
                mLasterXs[i] = mLastXs[i];
                mLastXs[i] = mCurXs[i];
            }

            // Move the Y pointer upward
            mLastY = startY;
        }

        mHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * 设置波形数据
     *
     * @param ecgDataBuf
     * @return
     */
    public void setEcgDataBuf(ConcurrentLinkedQueue<Short> ecgDataBuf) {
        mWaveDatas = ecgDataBuf;
    }


    float[] mCenterLineYs;
    float[] mCenterLineXs;
    float[] mLastXs = new float[12];
    float[] mLasterXs = new float[12];
    float[] mCurXs = new float[12];

    float oneYHeight = 4;  // equivalent to oneXwidth
    float mLastY;
    int mLeadNameHeight = 40;

    /**
     * 平均排序
     */
    public void meanSort() {
        // 1 row, N columns (N = number of leads)
        mRow = 1;
        mColumn = mLeadCount;   // for you: 6

        maxHeight = mViewHeight;

        int allRow = mRow + mDisplayRhythmLeadCount;
        int rectFCount = mRow * mColumn + mDisplayRhythmLeadCount;

        mLeadRectFs = new RectF[rectFCount];
        mCenterLineXs = new float[rectFCount];   // center X instead of Y

        // Full usable height for wave area (from top down to just above labels)
        float usableHeight = mViewHeight - paddingTop - paddingBottom - mLeadNameHeight;

        // Split width into columns
        float perColumnWidth = (mViewWidth - paddingLeft - paddingRight) / mColumn;

        for (int i = 0; i < mColumn; i++) {

            mLeadRectFs[i] = new RectF();

            // Column i horizontal bounds
            mLeadRectFs[i].left = paddingLeft + perColumnWidth * i;
            mLeadRectFs[i].right = mLeadRectFs[i].left + perColumnWidth;

            // Same vertical span for all leads (top to usableHeight)
            mLeadRectFs[i].top = paddingTop;
            mLeadRectFs[i].bottom = paddingTop + usableHeight;

            // Center line X for this lead
            mCenterLineXs[i] = (mLeadRectFs[i].left + mLeadRectFs[i].right) / 2f;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mViewHeight = getHeight();
        mViewWidth = getWidth();
        Log.d(TAG, "surfaceCreated");
        mWaveColor = Color.rgb(0, 255, 0);
        mNormalLeadTextColor = Color.rgb(0, 255, 0);
        mRhythmLeadTextColor = Color.rgb(0, 255, 0);

    }

    private int mRow = 6;
    private int mColumn = 1;
    private int mDisplayRhythmLeadCount = 0;


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        Log.d(TAG, "surfaceChanged");
        // 控件大小改变时调用
        mViewHeight = height;
        mViewWidth = width;

    }

    public void setRendererColor(int arg1, float arg2, int arg3, int arg4) {


    }

    public void startRenderer() {
        if (refreshWave) {
            return;
        }
        refreshWave = true;
        mLastX = 0;
        mLastYs = new float[12];
        mLasterYs = new float[12];
        mCurYs = new float[12];

        mLeadRectFs = null;
        mCenterLineYs = null;
        if (waveRenderer == null) {
            waveRenderer = new ReviewWaveNew.HealthWave();
        }
        waveRenderer.start();
    }

    public void stopRenderer() {
        if (!refreshWave) {
            return;
        }
        refreshWave = false;

        if (waveRenderer != null && waveRenderer.isAlive()) {
            waveRenderer.interrupt();
            waveRenderer = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.d(TAG, "surfaceDestroyed");
        stopRenderer();

    }

    private class HealthWave extends Thread {

        @Override
        public void run() {
//            Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY);
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
            while (refreshWave) {
                if (!refreshWave) {
                    break;
                }
                drawWave();
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private ReviewWaveNew.HealthWave waveRenderer;
}
