package com.kjq.common.ui.designs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.adapters.ListenerUtil;

import com.kjq.common.R;
import com.kjq.common.utils.data.StringUtils;

/**
 * 自定义的单行菜单
 * Created by Administrator on 2017/11/28 0028.
 */

/**
 * 双向绑定（正向/反向）数据绑定
 *
 * 要支持逆向绑定，首先要在类名上定义 @InverseBindingMethods。
 * attribute = “checkedValue” 是指定支持逆向绑定的属性。
 * event = “checkedValueAttrChanged” 是指定 valueChanged 监听事件。
 * method = “getCheckedValue” 是指定逆向绑定的时候的数据来源方法。
 *
 * event 和 method 都不是必须的，如果不指定，默认会以以下规则自动生成
 * event = “xxxAttrChanged”
 * method = “getXxx”
 *
 * method 的定义还可以直接在方法上面
 * @InverseBindingAdapter (attribute = "checkedValue", event = "checkedValueAttrChanged")
 * public Integer getCheckedValue() {
 *     return checkedValue;
 * }
 * ---------------------
 *
 */
@InverseBindingMethods(
        {@InverseBindingMethod(type = CustomMenu.class, attribute = "commonSwitchState", event = "commonSwitchStateChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonMenuTxt", event = "commonMenuTxtChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonMenuTxtColor", event = "commonRadioStateChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonSecondaryTxt", event = "commonSecondaryTxtChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonText", event = "commonTextChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonHint", event = "commonHintChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonIsRight", event = "commonIsRightChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonInputType", event = "commonInputTypeChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonRadioState", event = "commonRadioStateChanged"),
                @InverseBindingMethod(type = CustomMenu.class, attribute = "commonHintCount", event = "commonHintCountChanged")
        })
public class CustomMenu extends LinearLayout{

    public static final int I_VISIBLE = 0;
    public static final int I_INVISIBLE = 1;
    public static final int I_GONE = 2;

    public static final int I_TEXT_VIEW = 1;
    public static final int I_EDIT_TEXT = 2;
    private static final int RIGHT_TYPE_IMG = 0;
    private static final int RIGHT_TYPE_SWITCH = 1;
    private static final int RIGHT_TYPE_RADIO = 2;

    /*控件*/
    private View mV_wire;
    private ImageView mImgV_left;
    private TextView mTexV_menuTxt;
    private TextView mTexV_secondaryTxt;
    private TextView mTexV_hintCount;
    private ConstraintLayout mCLRoot_right;
    private ImageView mImgV_right;
    private Switch mSwitch_right;
    private AppCompatRadioButton mRadioButton;
    private TextView mTextView_text;
    private EditText mEditText;
    private int mI_isTxt;
    private int mI_rightType;

    private InverseBindingListener mIBL_switchState;
    private InverseBindingListener mIBL_radioState;
    private InverseBindingListener mIBL_secondaryTxt;
    private InverseBindingListener mIBL_text;
    private InverseBindingListener mIBL_hint;
    private InverseBindingListener mIBL_menuTxt;
    private InverseBindingListener mIBL_hintCount;


    /*左边图片相关设置*/
    public void setB_isLeft(boolean b_isLeft){
        mImgV_left.setVisibility(b_isLeft ? VISIBLE:GONE);
    }
    public void setI_leftImgID(int i_leftImgID){
        mImgV_left.setBackgroundResource(i_leftImgID);
    }

    public void setI_menuTxtColor(@ColorInt int color){
        mTexV_menuTxt.setTextColor(color);
    }

    public void setI_menuTxtSize(int size){
        mTexV_menuTxt.setTextSize(size);
    }

    public void setRightShow(int visibility){
        mCLRoot_right.setVisibility(visibility);
    }

    public void setRightType(int type){
        setI_imgShow(type == RIGHT_TYPE_IMG ? View.VISIBLE : View.GONE);
        setI_switchShow(type == RIGHT_TYPE_SWITCH ? View.VISIBLE : View.GONE);
        setI_radioShow(type == RIGHT_TYPE_RADIO ? View.VISIBLE : View.GONE);
    }

