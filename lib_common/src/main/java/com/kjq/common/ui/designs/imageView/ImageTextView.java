package com.kjq.common.ui.designs.imageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kjq.common.R;
import com.kjq.common.utils.ScreenSizeUtils;
import com.kjq.common.utils.data.StringUtils;


/**
 * <p>带文字和图片的空间</p>
 *
 * @author 康建群 948182974---->>>2018/9/6 11:56
 * @version 1.0.0
 */
public class ImageTextView extends ConstraintLayout implements View.OnClickListener, View.OnLongClickListener {

    private static final String TXT_LEFT = "0";
    private static final String TXT_TOP = "1";
    private static final String TXT_RIGHT = "2";
    private static final String TXT_BOTTOM = "3";

    private String mS_direction = "0";
    private ImageView mIV_sign;
    private TextView mTV_txt;
    private ConstraintLayout mCLayout_root;

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attributeSet){

        View view = LayoutInflater.from(context).inflate(R.layout.common_image_text_view,this,true);

        mCLayout_root = view.findViewById(R.id.common_dITView_root);
        mIV_sign = view.findViewById(R.id.common_dITView_sign);
        mTV_txt = view.findViewById(R.id.common_dITView_txt);

        if (attributeSet != null){
            @SuppressLint({"Recycle","CustomViewStyleable"})
            TypedArray sTypedArray = context.obtainStyledAttributes(attributeSet,R.styleable.CommonImageTextView);
            setViewParam(sTypedArray);
//            sTypedArray.recycle();
        }
    }

    private void setViewParam(TypedArray sTypedArray){
        String sS_txt = sTypedArray.getString(R.styleable.CommonImageTextView_commonITViewTxt);
        int sI_txtColor = sTypedArray.getColor(R.styleable.CommonImageTextView_commonITViewTxtColor, Color.BLACK);

        int sI_signId = sTypedArray.getResourceId(R.styleable.CommonImageTextView_commonITViewSign, R.drawable.common_svg_star_black_24dp);
        int sI_signBgId = sTypedArray.getResourceId(R.styleable.CommonImageTextView_commonITViewSignBg, R.drawable.common_svg_explode_effects_circle);

        int sI_signW = sTypedArray.getResourceId(R.styleable.CommonImageTextView_commonITViewSignW, 64);
        int sI_signH = sTypedArray.getResourceId(R.styleable.CommonImageTextView_commonITViewSignH, 64);

        int sI_padding = sTypedArray.getInteger(R.styleable.CommonImageTextView_commonITViewPadding, 0);
        mS_direction = sTypedArray.getString(R.styleable.CommonImageTextView_commonITViewDirection);


        setRootPadding(sI_padding);
//        setTxtDirection(mS_direction);
        setTxtColor(sI_txtColor);
        setTxtContent(sS_txt);

        setSignSize(sI_signW, sI_signH);
        setSignRes(sI_signId);
        setSignBgRes(sI_signBgId);
    }

    @Override
    public void onClick(View view) {
        mIV_sign.setPressed(true);
    }

    @Override
    public boolean onLongClick(View view) {
        mIV_sign.setPressed(true);
        return false;
    }

    public void setRootPadding(int i_padding){
        if (i_padding == 0){
            return;
        }
        mCLayout_root.setPadding(i_padding,i_padding,i_padding,i_padding);
    }

    /**
     * 设置文字位于图片的位置
     * @param s_direction 左上右下
     */
    public void setTxtDirection(String s_direction){

        if (s_direction == null){
            s_direction = TXT_BOTTOM;
        }

        ConstraintLayout.LayoutParams sContainer = (LayoutParams) mIV_sign.getLayoutParams();
        int sI_signId = mIV_sign.getId();
        switch (s_direction){
            case TXT_LEFT:
                sContainer.topToTop = sI_signId;
                sContainer.bottomToBottom = sI_signId;
                sContainer.endToStart = sI_signId;
                break;
            case TXT_TOP:
                sContainer.bottomToTop = sI_signId;
                sContainer.startToStart = sI_signId;
                sContainer.endToEnd = sI_signId;
                break;
            case TXT_RIGHT:
                sContainer.topToTop = sI_signId;
                sContainer.bottomToBottom = sI_signId;
                sContainer.startToEnd = sI_signId;
                break;
            case TXT_BOTTOM:
                sContainer.topToBottom = sI_signId;
                sContainer.startToStart = sI_signId;
                sContainer.endToEnd = sI_signId;
                break;
        }
        mTV_txt.setLayoutParams(sContainer);
    }

    public void setTxtColor(int i_txtColor){
        mTV_txt.setTextColor(i_txtColor);
    }

    public void setTxtContent(@StringRes int i_txtContent){
        mTV_txt.setText(i_txtContent);
    }

    public void setTxtContent(String s_txt){
        if (!StringUtils.isEmpty(s_txt)){
            mTV_txt.setText(s_txt);
        }
    }

    /**
     * 设置图片大小
     * @param w 宽
     * @param h 高
     */
    public void setSignSize(int w,int h){
        ConstraintLayout.LayoutParams sContainer = (LayoutParams) mIV_sign.getLayoutParams();
        sContainer.height = ScreenSizeUtils.INSTANCE.dp2px(getContext(),h);
        sContainer.width = ScreenSizeUtils.INSTANCE.dp2px(getContext(),w);
        mIV_sign.setLayoutParams(sContainer);
    }

    public void setSignRes(@DrawableRes int i_signId){
        mIV_sign.setImageResource(i_signId);
    }

    public void setSignBgRes(@DrawableRes int i_signBgId){
        mIV_sign.setBackgroundResource(i_signBgId);
    }

}
