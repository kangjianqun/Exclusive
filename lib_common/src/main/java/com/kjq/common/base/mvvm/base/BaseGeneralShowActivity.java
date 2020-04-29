package com.kjq.common.base.mvvm.base;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kjq.common.BR;
import com.kjq.common.R;
import com.kjq.common.utils.data.StringUtils;

/**
 * 通用显示容器
 */

public class BaseGeneralShowActivity extends BaseActivity {

    public static final String KEY_PATH = "key_path";
    public static final String KEY_BUNDLE = "key_bundle";
    public static final String KEY_POSTCARD = "key_postcard";

    private String mS_path;
    private Bundle mBundle;
    private Fragment mFragment;

    public static Intent getNewIntent(Context context, String s_path){
        Intent sIntent = new Intent(context, BaseGeneralShowActivity.class);
        Bundle sBundle = new Bundle();
        sBundle.putString(KEY_PATH,s_path);
        sIntent.putExtra("bundle",sBundle);
        return sIntent;
    }

    public static Intent getNewIntent(Context context, String s_path,Bundle bundle){
        Intent sIntent = new Intent(context, BaseGeneralShowActivity.class);
        Bundle sBundle = new Bundle();
        sBundle.putString(KEY_PATH,s_path);
        sBundle.putParcelable(KEY_BUNDLE, bundle);
        sIntent.putExtra("bundle",sBundle);
        return sIntent;
    }

    @Override
    public void initParam(Bundle fragmentOfBundle, Intent activityIntent) {
        Bundle sBundle = activityIntent.getBundleExtra("bundle");
        mS_path = sBundle.getString(KEY_PATH);
        mBundle = sBundle.getParcelable(KEY_BUNDLE);
    }

    @Override
    public void initData() {
        if (StringUtils.isEmpty(mS_path)){
            finish();
        }
        Object sO = ARouter.getInstance().build(mS_path).navigation();
        if (sO instanceof BaseFragment){
            BaseFragment sFragment = (BaseFragment) sO;
            if (mBundle != null){
//                Bundle sBundle = new Bundle();
//                sBundle.putParcelable("data", (Parcelable) mBundle);
                sFragment.setArguments(mBundle);
            }
            addFragment(sFragment);
        }
    }

    @Override
    public int getI_fragmentGroupLayoutId() {
        return R.id.kang_aGShow_content;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.common_activity_general_show;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
