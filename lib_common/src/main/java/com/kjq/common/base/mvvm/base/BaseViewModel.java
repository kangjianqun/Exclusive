package com.kjq.common.base.mvvm.base;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.kjq.common.ui.designs.title.event.TitleClickListener;
import com.kjq.common.utils.AppManager;
import com.kjq.common.utils.binding.command.BindingAction;
import com.kjq.common.utils.binding.command.BindingCommand;
import com.kjq.common.utils.bus.event.SingleLiveEvent;
import com.trello.rxlifecycle2.LifecycleProvider;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by goldze on 2017/6/15.
 */
public class BaseViewModel<M extends BaseModel> extends AndroidViewModel implements IBaseViewModel
        , Consumer<Disposable>
        , TitleClickListener {
    protected M model;
    private UIChangeLiveData uc;
    //弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private CompositeDisposable mCompositeDisposable;

    //标题文字
    public ObservableField<String> mOFS_titleText = new ObservableField<>("");
    //右边文字
    public ObservableField<String> mOFS_rightText = new ObservableField<>("更多");
    public ObservableInt mOI_titleVisibility = new ObservableInt(View.VISIBLE);
    //右边文字的观察者
    public ObservableInt mOI_txtVisibility = new ObservableInt(View.GONE);
    //右边图标的观察者
    public ObservableInt mOI_menuVisibility = new ObservableInt(View.GONE);

    public BaseViewModel(@NonNull Application application) {
        this(application, null);
    }

    public BaseViewModel(@NonNull Application application, M model) {
        super(application);
        this.model = model;
        mCompositeDisposable = new CompositeDisposable();
    }

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    public UIChangeLiveData getUC() {
        if (uc == null) {
            uc = new UIChangeLiveData();
        }
        return uc;
    }

    public void showDialog() {
        showDialog("请稍后...");
    }

    public void showDialog(String title) {
        uc.showDialogEvent.postValue(title);
    }

    public void dismissDialog() {
        uc.dismissDialogEvent.call();
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startActivityEvent.postValue(params);
    }

    public void addFragment(BaseFragment baseFragment){
        uc.addFragment.postValue(baseFragment);
    }

    public void showFragment(BaseFragment baseFragment,int tag){
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.BASE_FRAGMENT,baseFragment);
        params.put(ParameterField.FRAGMENT_TAG,tag);
        uc.showFragment.postValue(params);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CANONICAL_NAME, canonicalName);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startContainerActivityEvent.postValue(params);
    }

    /**
     * 关闭界面
     */
    public void finish() {
        uc.finishEvent.call();
    }

    protected void initToolbar(){

    }

    /**
     * 返回上一层
     */
    public void onBackPressed() {
        uc.onBackPressedEvent.call();
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void registerRxBus() {
    }

    @Override
    public void removeRxBus() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (model != null) {
            model.onCleared();
        }
        //ViewModel销毁时会执行，同时取消所有异步任务
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void accept(Disposable disposable) throws Exception {
        addSubscribe(disposable);
    }

    /**
     * 设置标题
     *
     * @param text 标题文字
     */
    protected void setTitleText(String text) {
        mOFS_titleText.set(text);
        mOI_menuVisibility.set(View.VISIBLE);
    }

    /**
     * 设置右边文字
     *
     * @param text 右边文字
     */
    protected void setRightText(String text) {
        mOFS_rightText.set(text);
        mOI_txtVisibility.set(View.VISIBLE);
    }

    /**
     * 设置右边文字的显示和隐藏
     *
     * @param visibility
     */
    protected void setTitleTextVisible(int visibility) {
        mOI_txtVisibility.set(visibility);
    }

    protected void setTitleVisibility(int visibility){
        mOI_titleVisibility.set(visibility);
    }

    /**
     * 设置右边图标的显示和隐藏
     *
     * @param visibility
     */
    protected void setTitleMenuVisible(int visibility) {
        mOI_menuVisibility.set(visibility);
    }

    /**
     * 返回按钮的点击事件
     */
    public final BindingCommand backOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call(View view) {
            back();
        }
    });

    public BindingCommand rightOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call(View view) {
            menu(view);
        }
    });

    @Override
    public void back() {
        AppManager sAppManager = AppManager.getAppManager();
        if (sAppManager.isFragment()){
            sAppManager.removeFragment(sAppManager.currentFragment());
        }else {
            sAppManager.removeActivity(sAppManager.currentActivity());
        }
    }

    @Override
    public void menu(View view) {

    }

    public final class UIChangeLiveData extends SingleLiveEvent {
        private SingleLiveEvent<String> showDialogEvent;
        private SingleLiveEvent<Void> dismissDialogEvent;
        private SingleLiveEvent<BaseFragment> addFragment;
        private SingleLiveEvent<Map<String,Object>> showFragment;
        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        private SingleLiveEvent<Map<String, Object>> startContainerActivityEvent;
        private SingleLiveEvent<Void> finishEvent;
        private SingleLiveEvent<Void> onBackPressedEvent;

        public SingleLiveEvent<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public SingleLiveEvent<Void> getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public SingleLiveEvent<BaseFragment> getAddFragment(){
            return addFragment = createLiveData(addFragment);
        }

        public SingleLiveEvent<Map<String, Object>> getShowFragment() {
            return showFragment = createLiveData(showFragment);
        }

        public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public SingleLiveEvent<Map<String, Object>> getStartContainerActivityEvent() {
            return startContainerActivityEvent = createLiveData(startContainerActivityEvent);
        }

        public SingleLiveEvent<Void> getFinishEvent() {
            return finishEvent = createLiveData(finishEvent);
        }

        public SingleLiveEvent<Void> getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }

        private SingleLiveEvent createLiveData(SingleLiveEvent liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent();
            }
            return liveData;
        }

        @Override
        public void observe(@NotNull LifecycleOwner owner, @NotNull Observer observer) {
            super.observe(owner, observer);
        }
    }

    public static final class ParameterField {
        public static final String FRAGMENT_TAG = "fragmentTag";
        public static final String BASE_FRAGMENT = "baseFragment";
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }
}
