package com.weather.customizeviewlist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.weather.customizeviewlist.utils.UtilsSize;

public class ChatView extends View {

    private final String[] X_STRING_ARRAY = new String[]{"10","20","30","40","50"};
    private final String[] Y_STRING_ARRAY = new String[]{"20","40","60","80","100","120","140","160","180"};

    private Paint paint;
    private TextPaint textPaint;
    private Paint effectPaint;
    private int mWidth;
    private int mHeight;
    private Rect rectText;
    private int lineWidth;
    private Path path = new Path();

    public ChatView(Context context) {
        this(context,null);
    }

    public ChatView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public ChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        lineWidth = UtilsSize.dpToPx(getContext(),3);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(lineWidth);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(UtilsSize.sp2px(getContext(),16));

        rectText = new Rect();

        effectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        effectPaint.setStrokeWidth(lineWidth / 2f);
        effectPaint.setColor(Color.WHITE);
        effectPaint.setPathEffect(new DashPathEffect(new float[]{5,5},0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mWidth == 0 || mHeight == 0) {
            return;
        }

        int mXWidth = mWidth - UtilsSize.dpToPx(getContext(), 80);
        int mYHeight = (int) (mHeight * 2 / 3f - UtilsSize.dpToPx(getContext(), 50));
        int startX = UtilsSize.dpToPx(getContext(),40);
        int endX = mWidth - UtilsSize.dpToPx(getContext(),50);
        int arrowSize = UtilsSize.dpToPx(getContext(),5);
        int endY = UtilsSize.dpToPx(getContext(),50);
        float startY = mHeight * 2 / 3f;
        // x轴
        canvas.drawLine(startX,mHeight * 2 / 3f,endX,mHeight * 2 /3f,paint);
        // x轴箭头
        canvas.drawLine(endX - arrowSize * 2,mHeight * 2 / 3f + arrowSize,endX,mHeight * 2 /3f,paint);
        canvas.drawLine(endX - arrowSize * 2,mHeight * 2 / 3f - arrowSize,endX,mHeight * 2 /3f,paint);
        // y轴
        canvas.drawLine(startX,startY,startX,endY,paint);
        // y轴箭头
        canvas.drawLine(startX - arrowSize,endY + arrowSize * 2,startX,endY,paint);
        canvas.drawLine(startX + arrowSize,endY + arrowSize * 2,startX,endY,paint);

        Log.d(getClass().getSimpleName(),"onDraw mWidth="+mWidth+",height="+mHeight+",mXWidth="+ mXWidth +",mYHeight="+ mYHeight);
        canvas.save();
        int xCount = X_STRING_ARRAY.length + 1;
        float mXLineWidth = mXWidth * 1f / xCount;
        for (int i = 0;i < xCount - 1;i ++){
            float tmpStartX =  startX + mXLineWidth * (i + 1);
            canvas.drawLine(tmpStartX,startY,tmpStartX,startY - UtilsSize.dpToPx(getContext(),10),paint);
            String text = X_STRING_ARRAY[i];
            textPaint.getTextBounds(text,0,text.length(),rectText);
            int textWidth = rectText.right - rectText.left;
            int textHeight = rectText.bottom - rectText.top;
            canvas.drawText(X_STRING_ARRAY[i],tmpStartX - textWidth / 2f,startY + textHeight + UtilsSize.dpToPx(getContext(),5),textPaint);
        }
        canvas.restore();
        int yCount = Y_STRING_ARRAY.length + 1;
        float mYLineHeight = mYHeight * 1f/ yCount;

        for (int i = 0;i < yCount - 1;i ++){
            float tmpStartY = startY - mYLineHeight * (i + 1);
            canvas.drawLine(startX,tmpStartY,endX,tmpStartY,effectPaint);
            String text = Y_STRING_ARRAY[i];
            textPaint.getTextBounds(text,0,text.length(),rectText);
            int textWidth = rectText.right - rectText.left;
            int textHeight = rectText.bottom - rectText.top;
            canvas.drawText(Y_STRING_ARRAY[i],startX - textWidth - lineWidth - UtilsSize.dpToPx(getContext(),5),tmpStartY + textHeight / 2f,textPaint);
        }

        canvas.save();
        float firstStartX = startX + mXLineWidth;
        float firstStartY = startY - mYLineHeight;
        Log.d(getClass().getSimpleName(),"firstStartX="+firstStartX+",firstStartY="+firstStartY+",mXLineWidth="+mXLineWidth+",mYLineHeight="+mYLineHeight);
        path.moveTo(startX,startY);
        path.lineTo(startX + 20,startY - 20);
        path.quadTo(startX + mXLineWidth / 2f,startY - mYLineHeight / 2f,firstStartX,firstStartY);
        path.close();
        canvas.drawPath(path,paint);
        canvas.restore();
    }
}
