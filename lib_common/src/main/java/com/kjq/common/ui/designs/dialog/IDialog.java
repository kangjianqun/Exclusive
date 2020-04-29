package com.kjq.common.ui.designs.dialog;


import android.view.WindowManager;

import androidx.annotation.ColorRes;

import com.kjq.common.ui.designs.dialog.litener.ViewEven;

public interface IDialog {
    void show(ViewEven viewEven);
    void show();
    void dismissDialog();
    void setTxt(String msg);
    void setTitleTxt(String msg);
    void setTitleTxt(String msg, @ColorRes int colorRes);
    void setBack();
    void setLayoutParams(WindowManager.LayoutParams layoutParams);
    WindowManager.LayoutParams getLayoutParams();
}
