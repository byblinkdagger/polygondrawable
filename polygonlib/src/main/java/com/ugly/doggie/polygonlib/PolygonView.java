package com.ugly.doggie.polygonlib;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Doggie
 * @Copyright com.taiyiche
 * @description 实现的主要功能:
 * @date 2016/9/28
 */

public class PolygonView extends View {

    //边数
    private int mBorderNum = 6;

    private Path mPath;

    private Paint mPaint;

    private Shader mShader;

    private float centerX, centerY, mRadius, innerDegree, mBorderHalfLength, mHalfHeight;

    public PolygonView(Context context) {
        this(context, null);
    }

    public PolygonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolygonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPath = new Path();
        mPaint = new Paint();
//        mPaint.setStrokeWidth(4);
//        mPaint.setColor(Color.WHITE);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setAntiAlias(true);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);


        mShader = new BitmapShader(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.cristina), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //直角坐标系旋转 D 角度后   新旧坐标公式为：x0= (x - rx0)*cos(a) - (y - ry0)*sin(a) + rx0 ;   y0= (x - rx0)*sin(a) + (y - ry0)*cos(a) + ry0 ;

        float degree = 0;

        mPath.moveTo(centerX - mBorderHalfLength, centerY + mHalfHeight);
        for (int i = 0; i < mBorderNum; i++) {
            degree = -360f / mBorderNum * i;
            mPath.lineTo((float) (mBorderHalfLength * Math.cos(degree / 180 * Math.PI) - mHalfHeight * Math.sin(degree / 180 * Math.PI) + centerX), (float) (mBorderHalfLength * Math.sin(degree / 180 * Math.PI) + mHalfHeight * Math.cos(degree / 180 * Math.PI) + centerY));
        }
        mPath.close();
        canvas.drawPath(mPath, mPaint);


//        for (int i = 0; i < mBorderNum; i++) {
//            canvas.save();
//            canvas.rotate(360f / mBorderNum * i, centerX, centerY);
//            canvas.drawLine(centerX - mBorderHalfLength, centerY + mHalfHeight, centerX + mBorderHalfLength, centerY + mHalfHeight, mPaint);
//            canvas.restore();
//        }
    }
}
