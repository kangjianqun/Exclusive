package com.kjq.common.base.mvvm.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kjq.common.ui.designs.title.BaseTitle;
import com.kjq.common.utils.hint.ToastUtils;
import com.kjq.common.utils.performance.bus.Messenger;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by kjq on 2019/5/20.
 */
public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends RxFragment
        implements IBaseView ,IBackListener, EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    private BaseActivity mBaseActivity;
    private BaseTitle.Builder mBuilder;
    private Bundle mFragmentOfBundle;
    private Intent mActivityIntent;
    private boolean mB_dataFirst = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity = (BaseActivity)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {
            initParam(getArguments(),null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
        return binding.getRoot();
    }

    /* 是否监听了返回 */
    @Override
    public boolean onBackPressed() {
        return viewModel.onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除Messenger注册
        Messenger.getDefault().unregister(viewModel);
        //解除ViewModel生命周期感应
        getLifecycle().removeObserver(viewModel);
        if (viewModel != null) {
            viewModel.removeRxBus();
        }
        if (binding != null) {
            binding.unbind();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBuilder = mBaseActivity.mBuilder;
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        // 给viewModel 传递需要的参数
        transmitParam();
        //私有的ViewModel与View的契约事件回调逻辑
        registeredUIChangeLiveDataCallBack();
        //页面数据初始化方法
        disposeData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerRxBus();
    }

    /**
     * 传递参数给viewMode使用
     */
    private void transmitParam(){
        viewModel.initParam(mFragmentOfBundle,mActivityIntent);
    }
    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
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
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //初始化标题
        initTitle();
    }

    private void initTitle(){
        if (mBuilder != null){
            if (mBuilder.titleIsCreate()){
                mBuilder.setVM(viewModel);
                viewModel.initToolbar();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // 授予权限
    }

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

    public void toastShow(CharSequence msg){
        ToastUtils.showShort(msg);
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
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
                String sPath = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startGeneralShowActivity(sPath, bundle);
            }
        });

        //关闭界面
        viewModel.getUC().getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                getActivity().finish();
            }
        });
        //关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                removeFragment();
            }
        });
    }

    public void showDialog(String title) {
//        if (dialog != null) {
//            dialog.show();
//        } else {
//            MaterialDialog.Builder builder = MaterialDialogUtils.showIndeterminateProgressDialog(getActivity(), title, true);
//            dialog = builder.show();
//        }
    }

    public void dismissDialog() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getContext(), clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void startActivity(String path,Bundle bundle){
        Object sO = ARouter.getInstance().build(path).navigation();
        if (sO instanceof BaseActivity){
            BaseActivity sBaseActivity = (BaseActivity) sO;
            startActivity(sBaseActivity.getClass(),bundle);
        }
    }

    public void startActivity(String path){
        Object sO = ARouter.getInstance().build(path).navigation();
        if (sO instanceof BaseActivity){
            BaseActivity sBaseActivity = (BaseActivity) sO;
            startActivity(sBaseActivity.getClass());
        }
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
            startActivity(BaseGeneralShowActivity.getNewIntent(getContext(),sPath));
        }else {
            startActivity(BaseGeneralShowActivity.getNewIntent(getContext(),sPath,bundle));
        }
    }

    /*添加fragment*/
    protected void addFragment(BaseFragment fragment) {
        if (null != fragment) {
            mBaseActivity.addFragment(fragment);
        }
    }

    /*添加fragment*/
    protected void addFragment(int layId,BaseFragment fragment) {
        if (null != fragment) {
            mBaseActivity.addFragment(layId,fragment);
        }
    }

    /*移除fragment*/
    protected void removeFragment() {
        mBaseActivity.removeFragment();
    }

    /**
     * =====================================================================
     **/

    //刷新布局
    public void refreshVMBinding() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

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
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    protected VM initViewModel() {
        return null;
    }

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

    public boolean isBackPressed() {
        return false;
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }
}
