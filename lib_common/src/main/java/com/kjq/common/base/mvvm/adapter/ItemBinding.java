package com.kjq.common.base.mvvm.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import org.jetbrains.annotations.NotNull;

public final class ItemBinding<T> {
    public static final int VAR_NONE = 0;
    private static final int VAR_INVALID = -1;
    private static final int LAYOUT_NONE = 0;
    private final OnItemBind<T> onItemBind;
    private int variableId;
    private int layoutRes;
    private SparseArray<Object> extraBindings;

    public static <T> ItemBinding<T> of(int variableId, int layoutRes) {
        return (new ItemBinding<>((OnItemBind<T>)null)).set(variableId, layoutRes);
    }

    public static <T> ItemBinding<T> of(OnItemBind<T> onItemBind) {
        if (onItemBind == null) {
            throw new NullPointerException("onItemBind == null");
        } else {
            return new ItemBinding<>(onItemBind);
        }
    }

    private ItemBinding(OnItemBind<T> onItemBind) {
        this.onItemBind = onItemBind;
    }

    public final ItemBinding<T> set(int variableId, int layoutRes) {
        this.variableId = variableId;
        this.layoutRes = layoutRes;
        return this;
    }

    public final ItemBinding<T> variableId(int variableId) {
        this.variableId = variableId;
        return this;
    }

    public final ItemBinding<T> layoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }

    public final ItemBinding<T> bindExtra(int variableId, Object value) {
        if (this.extraBindings == null) {
            this.extraBindings = new SparseArray<>(1);
        }

        this.extraBindings.put(variableId, value);
        return this;
    }

    public final ItemBinding<T> clearExtras() {
        if (this.extraBindings != null) {
            this.extraBindings.clear();
        }

        return this;
    }

    public ItemBinding<T> removeExtra(int variableId) {
        if (this.extraBindings != null) {
            this.extraBindings.remove(variableId);
        }

        return this;
    }

    public final int variableId() {
        return this.variableId;
    }

    public final int layoutRes() {
        return this.layoutRes;
    }

    public final Object extraBinding(int variableId) {
        return this.extraBindings == null ? null : this.extraBindings.get(variableId);
    }

    public void onItemBind(int position, T item) {
        if (this.onItemBind != null) {
            this.variableId = -1;
            this.layoutRes = 0;
            this.onItemBind.onItemBind(this, position, item);
            if (this.variableId == -1) {
                throw new IllegalStateException("variableId not set in onItemBind()");
            }

            if (this.layoutRes == 0) {
                throw new IllegalStateException("layoutRes not set in onItemBind()");
            }
        }

    }

    public boolean bind(ViewDataBinding binding, T item) {
        if (this.variableId == 0) {
            return false;
        } else {
            boolean result = binding.setVariable(this.variableId, item);
            if (!result) {
                throwMissingVariable(binding, this.variableId, this.layoutRes);
            }

            if (this.extraBindings != null) {
                int i = 0;

                for(int size = this.extraBindings.size(); i < size; ++i) {
                    int variableId = this.extraBindings.keyAt(i);
                    Object value = this.extraBindings.valueAt(i);
                    if (variableId != 0) {
                        binding.setVariable(variableId, value);
                    }
                }
            }

            return true;
        }
    }
    private static void throwMissingVariable(@NotNull ViewDataBinding binding, int bindingVariable, int layoutRes) {
        Context context = binding.getRoot().getContext();
        Resources resources = context.getResources();
        String layoutName = resources.getResourceName(layoutRes);
        String bindingVariableName = DataBindingUtil.convertBrIdToString(bindingVariable);
        throw new IllegalStateException("Could not bind variable '" + bindingVariableName + "' in layout '" + layoutName + "'");
    }
}