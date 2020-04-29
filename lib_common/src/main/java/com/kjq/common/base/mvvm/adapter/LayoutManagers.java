package com.kjq.common.base.mvvm.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LayoutManagers {
    protected LayoutManagers() {
    }

    public static LayoutManagerFactory linear() {
        return new LayoutManagerFactory() {
            public RecyclerView.LayoutManager create(RecyclerView recyclerView) {
                return new LinearLayoutManager(recyclerView.getContext());
            }
        };
    }

    public static LayoutManagerFactory linearH(){
        return new LayoutManagerFactory() {
            @Override
            public RecyclerView.LayoutManager create(RecyclerView recyclerView) {
                return new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL,false);
            }
        };
    }

    public static LayoutManagerFactory linear(final int orientation, final boolean reverseLayout) {
        return new LayoutManagerFactory() {
            public RecyclerView.LayoutManager create(RecyclerView recyclerView) {
                return new LinearLayoutManager(recyclerView.getContext(), orientation, reverseLayout);
            }
        };
    }

    public static LayoutManagerFactory grid(final int spanCount) {
        return new LayoutManagerFactory() {
            public RecyclerView.LayoutManager create(RecyclerView recyclerView) {
                return new GridLayoutManager(recyclerView.getContext(), spanCount);
            }
        };
    }

    public static LayoutManagerFactory grid(final int spanCount, final int orientation, final boolean reverseLayout) {
        return new LayoutManagerFactory() {
            public RecyclerView.LayoutManager create(RecyclerView recyclerView) {
                return new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, reverseLayout);
            }
        };
    }

    public static LayoutManagerFactory staggeredGrid(final int spanCount, final int orientation) {
        return new LayoutManagerFactory() {
            public RecyclerView.LayoutManager create(RecyclerView recyclerView) {
                return new StaggeredGridLayoutManager(spanCount, orientation);
            }
        };
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    public interface LayoutManagerFactory {
        RecyclerView.LayoutManager create(RecyclerView var1);
    }
}