package com.kjq.common.base.mvvm.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.kjq.common.R;
import com.kjq.common.ui.designs.title.BaseTitle;
import com.kjq.common.utils.performance.AppManager;
import com.kjq.common.utils.performance.MemoryUtils;
import com.kjq.common.utils.performance.bus.Messenger;
import com.kjq.common.utils.statusBar.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by kjq on 2019/5/20.
 * 一个拥有DataBinding框架的基类Activity 继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity
        implements IBaseView,EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    public BaseTitle.Builder mBuilder;
    private int mI_fragmentGroupLayoutId;
    private SparseArray<BaseFragment> mSArray_fragment = new SparseArray<>();
    private Bundle mFragmentOfBundle;
    private Intent mActivityIntent;
    private boolean mB_dataFirst = true;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法

        if (null != getIntent()){
            initParam(null,getIntent());
        }
        initStatusBar();
        //私有的初始化DataBinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //初始化标题
        initTitle();

        if (viewModel != null){
            // 给viewModel 传递需要的参数
            transmitParam();
            //私有的ViewModel与View的契约事件回调逻辑
            registeredUIChangeLiveDataCallBack();
            mI_fragmentGroupLayoutId = getI_fragmentGroupLayoutId();
            //页面数据初始化方法
            disposeData();
            //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
            initViewObservable();
            //注册RxBus
            viewModel.registerRxBus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除Messenger注册
        Messenger.getDefault().unregister(viewModel);
        //解除ViewModel生命周期感应
        getLifecycle().removeObserver(viewModel);
        if (viewModel != null) {
            viewModel.removeRxBus();
        }
        if(binding != null){
            binding.unbind();
        }

        Object object = this;
        MemoryUtils.release(object);
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        if (initContentView(savedInstanceState) != 0){
            binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));
            viewModelId = initVariableId();
            viewModel = initViewModel();
            if (viewModel == null) {
                Class modelClass;
                Type type = getClass().getGenericSuperclass();
                if (type instanceof ParameterizedType) {
                    modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                } else {
                    //如果没有指定泛型参数，则默认使用BaseViewModel
                    modelClass = BaseViewModel.class;
                }
                viewModel = (VM) createViewModel(this, modelClass);
            }
            //关联ViewModel
            binding.setVariable(viewModelId, viewModel);
            //让ViewModel拥有View的生命周期感应
            getLifecycle().addObserver(viewModel);
            //注入RxLifecycle生命周期
            viewModel.injectLifecycleProvider(this);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) { //fontScale不为1，需要强制设置为1
            getResources();
        }
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources.getConfiguration().fontScale != 1) { //fontScale不为1，需要强制设置为1
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置成默认值，即fontScale为1
            resources.updateConfiguration(newConfig, resources.getDisplayMetrics());
        }
        return resources;
    }

    private void initStatusBar(){
        if (!isSubclassRealizeFullscreen()){
            //是否侵入式状态栏
            StatusBarUtil.setRootViewFullscreen(this,getIsFullscreen());
        }
    }

    /**
     * 初始化标题
     */
    private void initTitle() {

        if (!isSubclassRealizeFullscreen()){
            mBuilder = new BaseTitle.Builder(this, viewModel);
            //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
            StatusBarUtil.setRootViewFitsSystemWindows(this, getFitSystemWindows(),getTranslucentStatus());
            //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
            //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
            if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
                //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
                //这样半透明+白=灰, 状态栏的文字能看得清
                StatusBarUtil.setStatusBarColor(this, 0x55000000);
//        StatusBarUtil.setStatusBarColor(this, getStatusVarDarkTheme());
            }

            if (mBuilder.titleIsCreate()) {
                viewModel.initToolbar();
            }
        }
    }

    /**
     * 传递参数给viewMode使用
     */
    private void transmitParam(){
        viewModel.initParam(mFragmentOfBundle,mActivityIntent);
    }

    protected boolean isSubclassRealizeFullscreen(){
        return false;
    }

    protected boolean getFitSystemWindows(){
        return true;
    }

    protected boolean getTranslucentStatus(){
        return true;
    }

    protected boolean getIsFullscreen(){
        return false;
    }

    public int getStatusVarDarkTheme(){
        return ContextCompat.getColor(this,R.color.common_colorPrimary);
    }

    //刷新布局
    public void refreshVMBinding() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    /**
     * 当权限被授予
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // 授予权限
    }

    /**
     * 权限被拒绝
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // （可选）检查用户是否拒绝任何权限并选中“从不再问”。 这将显示一个对话框，指示他们在应用程序设置中启用权限。
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

    /**
     * ==================注册ViewModel与View的契约UI回调事件================开始
     **/
    protected void registeredUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUC().getShowDialogEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String title) {
                showDialog(title);
            }
        });
        //加载对话框消失
        viewModel.getUC().getDismissDialogEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissDialog();
            }
        });

        viewModel.getUC().getRefreshVMBindingEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                refreshVMBinding();
            }
        });

        viewModel.getUC().getAddFragment().observe(this, new Observer<BaseFragment>() {
            @Override
            public void onChanged(BaseFragment baseFragment) {
                addFragment(baseFragment);
            }
        });

        viewModel.getUC().getShowFragment().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> params) {
                BaseFragment sBaseFragment = (BaseFragment) params.get(BaseViewModel.ParameterField.BASE_FRAGMENT);
                int sI_tag = (int) params.get(BaseViewModel.ParameterField.FRAGMENT_TAG);
                showFragment(sBaseFragment,sI_tag);
            }
        });

        //跳入新页面
        viewModel.getUC().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });
        //跳入GeneralShowActivity
        viewModel.getUC().getStartContainerActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                String canonicalName = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startGeneralShowActivity(canonicalName, bundle);
            }
        });
        //关闭界面
        viewModel.getUC().getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                finish();
            }
        });
        //关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                onBackPressed();
            }
        });
    }

    /**
     * ==================注册ViewModel与View的契约UI回调事件================结束
     **/

    public void showDialog(String title) {
//        if (dialog != null) {
//            dialog.show();
//        } else {
//            MaterialDialog.Builder builder = MaterialDialogUtils.showIndeterminateProgressDialog(this, title, true);
//            dialog = builder.show();
//        }
    }

    public void dismissDialog() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
    }

    @Override
    public void onBackPressed() {
        if (!BackUtil.handleBackPress(this)) {
            if (AppManager.getActivityStack().size() == 1) {
//                if (mDoubleClickExitDetector.click()) {
//                    removeFragment();
                    AppManager.getAppManager().exit();
//                }
            } else {
                removeFragment();
            }
        }
    }

    /* 添加fragment */
    public void addFragment(BaseFragment fragment) {
        addFragment(mI_fragmentGroupLayoutId == 0 ? mI_fragmentGroupLayoutId : getI_fragmentGroupLayoutId(),fragment);
    }

    /* 添加fragment */
    public void addFragment(int layId,BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(layId, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
            AppManager.getAppManager().addFragment(fragment);
        }
    }

    public void addDefaultFragment(BaseFragment fragment,int tag){
        mSArray_fragment.put(tag,fragment);
    }

    /**
     * 同级下显示，先隐藏全部，再显示当前的
     * @param fragment 内容
     */
    public void showFragment(BaseFragment fragment,int tag){
        if (fragment == null){
            throw new NullPointerException("fragment :" + null +" NullPointerException");
        }
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction sTransaction = mFragmentManager.beginTransaction();
        boolean sB_exist = false;
        if (mSArray_fragment.size() > 0){
            for (int i = 0; i < mSArray_fragment.size(); i++) {
                Fragment sFragment = mSArray_fragment.valueAt(i);
                if (sFragment.getClass().getSimpleName().equals(fragment.getClass().getSimpleName())){
                    sB_exist = true;
                    continue;
                }
                sTransaction.hide(sFragment);
            }
        }

        if (!sB_exist){
            sTransaction.add(getI_fragmentGroupLayoutId() == 0 ? mI_fragmentGroupLayoutId : getI_fragmentGroupLayoutId(), fragment);
        }else {
            sTransaction.show(fragment);
        }
        sTransaction.commit();
        mSArray_fragment.put(tag,fragment);
    }

    /* 移除fragment */
    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转容器页面
     *
     * @param sPath
     */
    public void startGeneralShowActivity(String sPath) {
        startGeneralShowActivity(sPath, null);
    }

    /**
     * 跳转容器页面
     *
     * @param sPath
     * @param bundle        跳转所携带的信息
     */
    public void startGeneralShowActivity(String sPath, Bundle bundle) {
        if (bundle == null){
            startActivity(BaseGeneralShowActivity.getNewIntent(this,sPath));
        }else {
            startActivity(BaseGeneralShowActivity.getNewIntent(this,sPath,bundle));
        }
    }

    /**
     * =====================================================================
     **/

    @Override
    public void initParam(Bundle fragmentOfBundle, Intent activityIntent) {
        mFragmentOfBundle = fragmentOfBundle;
        mActivityIntent = activityIntent;
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    public int getI_fragmentGroupLayoutId(){
        return 0;
    }

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    /**
     * 处理数据
     */
    private void disposeData(){
        if (mB_dataFirst){
            initData();
            mB_dataFirst = !mB_dataFirst;
        }
        refreshData();
    }

    /**
     * 生命周期中只执行一次
     */
    @Override
    public void initData() {

    }

    /**
     * 生命周期中每次都会执行
     */
    @Override
    public void refreshData() {

    }

    @Override
    public void initViewObservable() {

    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }
}
