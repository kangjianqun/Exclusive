package com.kjq.common.ui.designs.universalRecyclerAdapter.smoothScroll;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <p>滑动LinearLayoutManager</p>
 *
 * @author 康建群 948182974---->>>2018/9/15 17:44
 * @version 1.0.0
 */
public class SmoothScrollLayoutManager extends LinearLayoutManager {

    private final float SCROLL_SPEED = 450f;

    public SmoothScrollLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView,
                                       RecyclerView.State state, final int position) {

        LinearSmoothScroller smoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return SmoothScrollLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    // 返回：滑过1px时经历的时间(ms)。
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return getSpeedSlow(recyclerView.getContext(),SCROLL_SPEED) / displayMetrics.densityDpi;
                    }
                };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    //可以用来设置速度
    private static float getSpeedSlow(Context context, float x) {
        //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
        //0.3f是自己估摸的一个值，可以根据不同需求自己修改
        return context.getResources().getDisplayMetrics().density * 0.3f+(x);
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager  设置RecyclerView对应的manager
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }
}
