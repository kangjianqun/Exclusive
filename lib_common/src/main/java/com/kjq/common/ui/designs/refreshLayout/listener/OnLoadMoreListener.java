package com.kjq.common.ui.designs.refreshLayout.listener;


import androidx.annotation.NonNull;

import com.kjq.common.ui.designs.refreshLayout.api.RefreshLayout;

/**
 * 加载更多监听器
 * Created by SCWANG on 2017/5/26.
 */

public interface OnLoadMoreListener {
    void onLoadMore(@NonNull RefreshLayout refreshLayout);
}
