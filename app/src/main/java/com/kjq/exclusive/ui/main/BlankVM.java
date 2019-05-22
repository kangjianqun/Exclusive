package com.kjq.exclusive.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;

import com.kjq.common.base.mvvm.base.BaseViewModel;
import com.kjq.common.utils.hint.ToastUtils;

public class BlankVM extends BaseViewModel {
    public BlankVM(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setTitleText("hahaha");
    }

    @Override
    public void back() {
        ToastUtils.showShort("BlankVM");
    }
}
