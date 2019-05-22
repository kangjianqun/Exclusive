package com.kjq.exclusive.ui.login;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.kjq.common.base.mvvm.base.BaseViewModel;
import com.kjq.common.utils.binding.command.BindingAction;
import com.kjq.common.utils.binding.command.BindingCommand;
import com.kjq.common.utils.bus.RxUtils;
import com.kjq.common.utils.bus.event.SingleLiveEvent;
import com.kjq.common.utils.hint.ToastUtils;
import com.kjq.exclusive.data.model.DemoRepository;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginViewModel extends BaseViewModel<DemoRepository> {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application, DemoRepository model) {
        super(application, model);
        userName.set(model.getUserName());
        password.set(model.getPassword());
    }

    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call(View view) {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(uc.pSwitchEvent.getValue() == null || !uc.pSwitchEvent.getValue());
        }
    });

    /**
     * 网络模拟一个登陆操作
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        //RaJava模拟登录
        addSubscribe(model.login()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismissDialog();
                        //保存账号密码
                        model.saveUserName(userName.get());
                        model.savePassword(password.get());
                        //进入DemoActivity页面
//                        startActivity(DemoActivity.class);
                        //关闭页面
                        finish();
                    }
                }));

    }

    @Override
    public void back() {
        ToastUtils.showShort("111");
    }

    @Override
    public void menu(View view) {
        ToastUtils.showShort("222");
    }
}
