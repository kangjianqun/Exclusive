package com.kjq.common.ui.designs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.kjq.common.R;

/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/9/18 11:44
 * @version 1.0.0
 */
public class LinearLayoutExtend extends LinearLayout {

    private static final float DEFAULT_MAX_RATIO = 0.6f;
    private static final float DEFAULT_MAX_HEIGHT = 0f;
    private float mMaxHeight = DEFAULT_MAX_HEIGHT;// 优先级低

    public LinearLayoutExtend(Context context) {
        super(context);
    }

    public LinearLayoutExtend(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutExtend(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable")
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DesignsLinearLayoutExtend);
        final int count = a.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.DesignsLinearLayoutExtend_designsLLExtendMaxHeight) {
                mMaxHeight = a.getDimension(attr, DEFAULT_MAX_HEIGHT);
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
                heightSize = heightSize <= mMaxHeight ? heightSize : (int) mMaxHeight;
            }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
                heightSize = heightSize <= mMaxHeight ? heightSize : (int) mMaxHeight;
            }

        if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = heightSize <= mMaxHeight ? heightSize: (int) mMaxHeight;
            }

        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,heightMode);
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }
}
