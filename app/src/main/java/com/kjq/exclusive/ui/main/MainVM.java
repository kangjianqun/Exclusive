package com.kjq.exclusive.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;

import com.kjq.common.base.mvvm.base.BaseViewModel;
import com.kjq.common.utils.hint.ToastUtils;

public class MainVM extends BaseViewModel {

    public MainVM(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void initToolbar() {
        setTitleText("12345");
    }

    @Override
    public void back() {
        ToastUtils.showShort("MainVM");
    }
}
