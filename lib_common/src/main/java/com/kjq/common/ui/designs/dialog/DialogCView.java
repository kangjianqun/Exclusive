package com.kjq.common.ui.designs.dialog;

import android.content.Context;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.StyleRes;
import androidx.databinding.DataBindingUtil;

import com.kjq.common.databinding.CommonDialogOkNoDefaultBinding;
import com.kjq.common.databinding.CommonDialogTimeSelectDefaultBinding;
import com.kjq.common.databinding.CommonDialogTimeTwoSelectDefaultBinding;
import com.kjq.common.databinding.CommonDialogValueDefaultBinding;
import com.kjq.common.ui.designs.dialog.litener.ViewEven;
import com.kjq.common.utils.DateAndTimeUtil;
import com.kjq.common.utils.data.StringUtils;


/**
 * 弹出自定义View
 */
public abstract class DialogCView extends DialogBase {

    private String mS_diyTitle;
    private String mS_diyHint;
    public boolean mB_isDisplayOk = true;

    protected DialogCView(Context context) {
        super(context);
    }

    protected DialogCView(Context context, String title, String hint){
        super(context);
        mS_diyTitle = title;
        mS_diyHint = hint;

    }

    protected DialogCView(Context context, @StyleRes int i_styId){
        super(context,i_styId);
    }

    @Override
    public void setViewInfo(View view) {
        setEven(view);
    }

    public abstract void setEven (View view);

    public void setValueDialog(){
        final CommonDialogValueDefaultBinding sBinding = DataBindingUtil.bind(getView());
        if (sBinding == null) {
            return;
        }
        sBinding.setTitleTxt(mS_diyTitle);
        sBinding.setHintTxt(mS_diyHint);
        if (!mB_isBack){
            sBinding.dValueTViewNo.setVisibility(View.GONE);
        }

        sBinding.dValueTViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                getViewEven().okListener(sBinding.dValueTViewOk,sBinding.dValueETextValue.getText().toString().trim());
            }
        });

        sBinding.dValueTViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                getViewEven().noListener(sBinding.dValueTViewOk);
            }
        });
    }

    void setReconfirmDialog(){
        final CommonDialogOkNoDefaultBinding sBinding = DataBindingUtil.bind(getView());
        if (sBinding == null) {
            return;
        }
        sBinding.setTitleTxt(mS_diyTitle);
        sBinding.setHintTxt(mS_diyHint);
        if (!mB_isBack){
            sBinding.dONOTViewNo.setVisibility(View.GONE);
        }
        if (!mB_isDisplayOk){
            sBinding.dONOTViewOk.setVisibility(View.GONE);
            sBinding.dONOTViewNo.setVisibility(View.VISIBLE);
        }

        sBinding.dONOTViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                getViewEven().okListener(sBinding.dONOTViewOk);
            }
        });

        sBinding.dONOTViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                getViewEven().noListener(sBinding.dONOTViewNo);
            }
        });
    }

    public void show(String hour, String minute, ViewEven viewEven) {
        setTimeSelectDialog(hour,minute);
        super.show(viewEven);
    }

    public void show(String hour, ViewEven viewEven){
        setTimeSelectDialog(hour,"");
        super.show(viewEven);
    }

    public void show(String oneHour,String oneMinute,String twoHour,String twoMinute,ViewEven viewEven){
        setTimeSelectDialog(oneHour,oneMinute,twoHour,twoMinute);
        super.show(viewEven);
    }

    @Override
    public void show(ViewEven even) {
        setViewEven(even);
        setViewInfo(getView());
        super.show(even);
    }

    void setTimeSelectDialog(String hour, String minute){
        int sI_hour = 8,sI_minute = 30;
        final CommonDialogTimeSelectDefaultBinding sBinding = DataBindingUtil.bind(getView());
        final NumberPicker sPicker_hour = sBinding.dTSDefaultHour;
        sPicker_hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        sPicker_hour.setMaxValue(23);
        sPicker_hour.setMinValue(0);
        if (!StringUtils.isEmpty(hour)){
            sI_hour = Integer.parseInt(hour);
        }
        sPicker_hour.setValue(sI_hour);

        final NumberPicker sPicker_minute = sBinding.dTSDefaultMinute;
        sPicker_minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        sPicker_minute.setMaxValue(59);
        sPicker_minute.setMinValue(00);
        if (!StringUtils.isEmpty(minute)){
            sI_minute = Integer.parseInt(minute);
        }
        sPicker_minute.setValue(sI_minute);

        sBinding.dTSDefaultNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        sBinding.dTSDefaultOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sS_time = DateAndTimeUtil.format(sPicker_hour.getValue()) + ":" + DateAndTimeUtil.format(sPicker_minute.getValue());
                getViewEven().okListener(sBinding.dTSDefaultOk,sS_time);
                dismissDialog();
            }
        });
    }

    void setTimeSelectDialog(String hour, String minute,String twoHour,String twoMinute){
        int sI_hour = 8,sI_twoHour = 8,sI_minute = 30,sI_twoMinute = 30;
        final CommonDialogTimeTwoSelectDefaultBinding sBinding = DataBindingUtil.bind(getView());
        final NumberPicker sPicker_hour = sBinding.dTTSDefaultOneHour;
        sPicker_hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        sPicker_hour.setMaxValue(23);
        sPicker_hour.setMinValue(0);
        if (!StringUtils.isEmpty(hour)){
            sI_hour = Integer.parseInt(hour);
        }
        sPicker_hour.setValue(sI_hour);

        final NumberPicker sPicker_twoHour = sBinding.dTTSDefaultTwoHour;
        sPicker_twoHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        sPicker_twoHour.setMaxValue(23);
        sPicker_twoHour.setMinValue(0);
        if (!StringUtils.isEmpty(twoHour)){
            sI_twoHour = Integer.parseInt(twoHour);
        }
        sPicker_twoHour.setValue(sI_twoHour);

        final NumberPicker sPicker_minute = sBinding.dTTSDefaultOneMinute;
        sPicker_minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        sPicker_minute.setMaxValue(59);
        sPicker_minute.setMinValue(00);
        if (!StringUtils.isEmpty(minute)){
            sI_minute = Integer.parseInt(minute);
        }
        sPicker_minute.setValue(sI_minute);

        final NumberPicker sPicker_twoMinute = sBinding.dTTSDefaultTwoMinute;
        sPicker_twoMinute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        sPicker_twoMinute.setMaxValue(59);
        sPicker_twoMinute.setMinValue(00);
        if (!StringUtils.isEmpty(twoMinute)){
            sI_twoMinute = Integer.parseInt(twoMinute);
        }
        sPicker_twoMinute.setValue(sI_twoMinute);

        sBinding.dTTSDefaultNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        sBinding.dTTSDefaultOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sS_time = DateAndTimeUtil.format(sPicker_hour.getValue()) + ":" + DateAndTimeUtil.format(sPicker_minute.getValue());
                String sS_twoTime = DateAndTimeUtil.format(sPicker_twoHour.getValue()) + ":" + DateAndTimeUtil.format(sPicker_twoMinute.getValue());
                getViewEven().okListener(sBinding.dTTSDefaultOk,sS_time,sS_twoTime);
                dismissDialog();
            }
        });
    }

}
