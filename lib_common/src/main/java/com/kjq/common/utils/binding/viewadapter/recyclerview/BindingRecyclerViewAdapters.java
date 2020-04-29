package com.kjq.common.utils.binding.viewadapter.recyclerview;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.kjq.common.base.mvvm.adapter.BindingRecyclerViewAdapter;
import com.kjq.common.base.mvvm.adapter.ItemBinding;
import com.kjq.common.base.mvvm.adapter.LayoutManagers;

import java.util.List;

public class BindingRecyclerViewAdapters {
    @BindingAdapter(
            value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder"}, requireAll = false)
    public static <T> void setAdapter
            (RecyclerView recyclerView, ItemBinding<T> itemBinding, ObservableArrayList<T> items,
             BindingRecyclerViewAdapter<T> adapter, BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
             BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        } else {
            BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter)recyclerView.getAdapter();
            if (adapter == null) {
                if (oldAdapter == null) {
                    adapter = new BindingRecyclerViewAdapter<>();
                } else {
                    adapter = oldAdapter;
                }
            }
            adapter.setItemBinding(itemBinding);
            adapter.setItems(items);
            adapter.setItemIds(itemIds);
            adapter.setViewHolderFactory(viewHolderFactory);
            if (oldAdapter != adapter) {
                recyclerView.setAdapter(adapter);
            }

        }
    }

    @BindingAdapter({"layoutManager"})
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }
}
