package com.example.commonlibs.widgets;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import com.example.commonlibs.R;


/**
 * @author ZJ
 * created at 2019/8/12 19:16
 */

public class DownloadProgressButton extends android.support.v7.widget.AppCompatButton {

    public static final int MODE_BUTTON = 0;

    public static final int MODE_PROGRESS = 1;

    private Paint mBackgroundPaint;

    private Paint mTextPaint;

    private int mProgress = -1;

    /**
     * 进度条填充色
     */
    private int mPbColor = getResources().getColor(R.color.colorPrimary);

    /**
     * 进度条字体颜色
     */
    private int mPbTextColor = Color.WHITE;

    /**
     * 原始按钮字体颜色
     */
    private int mTextColor = getResources().getColor(R.color.colorPrimary);

    private int mWidth, mHeight;

    private Drawable mBackgroundDrawable;

    private Bitmap mBackgroundBitmap;

    private RectF mRectSrc;

    private int mMode = 1;

    private PorterDuffXfermode mXfermode;

    public void setPbTextColor(int pbTextColor) {
        mPbTextColor = pbTextColor;

    }

    public void setPbColor(int pbColor) {
        mPbColor = pbColor;
        invalidate();
    }

//    public void setTextColor(int textColor) {
//        mTextColor = textColor;
//        invalidate();
//    }

    public DownloadProgressButton(Context context) {
        this(context, null);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DownloadProgressButton);
//        mPbColor = a.getColor(R.styleable.DownloadProgressButton_progressColor, getResources().getColor(R.color.colorBlue));
//        mPbTextColor = a.getColor(R.styleable.DownloadProgressButton_pbTextColor, Color.WHITE);
//        a.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(getTextSize());
        mTextPaint.setColor(mTextColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //解决文字有时候画不出问题
            setLayerType(LAYER_TYPE_SOFTWARE, mTextPaint);
        }

        mBackgroundDrawable = getBackground();
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public void setButtonMode(int mode) {
        mMode = mode;
        if (mode == MODE_BUTTON) {
            setBackgroundDrawable(mBackgroundDrawable);
            mProgress = -1;
        } else if (mode == MODE_PROGRESS) {
            setBackgroundResource(0);
        } else {
            throw new UnsupportedOperationException("Unknown mode " + mode);
        }
    }

    private void releaseBgBitmap() {
        if (mBackgroundBitmap != null) {
            mBackgroundBitmap.recycle();
            mBackgroundBitmap = null;
        }
    }

    public boolean isProgressMode() {
        return mMode == MODE_PROGRESS;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0 && w != oldw && h != oldh) {
            mWidth = w;
            mHeight = h;
            mRectSrc = new RectF(0, 0, mWidth, mHeight);

            if (mBackgroundBitmap != null) {
                releaseBgBitmap();
            }

            mBackgroundBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBackgroundBitmap);

            /**
             * 这个不能掉
             */
            mBackgroundDrawable.setBounds(0, 0, mWidth, mHeight);

            mBackgroundDrawable.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mWidth > 0 && mHeight > 0 && isProgressMode()) {
            int saveCount = canvas.saveLayer(mRectSrc, mBackgroundPaint, Canvas.ALL_SAVE_FLAG);

            canvas.drawBitmap(mBackgroundBitmap, null, mRectSrc, mBackgroundPaint);

            mBackgroundPaint.setXfermode(mXfermode);

            mBackgroundPaint.setColor(mPbColor);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, mWidth * mProgress / 100, mHeight, mBackgroundPaint);

            mBackgroundPaint.setXfermode(null);

            drawText(canvas);

            canvas.restoreToCount(saveCount);
        }
    }

    private void drawText(Canvas canvas) {
        float progress = mProgress * 0.01f;
        LinearGradient shader = new LinearGradient(0, 0, mWidth, 0, new int[]{
                mPbTextColor, mTextColor
        }, new float[]{
                progress, progress + 0.001f
        }, Shader.TileMode.CLAMP);

        mTextPaint.setShader(shader);

        String text = getText().toString();
        float textWidth = mTextPaint.measureText(text);
        float baseX = (mWidth - textWidth) / 2;
        float baseY = mHeight / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2;
        canvas.drawText(text, baseX, baseY, mTextPaint);
    }

    public void destroy() {
        releaseBgBitmap();
    }
}