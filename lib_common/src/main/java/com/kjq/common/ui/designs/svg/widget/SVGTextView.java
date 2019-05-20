package com.kjq.common.ui.designs.svg.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kjq.common.ui.designs.svg.CompoundSVGParameter;

/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/9/20 17:27
 * @version 1.0.0
 */
@SuppressLint("AppCompatCustomView")
public class SVGTextView extends TextView {
    private CompoundSVGParameter[] mCompoundSVGParameters;


    public SVGTextView(Context context) {
        super(context);
    }

    public SVGTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SVGTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
