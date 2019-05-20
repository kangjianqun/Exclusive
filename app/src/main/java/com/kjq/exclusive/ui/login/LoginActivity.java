package com.kjq.exclusive.ui.login;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kjq.common.base.mvvm.base.BaseActivity;
import com.kjq.exclusive.AppViewModelFactory;
import com.kjq.exclusive.BR;
import com.kjq.exclusive.R;
import com.kjq.exclusive.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity<ActivityLoginBinding,LoginViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory sAppViewModelFactory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, sAppViewModelFactory).get(LoginViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.pSwitchEvent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
            //pSwitchObservable是boolean类型的观察者,所以可以直接使用它的值改变密码开关的图标
                if (viewModel.uc.pSwitchEvent.getValue()) {
                    //密码可见
                    //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
                    binding.imageView.setImageResource(R.drawable.common_ic_avatar_female);
                    binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
                    binding.imageView.setImageResource(R.drawable.common_ic_avatar_male);
                    binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
