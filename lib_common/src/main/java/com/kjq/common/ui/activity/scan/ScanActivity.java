package com.kjq.common.ui.activity.scan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.kjq.common.BR;
import com.kjq.common.R;
import com.kjq.common.base.mvvm.base.BaseActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

public class ScanActivity extends BaseActivity {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.common_activity_scan;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {

    }
}
