package com.kjq.common.ui.designs.popMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.kjq.common.databinding.DesignsItemMenuDefaultBinding;

import java.util.ArrayList;

public class DefaultAdapter<Model extends PopMenuModel> extends BaseAdapter<Model,DefaultAdapter.ViewHolder > {

    private int mI_layoutId;
    DefaultAdapter(Context context, ArrayList<Model> arrayList, int resId){
        mContext = context;
        mAL_model = arrayList;
        mI_layoutId = resId;
    }

    public class ViewHolder extends BaseViewHolder {
        DesignsItemMenuDefaultBinding mItemMenuDefaultBinding;
        public ViewHolder(View itemView) {
            super(itemView);
            mItemMenuDefaultBinding = DataBindingUtil.bind(itemView);
        }
        @Override
        protected void notifyData(PopMenuModel model) {
            mItemMenuDefaultBinding.setData(model);
            mItemMenuDefaultBinding.designsIMDefaultSign.setImageResource(model.getI_sign());
        }
    }

    @Override
    protected ViewHolder onCreateVHExtended(ViewGroup viewGroup, int viewType) {
        View sView = LayoutInflater.from(mContext).inflate(mI_layoutId,viewGroup,false);
        ViewHolder sViewHolder = new ViewHolder(sView);
        return sViewHolder;
    }

    @Override
    protected void onBindVHExtended(final DefaultAdapter.ViewHolder holder, final int position) {
        holder.notifyData(getItemModel(position));
        holder.mItemMenuDefaultBinding.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mISelectListener.selected(holder.getItemView(),getItemModel(position),position);
            }
        });
    }

}
