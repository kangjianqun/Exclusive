package com.kjq.common.ui.designs.dialog;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.kjq.common.ui.designs.dialog.litener.ViewEven;
import com.kjq.common.ui.designs.dialog.model.RecyclerData;
import com.kjq.common.ui.designs.dialog.view.BaseAdapter;
import com.kjq.common.ui.designs.dialog.view.DefaultRecyclerAdapter;
import com.kjq.common.ui.designs.universalRecyclerAdapter.itemDecoration.MyItemDecoration;

import java.util.ArrayList;

public abstract class DialogRV extends DialogBase {
    private ArrayList<RecyclerData> mAL_models;
    private RecyclerView mRecyclerView;
    public BaseAdapter mBaseAdapter;

    protected DialogRV(Context context) {
        super(context);
        mAL_models = getAL_models();
        mRecyclerView = getView(getRVResId());
        mBaseAdapter = new DefaultRecyclerAdapter<>(mContext,mAL_models, getItemResId());
    }

    @Override
    protected void setDialogEvent() {
        super.setDialogEvent();
    }

    @Override
    protected void setViewInfo(View view) {
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyItemDecoration(mContext,MyItemDecoration.VERTICAL_LIST));
    }

    protected abstract ArrayList<RecyclerData> getAL_models();

    protected abstract int getItemResId();
    protected abstract int getRVResId();

    public void addItem(RecyclerData data){
        if (mAL_models != null){
            mAL_models.add(data);
        }
    }

    public void setAL_models(ArrayList<RecyclerData> AL_models) {
        mAL_models = AL_models;
    }

    @Override
    public void setViewEven(ViewEven viewEven) {
        super.setViewEven(viewEven);
        mBaseAdapter.setViewEven(viewEven);
    }
}
