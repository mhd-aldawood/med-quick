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
 * 波形
 * Original behavior:
 * - 6 leads in 6 rows
 * - Time from left to right
 * <p>
 * Modified behavior:
 * - 6 leads in 1 row, 6 columns (after rotation)
 * - Time from bottom to top
 * - Same drawing logic (point/line, speed, etc.)
 */
public class ReviewWave_ extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = ReviewWave_.class.getSimpleName();
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

    public ReviewWave_(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        float scaledDensity = dm.scaledDensity;// 缩放密度

        mLeadNames = new String[]{"I", "II", "III", "aVR", "aVL", "aVF",};
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

        if (mWaveDatas == null || mWaveDatas.isEmpty()) {
            return;
        }

        // compute layout once (same as old behavior)
        if (mLeadRectFs == null && mViewHeight > 0) {
            meanSort();
        }

        if (mWaveDatas.size() < 5 * mLeadCount) {
            return;
        }

        // after rotation, "time axis" length = HEIGHT
        float sweepLimit = mViewHeight;

        float nextX = mLastX + oneXwidth;
        boolean wrapped = false;

        if (nextX > sweepLimit) {
            // we reached the top → wrap and restart from bottom
            mLastX = 0;
            nextX = mLeadNameWidth;
            wrapped = true;
        }

        Canvas canvas;

        if (wrapped) {
            // 🔴 FULL CLEAR when we wrap
            canvas = mHolder.lockCanvas();
            if (canvas == null) return;

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        } else {
            // 🟢 only redraw a small horizontal band (like original)
            Rect dirty = new Rect(
                    0,
                    (int) (mViewHeight - mLastX - oneXwidth * 5 - 20),
                    mViewWidth,
                    (int) (mViewHeight - mLastX)
            );
            canvas = mHolder.lockCanvas(dirty);
            if (canvas == null) return;
        }

        // rotate so time is bottom -> top
        canvas.save();
        canvas.translate(0f, mViewHeight);
        canvas.rotate(-90f);

        // draw lead names (after rotation they appear under each column)
        mPaint.setColor(Color.GREEN);
        for (int i = 0; i < mLeadCount; i++) {
            canvas.drawText(mLeadNames[i], paddingRight, mCenterLineYs[i], mPaint);
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2.0f);

        float startX;

        // same drawing loop as original, but using sweepLimit for vertical sweep
        for (int j = 0; j < 10; j++) {
            n++;
            startX = mLastX + oneXwidth;
            if (startX > sweepLimit) {
                break;
            }
            if (mLastX == 0) {
                startX = mLeadNameWidth;
            }

            for (int i = 0; i < mLeadCount; i++) {
                Short val = mWaveDatas.poll();
                if (val == null) {
                    i--;
                    continue;
                }
                mCurYs[i] = mCenterLineYs[i]
                        - val * 0.001f * gain * perMillmeter * amplitudeScale;

                // 两次锁屏连接处多画一个点，避免锁屏时精度损失造成的波形不连续
                if (j == 0) {
                    if (startX == mLeadNameWidth) {
                        canvas.drawPoint(startX, mCurYs[i], mPaint);
                        mLasterYs[i] = mLastYs[i];
                        mLastYs[i] = mCurYs[i];
                        continue;
                    } else {
                        canvas.drawLine(
                                mLastX - oneXwidth, mLasterYs[i],
                                mLastX, mLastYs[i],
                                mPaint
                        );
                    }
                }

                canvas.drawLine(
                        mLastX, mLastYs[i],
                        startX, mCurYs[i],
                        mPaint
                );
                mLasterYs[i] = mLastYs[i];
                mLastYs[i] = mCurYs[i];
            }
            mLastX = startX;
        }

        canvas.restore();
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


    /**
     * 平均排序
     * (unchanged: still 6 rows in "logical" space;
     * after rotation, they appear as 6 columns)
     */
    public void meanSort() {
        mRow = mLeadCount;
        maxHeight = mViewHeight;

        // We don't really need all the RectF logic for rows/columns now,
        // but we'll keep the array allocated to avoid NPEs elsewhere.
        int rectFCount = mRow * mColumn + mDisplayRhythmLeadCount;
        mLeadRectFs = new RectF[rectFCount];
        mCenterLineYs = new float[rectFCount];

        // 🔸 Use the full height to distribute lead centers
        float availableHeight = getWidth() - paddingTop - paddingBottom;
        if (availableHeight <= 0) {
            availableHeight = mViewHeight;
        }

        // Divide into (mLeadCount + 1) so there is margin on top & bottom
        float step = availableHeight / (mLeadCount + 1);

        for (int i = 0; i < mLeadCount; i++) {
            // center line for lead i
            float centerY = paddingTop + step * (i + 1);
            mCenterLineYs[i] = centerY;

            // just fill some RectF so it's non-null if used somewhere else
            mLeadRectFs[i] = new RectF(
                    paddingLeft,
                    centerY - step / 2f,
                    mViewWidth - paddingRight,
                    centerY + step / 2f
            );
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
            waveRenderer = new HealthWave();
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
        Log.d(TAG, "surfaceDestroyed");
        stopRenderer();

    }

    private class HealthWave extends Thread {

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
            while (refreshWave) {
                if (!refreshWave) {
                    break;
                }
                drawWave();
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private HealthWave waveRenderer;
}
