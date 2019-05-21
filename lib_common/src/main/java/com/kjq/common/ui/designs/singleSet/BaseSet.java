package com.kjq.common.ui.designs.singleSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.kjq.common.R;
import com.kjq.common.databinding.CommonBaseSetBinding;
import com.kjq.common.utils.data.StringUtils;


/**
 * <p>基类设置</p>
 *
 * @author 康建群 948182974---->>>2018/9/25 17:02
 * @version 1.0.0
 */
public class BaseSet extends ConstraintLayout {

    private CommonBaseSetBinding mBinding;

    public BaseSet(Context context) {
        super(context);
    }

    public BaseSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public BaseSet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attributeSet){
        Context sContext = getContext();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(sContext), R.layout.common_base_set,this,true);
        if (attributeSet != null){
            @SuppressLint("CustomViewStyleable")
            TypedArray sTypedArray = sContext.obtainStyledAttributes(attributeSet, R.styleable.CommonSingleSet);
            initAttr(sTypedArray);
            sTypedArray.recycle();
        }
    }

    private void initAttr(TypedArray typedArray) {
        String sS_txt = typedArray.getString(R.styleable.CommonSingleSet_commonSSetTxt);
        setTxt(sS_txt);



        String sS_rightStyle = typedArray.getString(R.styleable.CommonSingleSet_commonSSetRightStyle);
        setRightStyle(sS_rightStyle);



        String sS_secondaryTxt = typedArray.getString(R.styleable.CommonSingleSet_commonSSetSecondaryTxt);
        setSecondaryTxt(sS_secondaryTxt);
    }

    public void setTxt(String txt){
        if (StringUtils.isEmpty(txt)){
            return;
        }
        mBinding.designsBSetTxt.setText(txt);
    }

    public void setSecondaryTxt(String secondaryTxt){
        if (StringUtils.isEmpty(secondaryTxt)){
            return;
        }
        mBinding.designsBSetSecondaryTxt.setText(secondaryTxt);
    }

    /**
     * 设置右边样式
     * @param rightStyle null代表 默认
     */
    private void setRightStyle(@Nullable String rightStyle){
        if (StringUtils.isEmpty(rightStyle)){
            rightStyle = "0";
        }
        switch (rightStyle){
            case "0":
                mBinding.designsBSetRightSign.setVisibility(VISIBLE);
                break;
            case "1":
                mBinding.designsBSetRightSwitch.setVisibility(VISIBLE);
                break;
            case "2":
                mBinding.designsBSetSecondaryTxt.setVisibility(VISIBLE);
                break;
        }
    }
}
