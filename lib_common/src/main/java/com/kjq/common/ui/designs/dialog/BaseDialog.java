package com.kjq.common.ui.designs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/8/23 14:21
 * @version 1.0.0
 */
public abstract class BaseDialog extends Dialog {

    protected boolean mB_touchOutsideListenerOne = false;
    protected boolean mB_isTriggeredTouchOutside = false;

    public interface onTouchOutsideListener{
        void onTouchOutside(BaseDialog baseDialog);
    }

    protected onTouchOutsideListener mOnTouchOutsideListener;

    public onTouchOutsideListener getOnTouchOutsideListener() {
        return mOnTouchOutsideListener;
    }

    public void setOnTouchOutsideListener(onTouchOutsideListener onTouchOutsideListener,boolean isOne) {
        mOnTouchOutsideListener = onTouchOutsideListener;
        mB_touchOutsideListenerOne = isOne;
    }


    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected abstract void onTouchOutside();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* 触摸外部弹窗 */
        if (isOutOfBounds(getContext(), event)) {

            if (mB_touchOutsideListenerOne){
                if (!mB_isTriggeredTouchOutside){
                    onTouchOutside();
                    mB_isTriggeredTouchOutside = true;
                }
            }else {
                onTouchOutside();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }
}
