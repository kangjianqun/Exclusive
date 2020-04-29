package com.kjq.common.ui.designs.popMenu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.kjq.common.base.mvvm.adapter.ItemBinding;
import com.kjq.common.base.mvvm.base.BaseViewModel;

public class FreedomPopupVM<Item extends FreedomItemVM> extends BaseViewModel {

    public ItemBinding<Item> mItemBinding;

    public ObservableArrayList<Item> mObservableList = new ObservableArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    FreedomPopupVM(@NonNull Application application, ItemBinding<Item> itemBinding) {
        super(application);
        mItemBinding = itemBinding;
    }

    public void addItem (Item item){
        mObservableList.add(item);
    }

    public void addItem(int position,Item model,boolean isCover){
        if (isCover){
            int sI_index =  mObservableList.indexOf(model);
            if (sI_index < 0){
                mObservableList.add(position,model);
            }else {
                mObservableList.set(sI_index,model);
            }
        }else {
            mObservableList.add(position,model);
        }
    }

    public void removeItem(Item item){
        mObservableList.remove(item);
    }

    public void removeAll(){
        mObservableList.clear();
    }

    public void clickResponse(int type){
        if (mOnItemClickListener != null){
            mOnItemClickListener.itemClick(type);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void itemClick(int type);
    }
}
