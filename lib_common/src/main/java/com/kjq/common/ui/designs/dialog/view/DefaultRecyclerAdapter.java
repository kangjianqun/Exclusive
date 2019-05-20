package com.kjq.common.ui.designs.dialog.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;


import com.kjq.common.databinding.CommonItemDialogDefaultBinding;
import com.kjq.common.ui.designs.dialog.model.BaseData;
import com.kjq.common.ui.designs.dialog.model.RecyclerData;

import java.util.ArrayList;

public class DefaultRecyclerAdapter<Model extends RecyclerData> extends BaseAdapter<Model,DefaultRecyclerAdapter.ViewHolder> {

    private int mI_itemLayoutResId;

    public DefaultRecyclerAdapter(Context context, ArrayList<Model> model, int i_itemLayoutResId){
        mContext = context;
        mAL_model = model;
        mI_itemLayoutResId = i_itemLayoutResId;
    }

    public class ViewHolder extends BaseViewHolder {
        CommonItemDialogDefaultBinding mBinding;
        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        protected void notifyData(BaseData baseData) {
            mBinding.setData((RecyclerData) baseData);
        }
    }

    @Override
    protected ViewHolder onCreateVHExtended(ViewGroup viewGroup, int viewType) {
        View sView = LayoutInflater.from(mContext).inflate(mI_itemLayoutResId,viewGroup,false);
        ViewHolder sViewHolder = new ViewHolder(sView);
        return sViewHolder;
    }

    @Override
    protected void onBindVHExtended(final DefaultRecyclerAdapter.ViewHolder holder, final int position) {
        holder.notifyData(getItemModel(position));
        holder.mBinding.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewEven.onItemClick(holder.getItemView(),getItemModel(position),position);
            }
        });

    }


}
