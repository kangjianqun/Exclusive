package com.kjq.common.ui.designs.dialog.litener;

import android.view.View;

import com.kjq.common.ui.designs.dialog.model.BaseData;


public interface IViewEven {
    void setViewEven(View viewEven);
    void onItemClick(View view, BaseData model, int index);
    void okListener(View view);
    void okListener(View view, String msg);
    void okListener(View view, String ontTime, String twoTime);
    void noListener(View view);
    void onBack(View view);
}
