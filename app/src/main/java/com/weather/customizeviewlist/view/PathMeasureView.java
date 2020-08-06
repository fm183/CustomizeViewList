package com.weather.customizeviewlist.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.weather.customizeviewlist.utils.UtilsSize;


/**
 * 一条线顺着圆角矩形区域运动
 */
public class PathMeasureView extends View {
    private static final int mBaseColor = 0xFFFFFF00;

    private Paint mPaint;
    private Path path;
    private PathMeasure mPathMeasure;
    private float mAnimatorValue;
    private Path mDst;
    private float mLength;
    private  RectF rectF;

    private Paint basePaint;
    private RectF rectF2 = new RectF();


    public PathMeasureView(Context context) {
        this(context,null);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(UtilsSize.dpToPx(getContext(),10));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        path = new Path();

        rectF = new RectF();
        mPathMeasure = new PathMeasure(path, false);
        mDst = new Path();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();

        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setStrokeWidth(UtilsSize.dpToPx(getContext(),10));
        basePaint.setColor(mBaseColor);
        basePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.left = 50;
        rectF.right = w - 50;
        rectF.top = 50;
        rectF.bottom = h / 4f - 50;
        path.addRoundRect(rectF, 50, 50, Path.Direction.CW);
        mPathMeasure.setPath(path,false);
        mLength = mPathMeasure.getLength();

        rectF2.left = 50;
        rectF2.right = getWidth() - 50;
        rectF2.top = 50 ;
        rectF2.bottom = getHeight() / 4f - 50;

        Log.d(getClass().getSimpleName(),"onSizeChanged mLength="+mLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(rectF2, 50, 50, basePaint);

        mDst.reset();
        float stop = mLength * mAnimatorValue;
        float start = stop - 50;

        int redValue = (int) (mAnimatorValue * 255);
        mPaint.setColor(Color.rgb(redValue,redValue,255));

        Log.d(getClass().getSimpleName(),"onDraw start="+start+",stop="+stop+",mAnimatorValue="+mAnimatorValue + ",redValue="+redValue);
        mPathMeasure.getSegment(start, stop, mDst, true);
        canvas.drawPath(mDst, mPaint);
    }
}