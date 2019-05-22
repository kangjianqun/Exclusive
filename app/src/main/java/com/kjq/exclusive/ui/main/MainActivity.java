package com.kjq.exclusive.ui.main;


import android.os.Bundle;

import com.kjq.common.base.mvvm.base.BaseActivity;
import com.kjq.exclusive.BR;
import com.kjq.exclusive.R;
import com.kjq.exclusive.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainVM> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public int getI_fragmentGroupLayoutId() {
        return R.id.main_fL;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BlankFragment sBlankFragment = BlankFragment.newInstance();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_fL, sBlankFragment, sBlankFragment.getClass().getSimpleName())
//                    .addToBackStack(sBlankFragment.getClass().getSimpleName())
//                    .commitAllowingStateLoss();

        startGeneralShowActivity("/app/blank");
//        showFragment(sBlankFragment,0);
//        showFragment(sBlankFragment,0);
    }
}
