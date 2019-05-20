package com.kjq.common.ui.designs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;

import com.kjq.common.utils.ScreenSizeUtils;


/**
 * <p>扩展的约束布局</p>
 *
 * @author 康建群 948182974---->>>2018/9/8 16:55
 * @version 1.0.0
 */
public class ConstraintLayoutExtend extends ConstraintLayout {
    public ConstraintLayoutExtend(Context context) {
        super(context);
    }

    public ConstraintLayoutExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConstraintLayoutExtend(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, float width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = ScreenSizeUtils.INSTANCE.dp2px(view.getContext(),width);
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ScreenSizeUtils.INSTANCE.dp2px(view.getContext(),height);
        view.setLayoutParams(params);

    }
}
