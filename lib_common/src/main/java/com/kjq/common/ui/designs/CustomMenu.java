package com.kjq.common.ui.designs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;

import com.kjq.common.R;

/**
 * 自定义的单行菜单
 * Created by Administrator on 2017/11/28 0028.
 */

public class CustomMenu extends LinearLayout {
    /*控件*/
    private View mV_wire;
    private ImageView mImgV_left;
    private TextView mTexV_menuTxt;
    private TextView mTexV_secondaryTxt;
    private ImageView mImgV_right;
    private Switch mSwitch_right;

    /*左边图片相关设置*/
    public void setB_isLeft(boolean b_isLeft){
        mImgV_left.setVisibility(b_isLeft ? VISIBLE:GONE);
    }
    public void setI_leftImgID(int i_leftImgID){
        mImgV_left.setBackgroundResource(i_leftImgID);
    }

    /*菜单标题相关属性*/
    public void setS_menuTxt(String s_menuTxt){
        mTexV_menuTxt.setText(s_menuTxt);
    }

    public void setI_menuTxtColor(@ColorInt int color){
        mTexV_menuTxt.setTextColor(color);
    }

    public void setI_menuTxtSize(int size){
        mTexV_menuTxt.setTextSize(size);
    }

    public void setS_secondaryTxt(String s_secondaryTxt){
        mTexV_secondaryTxt.setText(s_secondaryTxt);
    }

    public void setB_switch(boolean b_isDisplay){
        mSwitch_right.setVisibility(b_isDisplay ? VISIBLE:GONE);
    }

    public void setSwitchStyle(@StyleRes int switchStyle){
        mSwitch_right.setScrollBarStyle(switchStyle);
    }

    /*右边图片相关设置*/
    public void setB_isRight(boolean b_isRight){
        mImgV_right.setVisibility(b_isRight ? VISIBLE:GONE);
    }
    public void setI_rightImgID(int i_rightImgID){
        mImgV_right.setBackgroundResource(i_rightImgID);
    }
    
    /*分割线相关设置*/
    public void setB_isWire(boolean b_isWire){
        mV_wire.setVisibility(b_isWire ? VISIBLE:INVISIBLE);
    }
    
    public CustomMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attributeSet){
        /*配置*/
        LayoutInflater.from(context).inflate(R.layout.designs_custom_menu,this,true);
        mImgV_left = findViewById(R.id.cMenu_left);
        mTexV_menuTxt = findViewById(R.id.cMenu_menuTxt);
        mTexV_secondaryTxt = findViewById(R.id.cMenu_secondaryTxt);
        mImgV_right = findViewById(R.id.cMenu_right);
        mV_wire = findViewById(R.id.cMenu_wire);
        mSwitch_right = findViewById(R.id.designs_cMenu_switch_right);

        /*赋值*/
        @SuppressLint({"Recycle", "CustomViewStyleable"})
        TypedArray sTypedArray = context.obtainStyledAttributes(attributeSet,R.styleable.DesignsCustomMenu);

        boolean mB_isLeft = sTypedArray.getBoolean(R.styleable.DesignsCustomMenu_designs_isLeft, true);
        int mI_leftImgID = sTypedArray.getResourceId(R.styleable.DesignsCustomMenu_designs_leftID, R.drawable.designs_svg_menu_black_24dp);

        String mS_menuTxt = sTypedArray.getString(R.styleable.DesignsCustomMenu_designs_menuTxt);
        int sI_menuTxtColor = sTypedArray.getColor(R.styleable.DesignsCustomMenu_designs_menuTxtColor, ContextCompat.getColor(context,R.color.common_default_txt_gray));
        int sI_menuTxtSize = sTypedArray.getInt(R.styleable.DesignsCustomMenu_designs_menuTxtSize,14);
        String mS_secondaryTxt = sTypedArray.getString(R.styleable.DesignsCustomMenu_designs_secondaryTxt);

        boolean sB_isSWitch = sTypedArray.getBoolean(R.styleable.DesignsCustomMenu_designs_isSwitch,false);
        int sI_switchStyle = sTypedArray.getInt(R.styleable.DesignsCustomMenu_designs_switchStyle,R.style.Widget_AppCompat_CompoundButton_Switch);

        boolean mB_isRight = sTypedArray.getBoolean(R.styleable.DesignsCustomMenu_designs_isRight, true);
        int mI_rightImgID = sTypedArray.getResourceId(R.styleable.DesignsCustomMenu_designs_rightID, R.drawable.designs_svg_keyboard_arrow_right_black_24dp);

        /*控件属性*/
        boolean mB_isWire = sTypedArray.getBoolean(R.styleable.DesignsCustomMenu_designs_isWire, true);

        /*绑定*/
        setB_isLeft(mB_isLeft);
        setI_leftImgID(mI_leftImgID);

        setS_menuTxt(mS_menuTxt);
        setI_menuTxtColor(sI_menuTxtColor);
        setI_menuTxtSize(sI_menuTxtSize);
        setS_secondaryTxt(mS_secondaryTxt);


        if (sB_isSWitch){
            setB_switch(true);
            setB_isRight(false);
            setSwitchStyle(sI_switchStyle);
        }else {

            setB_switch(false);
            setB_isRight(mB_isRight);
            setI_rightImgID(mI_rightImgID);
        }


        setB_isWire(mB_isWire);
    }
}
