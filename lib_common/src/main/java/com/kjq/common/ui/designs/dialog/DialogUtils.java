package com.kjq.common.ui.designs.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/8/23 14:26
 * @version 1.0.0
 */
public class DialogUtils extends BaseDialog {

    public DialogUtils(@NonNull Context context) {
        super(context);
    }

    public DialogUtils(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogUtils(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onTouchOutside() {
        if (mOnTouchOutsideListener != null){
            mOnTouchOutsideListener.onTouchOutside(this);
        }
    }
}
