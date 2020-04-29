package com.kjq.common.base.mvvm.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.kjq.common.R;
import com.kjq.common.ui.designs.title.event.TitleClickListener;
import com.kjq.common.utils.Utils;
import com.kjq.common.utils.binding.command.BindingAction;
import com.kjq.common.utils.binding.command.BindingCommand;
import com.kjq.common.utils.data.StringUtils;
import com.kjq.common.utils.hint.ToastUtils;
import com.kjq.common.utils.performance.AppManager;
import com.kjq.common.utils.performance.bus.event.SingleLiveEvent;
import com.kjq.common.utils.statusBar.StatusBarUtil;
import com.trello.rxlifecycle2.LifecycleProvider;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by kjq on 2019/5/20.
 */
public class BaseViewModel<M extends BaseModel> extends AndroidViewModel implements IBaseViewModel
        , Consumer<Disposable>, TitleClickListener,IBackListener {
    protected M model;
    private UIChangeLiveData uc;
    //弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private CompositeDisposable mCompositeDisposable;

    public ObservableInt mOI_titleBgColor = new ObservableInt(0x00000000);
    //标题文字
    public ObservableField<String> mOFS_titleText = new ObservableField<>("");

    public ObservableInt mOI_titleColor = new ObservableInt(ContextCompat.getColor(Utils.getContext(),R.color.common_black));
    //右边文字
    public ObservableField<String> mOFS_rightText = new ObservableField<>("更多");
    public ObservableField<Drawable> mOFD_rightBg = new ObservableField<>();
    public ObservableInt mOI_rightSeyleRes = new ObservableInt();
    public ObservableInt mOI_rightColor = new ObservableInt(ContextCompat.getColor(Utils.getContext(),R.color.common_black));
    public ObservableField<Drawable> mOFD_rightImg = new ObservableField<>(ContextCompat.getDrawable(Utils.getContext(), R.drawable.common_svg_menu_black_24dp));
    public ObservableField<Drawable> mOFD_backImg = new ObservableField<>(ContextCompat.getDrawable(Utils.getContext(), R.drawable.common_svg_chevron_left_black_24dp));
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

    public void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void initParam(Bundle fragmentOfBundle, Intent activityIntent){

    }

    /**
     * Activity和Fragment调用标题
     */

    @CallSuper
    protected void initToolbar(){

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
        if (uc != null && uc.dismissDialogEvent != null){
            uc.showDialogEvent.postValue(title);
        }
    }

    public void refreshVMBinding(){
        if (uc != null && uc.dismissDialogEvent != null){
            uc.refreshVMBindingEvent.call();
        }
    }

    public void dismissDialog() {
        if (uc != null && uc.dismissDialogEvent != null){
            uc.dismissDialogEvent.call();
        }
    }

    public void toastShow(CharSequence msg){
        ToastUtils.showShort(msg);
    }

    public void toastShow(@StringRes int stringRes){
        ToastUtils.showShort(stringRes);
    }

    /*
      #############################跳转Activity和Fragment 开始############################
     */

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

    public void removeFragment(){
        uc.onBackPressedEvent.call();
    }

    /**
     * 跳转容器页面
     *
     * @param sPath 路由路径
     */
    public void startContainerActivity(String sPath) {
        startContainerActivity(sPath, null);
    }

    /**
     * 跳转容器页面
     *
     * @param sPath 路由路径
     * @param bundle 跳转所携带的信息
     */
    public void startContainerActivity(String sPath, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CANONICAL_NAME, sPath);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startContainerActivityEvent.postValue(params);
    }


    /*
      #############################跳转Activity和Fragment 开始############################
     */

    /**
     * 关闭界面
     */
    public void finish() {
        uc.finishEvent.call();
    }


    /**
     * 设置标题背景颜色
     * @param color
     */
    protected void setTitleBgColor(@ColorRes int color){
        mOI_titleBgColor.set(ContextCompat.getColor(Utils.getContext(),color));
    }

    /**
     * 设置标题
     *
     * @param text 标题文字
     */
    protected void setTitleText(String text) {
        mOFS_titleText.set(text);
    }

    protected void setTitleTextColor(@ColorRes int color){
        mOI_titleColor.set(ContextCompat.getColor(Utils.getContext(),color));
    }

    /**
     * 设置右边文字
     *
     * @param text 右边文字
     */
    protected void setRightText(String text) {
        setRightText(text,R.color.common_black);
    }

    /**
     * 设置右边文字
     *
     * @param text 右边文字
     */
    protected void setRightText(String text, int color) {
        setRightText(text,color,0);
    }

    /**
     * 设置右边文字
     *
     * @param text 右边文字
     */
    protected void setRightText(String text, int color,@DrawableRes int bg) {
        if (StringUtils.isEmpty(text)){
            mOI_txtVisibility.set(View.GONE);
        }else {
            mOFS_rightText.set(text);
            mOI_rightColor.set(ContextCompat.getColor(Utils.getContext(),color));
            mOI_txtVisibility.set(View.VISIBLE);
            if (bg != 0){
                mOFD_rightBg.set(ContextCompat.getDrawable(Utils.getContext(),bg));
            }
        }
    }



    protected void setRightImg(@DrawableRes int imgRes){
        mOFD_rightImg.set(ContextCompat.getDrawable(Utils.getContext(),imgRes));
        mOI_menuVisibility.set(View.VISIBLE);
    }

    protected void setBackImg(@DrawableRes int imgRes){
        mOFD_backImg.set(ContextCompat.getDrawable(Utils.getContext(),imgRes));
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

    protected void setStatusBarColor(@ColorRes int colorInt){
        setStatusBarColor(colorInt,true);
    }

    protected void setStatusBarColor(@ColorRes int colorInt,boolean isAuto){
        int sI_colorInt = ContextCompat.getColor(Utils.getContext(),colorInt);
        Activity sActivity = AppManager.getAppManager().currentActivity();
        StatusBarUtil.setStatusBarColor(sActivity, sI_colorInt);
        if (isAuto){
            if (StatusBarUtil.isLightColor(sI_colorInt)){
                StatusBarUtil.setStatusBarDarkTheme(sActivity,true);
            }else {
                StatusBarUtil.setStatusBarDarkTheme(sActivity,false);
            }
        }
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

    public BindingCommand baseClick = new BindingCommand(new BindingAction() {
        @Override
        public void call(View view) {
            onBaseClick(view);
        }
    });

    public BindingCommand baseLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call(View view) {
            onBaseLongClick(view);
        }
    });

    protected void onBaseLongClick(View view) {

    }


    protected void onBaseClick(@NotNull View view) {

    }

    /**
     * 菜单返回键监听
     */
    @Override
    public void back() {
        removeFragment();
    }

    /**
     * 菜单右侧监听
     */
    @Override
    public void menu(View view) {

    }

    /**
     * 监听返回键
     */
    public boolean onBackPressed() {
//        uc.onBackPressedEvent.call();
        return false;
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


    public final class UIChangeLiveData extends SingleLiveEvent {
        private SingleLiveEvent<String> showDialogEvent;
        private SingleLiveEvent<Void> dismissDialogEvent;
        private SingleLiveEvent<Void> refreshVMBindingEvent;
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

        public SingleLiveEvent<Void> getRefreshVMBindingEvent(){
            return refreshVMBindingEvent = createLiveData(refreshVMBindingEvent);
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

        @Contract("!null -> param1")
        private SingleLiveEvent createLiveData(SingleLiveEvent liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent();
            }
            return liveData;
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
            super.observe(owner, observer);
        }
    }

    public static final class ParameterField {
        static final String FRAGMENT_TAG = "fragmentTag";
        static final String BASE_FRAGMENT = "baseFragment";
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }
}
