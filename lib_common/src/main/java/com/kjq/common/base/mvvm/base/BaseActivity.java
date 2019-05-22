package com.kjq.common.base.mvvm.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.kjq.common.ui.designs.title.BaseTitle;
import com.kjq.common.utils.bus.Messenger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;


/**
 * Created by goldze on 2017/6/15.
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements IBaseView {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    public BaseTitle.Builder mBuilder;
    private int mI_fragmentGroupLayoutId;
    private SparseArray<BaseFragment> mSArray_fragment = new SparseArray<>();

//    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法

        if (null != getIntent()){
            initParam(null,getIntent());
        }
        //私有的初始化DataBinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //初始化标题
        initTitle();
        //私有的ViewModel与View的契约事件回调逻辑
        registeredUIChangeLiveDataCallBack();
        mI_fragmentGroupLayoutId = getI_fragmentGroupLayoutId();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerRxBus();
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
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
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

    private void initTitle(){
        mBuilder = new BaseTitle.Builder(this,viewModel);
        viewModel.initToolbar();
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
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
        }
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
            startActivity(GeneralShowActivity.getNewIntent(this,sPath));
        }else {
            startActivity(GeneralShowActivity.getNewIntent(this,sPath,bundle));
        }
    }

    /**
     * =====================================================================
     **/

    @Override
    public void initParam(Bundle fragmentOfBundle, Intent activityIntent) {

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

    @Override
    public void initData() {

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
