package com.kjq.common.ui.designs.dialog.view;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.kjq.common.ui.designs.dialog.model.BaseData;


public abstract class BaseViewHolder<Model extends BaseData> extends RecyclerView.ViewHolder {
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
    }

    protected abstract void notifyData(Model model);

    public View getItemView() {
        return mItemView;
    }
}