package com.kjq.common.ui.designs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kjq.common.R;

/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/9/18 13:49
 * @version 1.0.0
 */
public class RecyclerViewExtend extends androidx.recyclerview.widget.RecyclerView {

    private static final float DEFAULT_MAX_RATIO = 0.6f;
    private static final float DEFAULT_MAX_HEIGHT = 0f;
    private float mMaxHeight = DEFAULT_MAX_HEIGHT;// 优先级低

    public RecyclerViewExtend(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewExtend(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }

    public RecyclerViewExtend(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable")
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CommonRecycleViewExtend);

        mMaxHeight = a.getDimension(R.styleable.CommonRecycleViewExtend_commonRVExtendMaxHeight,DEFAULT_MAX_HEIGHT);
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