    public void setI_imgShow(int visibility){
        mImgV_right.setVisibility(visibility);
    }

    public void setI_switchShow(int visibility){
        mSwitch_right.setVisibility(visibility);
    }

    public void setI_radioShow(int visibility){
        mRadioButton.setVisibility(visibility);
    }


    public void setI_rightImgID(int i_rightImgID){
        mImgV_right.setImageResource(i_rightImgID);
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
        LayoutInflater.from(context).inflate(R.layout.common_custom_menu,this,true);
        mImgV_left = findViewById(R.id.cMenu_left);
        mTexV_menuTxt = findViewById(R.id.cMenu_menuTxt);
        mTexV_secondaryTxt = findViewById(R.id.cMenu_secondaryTxt);
        mV_wire = findViewById(R.id.cMenu_wire);
        mCLRoot_right = findViewById(R.id.common_cMenu_cL_right);
        mRadioButton = findViewById(R.id.common_cMenu_rBtn);
        mImgV_right = findViewById(R.id.cMenu_right);
        mSwitch_right = findViewById(R.id.designs_cMenu_switch_right);
        mTexV_hintCount = findViewById(R.id.common_cMenu_tV_hintCount);
        mTextView_text = findViewById(R.id.common_cMenu_tV_content);
        mEditText = findViewById(R.id.common_cMenu_eT_content);

        /*赋值*/
        TypedArray sTypedArray = context.obtainStyledAttributes(attributeSet,R.styleable.CustomMenu);

        boolean mB_isLeft = sTypedArray.getBoolean(R.styleable.CustomMenu_commonIsLeft, true);
        int mI_leftImgID = sTypedArray.getResourceId(R.styleable.CustomMenu_commonLeftID, R.drawable.common_svg_menu_black_24dp);

        String mS_menuTxt = sTypedArray.getString(R.styleable.CustomMenu_commonMenuTxt);
        int sI_menuTxtColor = sTypedArray.getColor(R.styleable.CustomMenu_commonMenuTxtColor, Color.BLACK);
        int sI_menuTxtSize = sTypedArray.getInt(R.styleable.CustomMenu_commonMenuTxtSize,15);
        String mS_secondaryTxt = sTypedArray.getString(R.styleable.CustomMenu_commonSecondaryTxt);

        mI_rightType = sTypedArray.getInt(R.styleable.CustomMenu_commonRightType,RIGHT_TYPE_IMG);

        int sI_switchThumb = sTypedArray.getResourceId(R.styleable.CustomMenu_commonSwitchThumb,0);
        int sI_switchTrack = sTypedArray.getResourceId(R.styleable.CustomMenu_commonSwitchTrack,0);

        int sI_radioButton = sTypedArray.getResourceId(R.styleable.CustomMenu_commonRadioIsButton,0);
        mI_isTxt = sTypedArray.getInt(R.styleable.CustomMenu_commonIsTxt,0);
        int sI_show = sTypedArray.getInt(R.styleable.CustomMenu_commonIsRight, 0);
        int mI_rightImgID = sTypedArray.getResourceId(R.styleable.CustomMenu_commonRightID, R.drawable.common_svg_keyboard_arrow_right_black_24dp);
        int sI_maxLines = sTypedArray.getInt(R.styleable.CustomMenu_commonMaxLines,1);
        int sI_maxLength = sTypedArray.getInt(R.styleable.CustomMenu_commonMaxLength,0);
//        int sI_inputType = sTypedArray.getInt(R.styleable.CustomMenu_commonInputType, InputType.TYPE_CLASS_TEXT);
        String sS_hint = sTypedArray.getString(R.styleable.CustomMenu_commonHint);

        /*控件属性*/
        boolean mB_isWire = sTypedArray.getBoolean(R.styleable.CustomMenu_commonIsWire, true);
        sTypedArray.recycle();

        if (sI_show == I_VISIBLE){
            sI_show = View.VISIBLE;
        }else if (sI_show == I_INVISIBLE){
            sI_show = View.INVISIBLE;
        }else if (sI_show == I_GONE){
            sI_show = View.GONE;
        }

        /*绑定*/
        if (mI_isTxt == I_TEXT_VIEW) {
            mTextView_text.setVisibility(VISIBLE);
            mTextView_text.setHint(sS_hint);
            if (sI_maxLines > 0){
                mTextView_text.setMaxLines(sI_maxLines);
                if (sI_maxLines == 1){
                    mTextView_text.setSingleLine();
                }
            }
            if (sI_maxLength != 0){
                mTextView_text.setFilters(new InputFilter[] { new InputFilter.LengthFilter(sI_maxLength)});
            }
        }else if (mI_isTxt == I_EDIT_TEXT){
            mEditText.setVisibility(VISIBLE);
            mEditText.setHint(sS_hint);

            final TextWatcher newValue = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mIBL_text != null){
                        mIBL_text.onChange();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            final TextWatcher oldValue = ListenerUtil.trackListener(mEditText, newValue, R.id.textWatcher);
            if (oldValue != null) {
                mEditText.removeTextChangedListener(oldValue);
            }
            mEditText.addTextChangedListener(newValue);

            if (sI_maxLines > 0){
                mEditText.setMaxLines(sI_maxLines);
                if (sI_maxLines == 1){
                    mEditText.setSingleLine();
                }
            }
            if (sI_maxLength != 0){
                mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(sI_maxLength)});
            }
        }
        setB_isLeft(mB_isLeft);
        setI_leftImgID(mI_leftImgID);

        setCommonMenuTxt(mS_menuTxt);
        setI_menuTxtColor(sI_menuTxtColor);
        setI_menuTxtSize(sI_menuTxtSize);
        setCommonSecondaryTxt(mS_secondaryTxt);
        setB_isWire(mB_isWire);


        setRightShow(sI_show);
        if (sI_show == View.GONE){
            return;
        }
        setRightType(mI_rightType);

        if (mI_rightType == RIGHT_TYPE_SWITCH){
            if (sI_switchThumb != 0 && sI_switchTrack != 0){
                mSwitch_right.setThumbResource(sI_switchThumb);
                mSwitch_right.setTrackResource(sI_switchTrack);
            }
            mSwitch_right.setClickable(false);
            mSwitch_right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mIBL_switchState != null){
                        mIBL_switchState.onChange();
                    }
                }
            });
        }else if (mI_rightType == RIGHT_TYPE_RADIO){
            if (sI_radioButton != 0){
                mRadioButton.setClickable(false);
                mRadioButton.setButtonDrawable(sI_radioButton);
                mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (mIBL_radioState != null){
                            mIBL_radioState.onChange();
                        }
                    }
                });
            }
        }else {
            setI_rightImgID(mI_rightImgID);
        }
    }

    public void setCommonHintCount(int commonHintCount){
        if (commonHintCount <= 0){
            mTexV_hintCount.setVisibility(View.GONE);
        }else {
            if (commonHintCount > 99){
                commonHintCount = 99;
            }
            mTexV_hintCount.setVisibility(View.VISIBLE);
            mTexV_hintCount.setText(String.valueOf(commonHintCount));
        }
    }

    public int getCommonHintCount(){
        return Integer.parseInt(mTextView_text.getText().toString());
    }

    public void setCommonHintCountChanged(InverseBindingListener inverseBindingListener){
        if (inverseBindingListener != null){
            mIBL_hintCount = inverseBindingListener;
        }
    }

    public void setCommonSwitchState(boolean commonSwitchState){
        mSwitch_right.setChecked(commonSwitchState);
    }

    public boolean getCommonSwitchState(){
        return mSwitch_right.isChecked();
    }

    public void setCommonSwitchStateChanged(InverseBindingListener inverseBindingListener){
        if (inverseBindingListener != null){
            mIBL_switchState = inverseBindingListener;
        }
    }

    public void setCommonRadioState(boolean commonRadioState){
        mRadioButton.setChecked(commonRadioState);
    }

    public boolean getCommonRadioState(){
        return mRadioButton.isChecked();
    }

    public void setCommonRadioStateChanged(InverseBindingListener inverseBindingListener){
        if (inverseBindingListener != null){
            mIBL_radioState = inverseBindingListener;
        }
    }

    public void setCommonSecondaryTxtChanged(InverseBindingListener bindingListener){
        if (bindingListener != null){
            mIBL_secondaryTxt = bindingListener;
        }
    }

    public String getCommonSecondaryTxt(){
        return mTexV_secondaryTxt.getText().toString();
    }

    public void setCommonTextChanged(InverseBindingListener bindingListener){
        if (bindingListener != null){
            mIBL_text = bindingListener;
        }
    }

    public String getCommonText(){
        if (mI_isTxt == I_EDIT_TEXT){
            return mEditText.getText().toString();
        }else if (mI_isTxt == I_TEXT_VIEW){
            return mTextView_text.getText().toString();
        }else {
            return "";
        }
    }

    public void setCommonIsRight(int rightVisibility){
        setRightShow(rightVisibility);
    }

    public void setCommonInputType(int inputType){
        mEditText.setInputType(inputType);
    }

    public void setCommonText(ObservableField<String> commonText){
        setCommonText(commonText.get());
    }

    public void setCommonText(@StringRes int commonText){
        setCommonText(getResources().getString(commonText));
    }

    public void setCommonText(String commonText){
        if (mI_isTxt == I_EDIT_TEXT){
            mEditText.setText(commonText);
        }else if (mI_isTxt == I_TEXT_VIEW){
            mTextView_text.setText(commonText);
        }
        if (mIBL_text != null){
            mIBL_text.onChange();
        }
    }

    public void setCommonHintChanged(InverseBindingListener bindingListener){
        if (bindingListener != null){
            mIBL_hint = bindingListener;
        }
    }

    public String getCommonHint(){
        if (mI_isTxt == I_EDIT_TEXT){
            return mEditText.getHint().toString();
        }else if (mI_isTxt == I_TEXT_VIEW){
            return mTextView_text.getHint().toString();
        }else {
            return "";
        }
    }

    public void setCommonHint(ObservableField<String> commonHint){
        setCommonHint(commonHint.get());
    }

    public void setCommonHint(@StringRes int commonHint){
        setCommonHint(getResources().getString(commonHint));
    }

    public void setCommonHint(String commonHint){
        if (mI_isTxt == I_EDIT_TEXT){
            mEditText.setHint(commonHint);
        }else if (mI_isTxt == I_TEXT_VIEW){
            mTextView_text.setHint(commonHint);
        }
        if (mIBL_hint != null){
            mIBL_hint.onChange();
        }
    }

    public void setCommonMenuTxtColor(@ColorRes int menuTxtColor){
        setI_menuTxtColor(ContextCompat.getColor(getContext(),menuTxtColor));
    }

    public void setCommonMenuTxtColor(ObservableInt menuTxtColor){
        setI_menuTxtColor(ContextCompat.getColor(getContext(),menuTxtColor.get()));
    }

    public void setCommonMenuTxt(@StringRes int commonMenuTxt){
        setCommonMenuTxt(getResources().getString(commonMenuTxt));
    }

    public void setCommonMenuTxt(ObservableField<String> commonMenuTxt){
        setCommonMenuTxt(commonMenuTxt.get());
    }

    public void setCommonMenuTxt(String commonMenuTxt){
        mTexV_menuTxt.setText(commonMenuTxt);
        if (mIBL_menuTxt != null){
            mIBL_menuTxt.onChange();
        }
    }

    public String getCommonMenuTxt(){
         return mTexV_menuTxt.getText().toString();
    }

    public void setCommonMenuTxtChanged(InverseBindingListener bindingListener){
        if (bindingListener != null){
            mIBL_menuTxt = bindingListener;
        }
    }

    public void setCommonSecondaryTxt(String commonSecondaryTxt){
        mTexV_secondaryTxt.setText(commonSecondaryTxt);
        if (!StringUtils.isEmpty(commonSecondaryTxt)){
            mTexV_secondaryTxt.setVisibility(View.VISIBLE);
        }
        if (mIBL_secondaryTxt != null){
            mIBL_secondaryTxt.onChange();
        }
    }

    public void setCommonSecondaryTxt(int commonSecondaryTxt){
        mTexV_secondaryTxt.setText(commonSecondaryTxt);
        if (mIBL_secondaryTxt != null){
            mIBL_secondaryTxt.onChange();
        }
    }
}
