package com.kjq.common.base.mvvm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.kjq.common.R;
import com.kjq.common.ui.designs.refreshLayout.SmartRefreshLayout;
import com.kjq.common.ui.designs.refreshLayout.api.DefaultRefreshHeaderCreator;
import com.kjq.common.ui.designs.refreshLayout.api.DefaultRefreshInitializer;
import com.kjq.common.ui.designs.refreshLayout.api.RefreshHeader;
import com.kjq.common.ui.designs.refreshLayout.api.RefreshLayout;
import com.kjq.common.ui.designs.refreshLayout.header.ClassicsHeader;
import com.kjq.common.utils.DateAndTimeUtil;
import com.kjq.common.utils.Utils;
import com.kjq.common.utils.performance.AppManager;

import org.litepal.LitePal;


/**
 * Created by kjq on 2017/6/15.
 */

public class BaseApplication extends MultiDexApplication {

    static {
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置（优先级最低）
                layout.setEnableAutoLoadMore(true);
                layout.setEnableOverScrollDrag(false);
                layout.setEnableOverScrollBounce(true);
                layout.setEnableLoadMoreWhenContentNotFull(true);
                layout.setEnableScrollContentWhenRefreshed(true);
            }
        });
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
                layout.setPrimaryColorsId(R.color.common_colorPrimary, android.R.color.white);

                return new ClassicsHeader(context).setTimeFormat(new DateAndTimeUtil("更新于 %s"));
            }
        });
    }

    private static BaseApplication sInstance;

    /**
     * 全局临时数据存储
     */
    private SparseArray mSparseArray = new SparseArray();

    public void clearGlobalValue() {
        mSparseArray.clear();
    }

    /**
     * 保存临时数据
     * @param key
     * @param t
     * @param <T>
     */
    public <T> void setGlobalValue(int key,T t) {
        int sI_index = mSparseArray.indexOfKey(key);
        if (sI_index < 0){
            mSparseArray.append(key,t);
        }else {
            mSparseArray.setValueAt(sI_index,t);
        }
    }

    /**
     * 取出临时数据
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getGlobalValue(int key){
        return (T) mSparseArray.get(key);
    }

    /**
     * 取出临时数据
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getGlobalValue(int key,T defaultV){
        T sT = (T) mSparseArray.get(key);
        if (sT == null){
            return defaultV;
        }else {
            return sT;
        }
    }

    @Override
    public void onCreate() {
        setApplication(this);
        LitePal.initialize(this);
        super.onCreate();
    }

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     *
     * @param application app
     */
    public static synchronized void setApplication(@NonNull BaseApplication application) {
        sInstance = application;

        //初始化工具类
        Utils.init(application);
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppManager.getAppManager().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                AppManager.getAppManager().removeActivity(activity);
            }
        });
    }

    /**
     * 获得当前app运行的Application
     */
    public static BaseApplication getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("please inherit BaseApplication or call setApplication.");
        }
        return sInstance;
    }

}
