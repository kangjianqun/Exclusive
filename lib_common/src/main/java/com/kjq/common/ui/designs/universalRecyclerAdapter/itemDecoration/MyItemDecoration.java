package com.kjq.common.ui.designs.universalRecyclerAdapter.itemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kjq.common.R;


/**
 *
 * Created by Administrator on 2018/1/23 0023.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Drawable mDivider;
    private int mOrientation;
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private int mI_paddingT;
    private int mI_paddingD;
    private int mI_paddingL;
    private int mI_paddingR;

    public MyItemDecoration(Context context,int orientation){
        this.mContext = context;
        this.mDivider = this.mContext.getResources().getDrawable(R.drawable.common_adapter_recycler_view_divider);
        setOrientation(orientation);
    }

    public MyItemDecoration(Context context,int orientation,int resId) {
        this.mContext = context;
        this.mDivider = this.mContext.getResources().getDrawable(resId);
        setOrientation(orientation);
    }

    public MyItemDecoration(Context context,int orientation,int paddingT,int paddingD,int paddingL,int paddingR){
        this.mContext = context;
        this.mDivider = this.mContext.getResources().getDrawable(R.drawable.common_adapter_recycler_view_divider);
        mI_paddingT = paddingT;
        mI_paddingD = paddingD;
        mI_paddingL = paddingL;
        mI_paddingR = paddingR;
        setOrientation(orientation);
    }

    //设置屏幕的方向
    public void setOrientation(int orientation){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
            mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST){
            drawVerticalLine(c, parent, state);
        }else {
            drawHorizontalLine(c, parent, state);
        }
    }

    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    private void drawHorizontalLine(Canvas c, RecyclerView parent, RecyclerView.State state){
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i);

            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left+mI_paddingL, top-mI_paddingT, right-mI_paddingR, bottom-mI_paddingD);
            mDivider.draw(c);
            //Log.d("wnw", left + " " + top + " "+right+"   "+bottom+" "+i);
        }
    }

    //画竖线
    private void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state){
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i);

            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left+mI_paddingL, top-mI_paddingT, right-mI_paddingR, bottom-mI_paddingD);
            mDivider.draw(c);
        }
    }

    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == HORIZONTAL_LIST){
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }else {
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}

