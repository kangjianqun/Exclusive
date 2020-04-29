package com.kjq.common.ui.designs.refreshLayout.listener;

import androidx.annotation.NonNull;

import com.kjq.common.ui.designs.refreshLayout.api.RefreshLayout;

/**
 * 刷新监听器
 * Created by SCWANG on 2017/5/26.
 */

public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
