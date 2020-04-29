package com.kjq.common.base.mvvm.adapter;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;


public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BindingCollectionAdapter<T> {
    private static final Object DATA_INVALIDATION = new Object();
    private ItemBinding<T> itemBinding;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    private ObservableArrayList<T> items;
    private LayoutInflater inflater;
    private ItemIds<? super T> itemIds;
    private ViewHolderFactory viewHolderFactory;
    @Nullable
    private RecyclerView recyclerView;

    public BindingRecyclerViewAdapter() {
    }

    public void setItemBinding(ItemBinding<T> itemBinding) {
        this.itemBinding = itemBinding;
    }

    public ItemBinding<T> getItemBinding() {
        return this.itemBinding;
    }

    public void setItems(@Nullable ObservableArrayList<T> items) {
        if (this.items != items) {
            if (this.recyclerView != null) {
                if (this.items != null) {
                    this.items.removeOnListChangedCallback(this.callback);
                }

                if (items != null) {
                    items.addOnListChangedCallback(this.callback);
                }
            }

            this.items = items;
            this.notifyDataSetChanged();
        }
    }

    public T getAdapterItem(int position) {
        return this.items.get(position);
    }

    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutId, viewGroup, false);
    }

    public void onBindBinding(ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
        if (this.itemBinding.bind(binding, item)) {
            binding.executePendingBindings();
        }

    }

    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        if (this.recyclerView == null && this.items != null) {
            this.items.addOnListChangedCallback(this.callback);
        }

        this.recyclerView = recyclerView;
    }

    public void onDetachedFromRecyclerView(@NotNull RecyclerView recyclerView) {
        if (this.recyclerView != null && this.items != null) {
            this.items.removeOnListChangedCallback(this.callback);
        }

        this.recyclerView = null;
    }

    @NotNull
    public final RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int layoutId) {
        if (this.inflater == null) {
            this.inflater = LayoutInflater.from(viewGroup.getContext());
        }
        ViewDataBinding binding = this.onCreateBinding(this.inflater, layoutId, viewGroup);
        final RecyclerView.ViewHolder holder = this.onCreateViewHolder(binding);
        binding.addOnRebindCallback(new OnRebindCallback() {
            public boolean onPreBind(ViewDataBinding binding) {
                return BindingRecyclerViewAdapter.this.recyclerView != null && BindingRecyclerViewAdapter.this.recyclerView.isComputingLayout();
            }

            public void onCanceled(ViewDataBinding binding) {
                if (BindingRecyclerViewAdapter.this.recyclerView != null && !BindingRecyclerViewAdapter.this.recyclerView.isComputingLayout()) {
                    int position = holder.getAdapterPosition();
                    if (position != -1) {
                        BindingRecyclerViewAdapter.this.notifyItemChanged(position, BindingRecyclerViewAdapter.DATA_INVALIDATION);
                    }
                }
            }
        });
        return holder;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewDataBinding binding) {
        return (this.viewHolderFactory != null ?
                this.viewHolderFactory.createViewHolder(binding) : new BindingViewHolder(binding));
    }

    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        T item = this.items.get(position);
        ViewDataBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
        this.onBindBinding(binding, this.itemBinding.variableId(), this.itemBinding.layoutRes(), position, item);
    }

    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position, @NotNull List<Object> payloads) {
        if (this.isForDataBinding(payloads)) {
            ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
            binding.executePendingBindings();
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }

    }

    private boolean isForDataBinding(List<Object> payloads) {
        if (payloads != null && payloads.size() != 0) {
            for(int i = 0; i < payloads.size(); ++i) {
                Object obj = payloads.get(i);
                if (obj != DATA_INVALIDATION) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public int getItemViewType(int position) {
        this.itemBinding.onItemBind(position, this.items.get(position));
        return this.itemBinding.layoutRes();
    }

    public void setItemIds(@Nullable ItemIds<? super T> itemIds) {
        if (this.itemIds != itemIds) {
            this.itemIds = itemIds;
            this.setHasStableIds(itemIds != null);
        }
    }

    public void setViewHolderFactory(@Nullable ViewHolderFactory factory) {
        this.viewHolderFactory = factory;
    }

    public int getItemCount() {
        return this.items == null ? 0 : this.items.size();
    }

    public long getItemId(int position) {
        return this.itemIds == null ? (long)position : this.itemIds.getItemId(position, this.items.get(position));
    }

    public interface ViewHolderFactory {
        RecyclerView.ViewHolder createViewHolder(ViewDataBinding var1);
    }

    public interface ItemIds<T> {
        long getItemId(int position, T data);
    }

    public static <T> ItemIds<T> getDefaultItemIds(){
        return new ItemIds<T>() {
            @Override
            public long getItemId(int position, T data) {
                return position;
            }
        };
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<T>> {
        final WeakReference<BindingRecyclerViewAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingRecyclerViewAdapter<T> adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        public void onChanged(ObservableArrayList<T> sender) {
            BindingRecyclerViewAdapter<T> adapter = this.adapterRef.get();
            if (adapter != null) {
                ensureChangeOnMainThread();
                adapter.notifyDataSetChanged();
            }
        }

        public void onItemRangeChanged(ObservableArrayList<T> sender, int positionStart, int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = this.adapterRef.get();
            if (adapter != null) {
                ensureChangeOnMainThread();
                adapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        public void onItemRangeInserted(ObservableArrayList<T> sender, int positionStart, int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = this.adapterRef.get();
            if (adapter != null) {
                ensureChangeOnMainThread();
                adapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        }

        public void onItemRangeMoved(ObservableArrayList<T> sender, int fromPosition, int toPosition, int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = this.adapterRef.get();
            if (adapter != null) {
                ensureChangeOnMainThread();
                for(int i = 0; i < itemCount; ++i) {
                    adapter.notifyItemMoved(fromPosition + i, toPosition + i);
                }

            }
        }

        public void onItemRangeRemoved(ObservableArrayList<T> sender, int positionStart, int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = this.adapterRef.get();
            if (adapter != null) {
                ensureChangeOnMainThread();
                adapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }
    }

    /**
     * 如果出错可能是因为layout没有设置成为binding视图
     */
    private static class BindingViewHolder extends RecyclerView.ViewHolder {
        public BindingViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
        }
    }
    private static void ensureChangeOnMainThread() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("You must only modify the ObservableList on the main thread.");
        }
    }
}


