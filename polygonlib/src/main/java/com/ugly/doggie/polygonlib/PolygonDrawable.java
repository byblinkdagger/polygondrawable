package com.ugly.doggie.polygonlib;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * @author Doggie
 * @Copyright com.taiyiche
 * @description 实现的主要功能:
 * @date 2016/9/28
 */

public class PolygonDrawable extends Drawable {

    private Paint mPaint;

    private Path mPath;

    private int mBorderNum;

    private Bitmap mBitmap;

    private float centerX, centerY, mRadius, innerDegree, mBorderHalfLength, mHalfHeight;

    public PolygonDrawable(@NonNull Bitmap bitmap, int borderNum) {
        mBitmap = bitmap;
        mBorderNum = borderNum;
        init();
    }

    public void init() {

        try {
            if (mBorderNum < 3) {
                throw new Exception("多边形边数必须大于等于三");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initPaint();
        initPath();
    }

    private void initPath() {

        if (mPath == null) {
            mPath = new Path();
        }
        mPath.reset();

        float degree = 0;

        mPath.moveTo(centerX - mBorderHalfLength, centerY + mHalfHeight);
        for (int i = 0; i < mBorderNum - 1; i++) {
            degree = -360f / mBorderNum * i;
            mPath.lineTo((float) (mBorderHalfLength * Math.cos(degree / 180 * Math.PI) - mHalfHeight * Math.sin(degree / 180 * Math.PI) + centerX), (float) (mBorderHalfLength * Math.sin(degree / 180 * Math.PI) + mHalfHeight * Math.cos(degree / 180 * Math.PI) + centerY));
        }
        mPath.close();

    }

    private void initPaint() {

        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(8);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        mPaint.setPathEffect(new DiscretePathEffect(5.0f, 5.0f));
//        mPaint.setPathEffect(new CornerPathEffect(20));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int i) {
        if (mPaint != null) {
            mPaint.setAlpha(i);
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (mPaint != null) {
            mPaint.setColorFilter(colorFilter);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    // 设置边界信息
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);

        int w = Math.abs(right - left);
        int h = Math.abs(top - bottom);

        centerX = w / 2;
        centerY = h / 2;

        innerDegree = 180f / mBorderNum;
        if (mBorderNum % 2 == 1) {
            //单数边
            mRadius = (float) (0.8f * w / 2 / Math.sin((float) (90 * (mBorderNum - 1) / mBorderNum) / 180 * Math.PI));
        } else {
            //偶数边
            mRadius = 0.8f * w / 2;
        }

        mBorderHalfLength = (float) (mRadius * Math.sin(innerDegree / 180 * Math.PI));
        mHalfHeight = (float) (mRadius * Math.cos(innerDegree / 180 * Math.PI));

        initPath();
    }

    @Override
    public int getIntrinsicWidth() {
        if (mBitmap != null) {
            return mBitmap.getWidth();
        } else {
            return super.getIntrinsicWidth();
        }
    }

    @Override
    public int getIntrinsicHeight() {
        if (mBitmap != null) {
            return mBitmap.getHeight();
        }
        return super.getIntrinsicHeight();
    }
}
