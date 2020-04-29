package com.kjq.common.utils.binding.viewadapter.textView;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableInt;

import com.kjq.common.utils.Utils;
import com.kjq.common.utils.binding.command.BindingCommand;

import org.jetbrains.annotations.NotNull;

public class ViewAdapter {

    /**
     *
     */
    @BindingAdapter(value = {"maxLength"})
    public static void setMaxLength(TextView textView, int maxLength) {
        textView.setMaxHeight(maxLength);
    }

    @BindingAdapter(value = {"commonTextStyle"})
    public static void setTextStyle(TextView textView, ObservableInt style) {
        textView.setTypeface(Typeface.DEFAULT,style.get());
    }

    @BindingAdapter(value = {"commonTextSize"})
    public static void setTextSize(@NotNull TextView textView, @NotNull ObservableInt style) {
        textView.setTextSize(textView.getContext().getResources().getDimension(style.get()));
    }

    @BindingAdapter(value = {"commonTextColor"})
    public static void setTextColor(@NotNull TextView textView, @NotNull ObservableInt color){
        textView.setTextColor(ContextCompat.getColor(textView.getContext(),color.get()));
    }

}
