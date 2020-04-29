package com.kjq.common.ui.designs.refreshLayout.impl;

import android.annotation.SuppressLint;
import android.view.View;

import com.kjq.common.ui.designs.refreshLayout.api.RefreshHeader;
import com.kjq.common.ui.designs.refreshLayout.internal.InternalAbstract;


/**
 * 刷新头部包装
 * Created by SCWANG on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader/*, InvocationHandler*/ {

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

}
