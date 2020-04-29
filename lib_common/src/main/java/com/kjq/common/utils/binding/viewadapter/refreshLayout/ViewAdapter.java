package com.kjq.common.utils.binding.viewadapter.refreshLayout;

import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.kjq.common.ui.designs.refreshLayout.SmartRefreshLayout;
import com.kjq.common.ui.designs.refreshLayout.api.RefreshLayout;
import com.kjq.common.ui.designs.refreshLayout.listener.OnLoadMoreListener;
import com.kjq.common.ui.designs.refreshLayout.listener.OnRefreshListener;
import com.kjq.common.utils.binding.command.BindingCommand;

/**
 * Created by kjq on 2017/6/18.
 */
public class ViewAdapter {
    //下拉刷新命令
    @BindingAdapter(value = {"onRefreshCommand"} , requireAll = false)
    public static void onRefreshCommand(final SmartRefreshLayout smartRefreshLayout, final BindingCommand onRefreshCommand) {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute(smartRefreshLayout.getRootView());
                }
            }
        });
    }

    @BindingAdapter({"onLoadMoreCommand"})
    public static void onLoadMoreCommand(final SmartRefreshLayout smartRefreshLayout, final BindingCommand onLoadMoreCommand) {
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (onLoadMoreCommand != null){
                    onLoadMoreCommand.execute(smartRefreshLayout.getRootView());
                }
            }
        });
    }
}
