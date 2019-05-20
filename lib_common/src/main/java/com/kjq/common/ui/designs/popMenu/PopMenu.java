package com.kjq.common.ui.designs.popMenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class PopMenu<Model extends PopMenuModel> {
    private Activity mActivity;

    private ArrayList<Model> mAL_model;

    private BaseAdapter mBaseAdapter;

    /**
     * 菜单选择监听.
     */
    private ISelectListener mListener;

    private RecyclerView mRecyclerView;

    /**
     * 弹出窗口.
     */
    private PopupWindow mPopupWindow;

    public PopMenu(Activity activity){
        mActivity = activity;
        mAL_model = new ArrayList<>();
        View view = onCreateView(activity);
        view.setFocusableInTouchMode(true);
        mBaseAdapter = onCreateAdapter(activity,mAL_model);
        mRecyclerView = findById(view);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mBaseAdapter.setISelectListener(new ISelectListener() {
            @Override
            public void selected(View view, PopMenuModel item, int position) {
                if (mListener == null) return;
                mListener.selected(view,item,position);
                mPopupWindow.dismiss();
            }
        });

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    /**
     * 菜单的界面视图.
     *
     * @param context
     * @return
     */
    protected abstract View onCreateView(Context context);

    protected abstract RecyclerView findById(View view);

    /**
     * 菜单列表中的适配器.
     *
     * @param itemList 表示所有菜单项.
     * @return
     */
    protected abstract BaseAdapter onCreateAdapter(Context context, ArrayList<Model> itemList);

    /**
     * 添加菜单项.
     *
     */
    private void addItemNotify(Model model) {
        mAL_model.add(model);
        mBaseAdapter.notifyDataSetChanged();
    }

    /**
     * 添加菜单项.
     *
     */
    public void addItem(Model model) {
        addItemNotify(model);
    }

    /**
     * 作为指定View的下拉控制显示.
     *
     * @param parent 所指定的View
     */
    public void showAsDropDown(View parent) {
        mPopupWindow.showAsDropDown(parent);
        backgroundAlpha(0.7f);
    }

    /**
     * 隐藏菜单.
     */
    public void dismiss() {
        mPopupWindow.dismiss();
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * 设置菜单选择监听.
     *
     * @param listener 监听器.
     */
    public void setOnItemSelectedListener(ISelectListener listener) {
        mListener = listener;
    }

    /**
     * 当前菜单是否正在显示.
     *
     * @return
     */
    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

}
