package com.kjq.common.ui.designs.popMenu;

public class PopMenuModel {
    private int mI_itemType;
    private String mS_menuTxt;
    private int mI_sign;

    public PopMenuModel(int i_itemType, String s_menuTxt, int i_sign) {
        mI_itemType = i_itemType;
        mS_menuTxt = s_menuTxt;
        mI_sign = i_sign;
    }

    public PopMenuModel(int i_itemType,String s_menuTxt){
        mI_itemType = i_itemType;
        mS_menuTxt = s_menuTxt;
    }

    public int getI_itemType() {
        return mI_itemType;
    }

    public void setI_itemType(int i_itemType) {
        mI_itemType = i_itemType;
    }

    public String getS_menuTxt() {
        return mS_menuTxt;
    }

    public void setS_menuTxt(String s_menuTxt) {
        mS_menuTxt = s_menuTxt;
    }

    public int getI_sign() {
        return mI_sign;
    }

    public void setI_sign(int i_sign) {
        mI_sign = i_sign;
    }
}
