package com.kjq.common.ui.designs.popMenu;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FreedomPopup extends PopupWindow {
    private Activity mActivity;

    public FreedomPopup(Activity activity) {
        super(activity);
        mActivity = activity;
        init();
    }

    public FreedomPopup(Activity activity, AttributeSet attrs) {
        super(activity, attrs);
        mActivity = activity;
        init();
    }

    public FreedomPopup(Activity activity, AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        mActivity = activity;
        init();
    }

    public FreedomPopup(Activity activity, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(activity, attrs, defStyleAttr, defStyleRes);
        mActivity = activity;
        init();
    }

    private void init(){
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public void setSizeStyle(@SizeStyle.Size int sizeStyle){
        switch (sizeStyle){
            case SizeStyle.W_M:
                setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case SizeStyle.H_M:
                setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                break;
            case SizeStyle.ALL_M:
                setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case SizeStyle.ALL_W:
                setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                break;
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.7f);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    public static class SizeStyle{

        public static final int W_M = 1;
        public static final int H_M = 2;
        public static final int ALL_M = 3;
        public static final int ALL_W = 4;

        @IntDef({W_M, H_M,ALL_M,ALL_W})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Size{}
    }

}
