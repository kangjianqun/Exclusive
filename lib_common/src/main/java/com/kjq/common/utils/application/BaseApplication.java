package com.kjq.common.utils.application;

import android.util.SparseArray;


import com.kjq.common.utils.ScreenSizeUtils;
import com.kjq.common.utils.Utils;
import com.kjq.common.utils.data.Constant;

import java.util.List;

import androidx.multidex.MultiDexApplication;

/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 *
 * @author 2016/12/2 17:02
 * @version V1.0.0
 * @name BaseApplication
 */
public class BaseApplication extends MultiDexApplication {
    public static final String ROOT_PACKAGE = "com.kang.module";

    private static BaseApplication sInstance;

    private List<IApplicationDelegate> mAppDelegateList;


    public static BaseApplication getIns() {
        return sInstance;
    }

    private SparseArray<Object> mObjectSparseArray = new SparseArray<>();

    public void setPutArray(int key,Object o){
        mObjectSparseArray.put(key,o);
    }

    public Object getArrayValue(int key){
        return mObjectSparseArray.get(key);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
//        Logger.init("pattern").logLevel(LogLevel.FULL);
        Utils.init(this);
        Constant.AppInfo.H = ScreenSizeUtils.INSTANCE.getDisplayH();
        mAppDelegateList = ClassUtils.getObjectsWithInterface(this, IApplicationDelegate.class, ROOT_PACKAGE);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onCreate();
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTerminate();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTrimMemory(level);
        }
    }
}
