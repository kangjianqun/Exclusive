package com.kjq.common.base.mvvm.base;


import android.view.View;

import androidx.annotation.NonNull;

import com.kjq.common.utils.binding.command.BindingAction;
import com.kjq.common.utils.binding.command.BindingCommand;

/**
 * BaseItemViewModel
 * Created by kjq on 2018/10/3.
 */

public class BaseItemViewModel<VM extends BaseViewModel> {
    public VM viewModel;

    public BaseItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }

    public BindingCommand baseClick = new BindingCommand(new BindingAction() {
        @Override
        public void call(View view) {
            onBaseClick(view);
        }
    });

    public BindingCommand baseLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call(View view) {
            onBaseLongClick(view);
        }
    });

    protected void onBaseClick(View view){

    }

    protected void onBaseLongClick(View view){

    }

    /**
     * RecyclerView 防止position错乱
     * @return 默认返回 {@link Object#hashCode()}
     */
    public long getItemIds() {
        return this.hashCode();
    }
}
