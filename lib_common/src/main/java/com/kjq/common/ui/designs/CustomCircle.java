package com.kjq.common.ui.designs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kjq.common.R;
import com.kjq.common.utils.ScreenSizeUtils;

public class CustomCircle extends View {
    private @ColorRes int mI_textShadow = R.color.common_shadow;
    private Paint mPaint;
    /**
     * 圆的宽度
     */
    private int mCircleWidth = 3;
    private RectF mOval;

    public CustomCircle(Context context) {
        this(context,null);
    }

    public CustomCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mOval = new RectF();
    }

    public void setI_textShadow(int i_textShadow) {
        mI_textShadow = i_textShadow;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(ContextCompat.getColor(getContext(),mI_textShadow));

        int sI_off = ScreenSizeUtils.INSTANCE.dip2px(5);
        int sI_offB = ScreenSizeUtils.INSTANCE.dip2px(8);

        int sI_left = 0;
        int sI_top = 0;
        int sI_right = getWidth()+sI_off * 2;
        int sI_bottom = getHeight() + sI_offB * 2;
        mOval.set(sI_left,sI_top,sI_right,sI_bottom);
        mOval.offset(-sI_off,0);
        canvas.drawArc(mOval,0,-180,false,mPaint);
    }
}
