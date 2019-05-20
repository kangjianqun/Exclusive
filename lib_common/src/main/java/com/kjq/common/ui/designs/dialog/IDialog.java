package com.kjq.common.ui.designs.dialog;


import com.kjq.common.ui.designs.dialog.litener.ViewEven;

public interface IDialog {
    void show(ViewEven viewEven);
    void show();
    void dismissDialog();
    void setTxt(String msg);
    void setTitleTxt(String msg);
}
