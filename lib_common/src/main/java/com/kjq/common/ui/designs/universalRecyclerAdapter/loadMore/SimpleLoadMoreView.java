package com.kjq.common.ui.designs.universalRecyclerAdapter.loadMore;


import com.kjq.common.R;

/**
 *
 * Created by BlingBling on 2016/10/11.
 */

public final class SimpleLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.common_quick_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.common_load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.common_load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.common_load_more_load_end_view;
    }
}
