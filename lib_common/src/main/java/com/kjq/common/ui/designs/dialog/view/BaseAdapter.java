package com.kjq.common.ui.designs.dialog.view;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.kjq.common.ui.designs.dialog.litener.ViewEven;
import com.kjq.common.ui.designs.dialog.model.BaseData;

import java.util.ArrayList;

public abstract class BaseAdapter<Model extends BaseData,VH extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    protected ArrayList<Model> mAL_model;
    protected Context mContext;
    protected ViewEven mViewEven;


    public void setViewEven(ViewEven viewEven) {
        mViewEven = viewEven;
    }

    @Override
    public int getItemCount() {
        return mAL_model==null ? 0 : mAL_model.size();
    }

    protected Model getItemModel(int position){
        return mAL_model.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateVHExtended(parent,viewType);
    }

    protected abstract VH onCreateVHExtended(ViewGroup viewGroup, int viewType);
    protected abstract void onBindVHExtended(VH holder, int position);
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        onBindVHExtended((VH) holder,position);
    }
}
