package com.kjq.common.base.mvvm.base;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by goldze on 2017/6/15.
 */

public interface IBaseView {
    /**
     * 初始化界面传递参数
     */
    void initParam(Bundle fragmentOfBundle, Intent activityIntent);

    /**
     * 初始化数据 只执行一次
     */
     void initData();

    /**
     * 生命周期中每次都会执行
     */
     void refreshData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
