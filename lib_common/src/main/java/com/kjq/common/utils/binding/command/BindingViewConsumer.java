package com.kjq.common.utils.binding.command;

import android.view.View;

public interface BindingViewConsumer<T> {
    void call(View view, T t);
}
