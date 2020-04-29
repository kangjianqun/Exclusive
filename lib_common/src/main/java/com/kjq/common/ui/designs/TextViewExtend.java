package com.kjq.common.ui.designs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import com.kjq.common.R;
import com.kjq.common.utils.data.StringUtils;

@SuppressLint("AppCompatCustomView")

@InverseBindingMethods(
        {@InverseBindingMethod(type = TextViewExtend.class, attribute = "commonText", event = "commonTextChanged")})
public class TextViewExtend extends TextView {
    private InverseBindingListener mIBL_text;

    public TextViewExtend(Context context) {
        this(context,null);
    }

    public TextViewExtend(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextViewExtend(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (getMinEms() == 1){
            StringBuffer sBuffer = resultData(text);
            super.setText(sBuffer, type);
        }else {
            super.setText(text, type);
        }
    }

    private StringBuffer resultData(CharSequence sequence){
        StringBuffer sBuffer = new StringBuffer();
        if (!StringUtils.isEmpty(sequence)) {
            int m = sequence.length();
            for (int i = 0; i < m; i++) {
                CharSequence sCharSequence = sequence.toString().subSequence(i,i+1);
                sBuffer.append(sCharSequence);
                if (i + 1 < m){
                    sBuffer.append("\n");
                }
            }
        }
        return sBuffer;
    }

    public void setCommonText(CharSequence text){
        setText(resultData(text));
        if (mIBL_text != null){
            mIBL_text.onChange();
        }
    }

    public void setCommonTextChanged(InverseBindingListener textChanged){
        if (textChanged != null){
            mIBL_text = textChanged;
        }
    }
}
