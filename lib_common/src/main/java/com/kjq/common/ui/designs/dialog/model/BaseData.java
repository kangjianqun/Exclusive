package com.kjq.common.ui.designs.dialog.model;

public abstract class BaseData {

    private int mI_sign;
    private String mS_txt;
    private Object mObject;

    public BaseData(String txt) {
        mS_txt = txt;
    }

    public BaseData(int i_sign, String txt) {
        mI_sign = i_sign;
        mS_txt = txt;
    }

    public BaseData(String s_txt, Object object) {
        mS_txt = s_txt;
        mObject = object;
    }

    public int getI_sign() {
        return mI_sign;
    }

    public void setI_sign(int i_sign) {
        mI_sign = i_sign;
    }

    public String getS_txt() {
        return mS_txt;
    }

    public void setS_txt(String txt) {
        mS_txt = txt;
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object object) {
        mObject = object;
    }
}
