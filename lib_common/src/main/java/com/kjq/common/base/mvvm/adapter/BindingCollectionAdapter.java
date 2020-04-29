package com.kjq.common.base.mvvm.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;

import java.util.List;

public interface BindingCollectionAdapter<T> {
    void setItemBinding(ItemBinding<T> var1);

    ItemBinding<T> getItemBinding();

    void setItems(@Nullable ObservableArrayList<T> var1);

    T getAdapterItem(int var1);

    ViewDataBinding onCreateBinding(LayoutInflater var1, int var2, ViewGroup var3);

    void onBindBinding(ViewDataBinding var1, int var2, int var3, int var4, T var5);
}

