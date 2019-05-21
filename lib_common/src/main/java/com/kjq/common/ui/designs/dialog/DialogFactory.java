package com.kjq.common.ui.designs.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;
import androidx.databinding.DataBindingUtil;


import com.kjq.common.R;
import com.kjq.common.databinding.CommonDialogTimeSelectDefaultBinding;
import com.kjq.common.ui.designs.dialog.litener.ViewEven;
import com.kjq.common.ui.designs.dialog.model.RecyclerData;

import java.util.ArrayList;

public class DialogFactory {

    /**
     * 得到默认视图的RV
     * @param context content
     * @param recyclerData 数据
     * @param viewEven 事件
     * @param <Dialog> 继承DialogBase的基类
     * @return 继承DialogBase的基类
     */
    @SuppressLint("ResourceType")
    public static <Dialog extends DialogBase> Dialog getDialogRV(Context context, ArrayList<RecyclerData> recyclerData, ViewEven viewEven){
        return getDialogRV(context,recyclerData, R.layout.common_dialog_recycler_view_default,R.id.designs_dCView_content, R.layout.common_item_dialog_default,viewEven);
    }

    /**
     * 自定义视图的RV
     * @param context content
     * @param recyclerData 数据
     * @param layoutId 显示的布局id
     * @param rVResId RV控件id
     * @param itemResId RV的item布局id
     * @param viewEven 事件
     * @param <Dialog> 继承DialogBase的基类
     * @return 继承DialogBase的基类
     */
    public static <Dialog extends DialogBase> Dialog getDialogRV
    (Context context, final ArrayList<RecyclerData> recyclerData, @LayoutRes final int layoutId,
     @LayoutRes final int rVResId, @LayoutRes final int itemResId, ViewEven viewEven){
        DialogRV sDialogRV = new DialogRV(context) {
            @Override
            protected ArrayList<RecyclerData> getAL_models() {
                return recyclerData;
            }

            @Override
            protected int getLayoutResId() {
                return layoutId;
            }

            @Override
            protected int getRVResId() {
                return rVResId;
            }

            @Override
            protected int getItemResId() {
                return itemResId;
            }
        };
        sDialogRV.setViewEven(viewEven);
        sDialogRV.mBaseAdapter.setViewEven(viewEven);
        return (Dialog)sDialogRV;
    }

    /**
     * 自定义视图 背景透明
     * @param context context
     * @param layoutId 布局Id
     * @param viewEven 事件绑定
     * @param <Dialog> 继承DialogBase的基类
     * @return 继承DialogBase的基类
     */
    public static <Dialog extends DialogBase> Dialog getDialogCViewTransparent(Context context, @LayoutRes int layoutId, ViewEven viewEven){
        return getDialogCView(context,R.style.CommonDialog,layoutId,viewEven);
    }

    /**
     * 自定义视图
     * @param context context
     * @param layoutId 布局Id
     * @param viewEven 事件绑定
     * @param <Dialog> 继承DialogBase的基类
     * @return 继承DialogBase的基类
     */
    public static <Dialog extends DialogBase> Dialog getDialogCView(Context context, @LayoutRes final int layoutId, final ViewEven viewEven){
        DialogCView sDialogCView ;
        sDialogCView = new DialogCView(context) {
            @Override
            public void setEven(View view) {
                viewEven.setViewEven(view);
            }
            @Override
            protected int getLayoutResId() {
                return layoutId;
            }
        };
        return (Dialog) sDialogCView;
    }

    /**
     * 自定义视图
     * @param context context
     * @param styleId styleId
     * @param layoutId 布局Id
     * @param viewEven 事件绑定
     * @param <Dialog> 继承DialogBase的基类
     * @return 继承DialogBase的基类
     */
    public static <Dialog extends DialogBase>Dialog getDialogCView
            (Context context, @StyleRes int styleId, @LayoutRes final int layoutId, final ViewEven viewEven){
        DialogCView sDialogCView ;
            sDialogCView = new DialogCView(context,styleId) {
                @Override
                public void setEven(View view) {
                    viewEven.setViewEven(view);
                }
                @Override
                protected int getLayoutResId() {
                    return layoutId;
                }
            };
        return (Dialog) sDialogCView;
    }

    public static <Dialog extends DialogBase>Dialog getValueDialog
            (Context context, String title, String hint, boolean isBack, ViewEven viewEven){
        DialogCView sDialogCView = new DialogCView(context,title,hint) {

            @Override
            public void setEven(View view) {

            }

            @Override
            protected int getLayoutResId() {
                return R.layout.common_dialog_value_default;
            }
        };
        sDialogCView.mB_isBack = isBack;
        if (viewEven != null){
            sDialogCView.setViewEven(viewEven);
        }
        sDialogCView.setValueDialog();
        return (Dialog) sDialogCView;
    }

    public static <Dialog extends DialogBase>Dialog getReconfirmDialog
            (Context context, String title, String hint, boolean isBack, ViewEven viewEven){
        DialogCView sDialogCView = new DialogCView(context,title,hint) {

            @Override
            public void setEven(View view) {
            }
            @Override
            protected int getLayoutResId() {
                return R.layout.common_dialog_ok_no_default;
            }
        };
        sDialogCView.mB_isBack = isBack;
        if (viewEven != null){
            sDialogCView.setViewEven(viewEven);
        }
        sDialogCView.setReconfirmDialog();
        return (Dialog) sDialogCView;
    }

    public static <Dialog extends DialogBase>Dialog getReconfirmDialogNotOk
            (Context context, String title, String hint, boolean isBack, ViewEven viewEven){
        DialogCView sDialogCView = new DialogCView(context,title,hint) {

            @Override
            public void setEven(View view) {
            }
            @Override
            protected int getLayoutResId() {
                return R.layout.common_dialog_ok_no_default;
            }
        };
        sDialogCView.mB_isBack = isBack;
        sDialogCView.mB_isDisplayOk = false;
        if (viewEven != null){
            sDialogCView.setViewEven(viewEven);
        }
        sDialogCView.setReconfirmDialog();
        return (Dialog) sDialogCView;
    }

    public static <Dialog extends DialogBase>Dialog getTimeHour(Context context){
        return getTimeHour(context,null);
    }

    public static <Dialog extends DialogBase>Dialog getTimeSelectDialog(Context context){
        return getTimeSelectDialog(context,null,null);
    }

    public static <Dialog extends DialogBase>Dialog getTimeHour(Context context, String hour){
        DialogCView sDialogCView = new DialogCView(context) {
            @Override
            public void setEven(View view) {
                CommonDialogTimeSelectDefaultBinding sBinding = DataBindingUtil.bind(view);
                sBinding.dTSDefaultMinute.setVisibility(View.GONE);
                sBinding.dTSDefaultMinuteHint.setVisibility(View.GONE);
            }
            @Override
            protected int getLayoutResId() {
                return R.layout.common_dialog_time_select_default;
            }
        };
        sDialogCView.setTimeSelectDialog(hour,null);
        return (Dialog) sDialogCView;
    }

    public static <Dialog extends DialogBase>Dialog getTimeSelectDialog(Context context, String hour, String minute){
        DialogCView sDialogCView = new DialogCView(context) {
            @Override
            public void setEven(View view) {

            }
            @Override
            protected int getLayoutResId() {
                return R.layout.common_dialog_time_select_default;
            }
        };
        sDialogCView.setTimeSelectDialog(hour,minute);
        return (Dialog) sDialogCView;
    }

    public static <Dialog extends DialogBase>Dialog getTwoTimeSelectDialog(Context context){
        return getTwoTimeSelectDialog(context,null,null,null,null);
    }

    public static <Dialog extends DialogBase>Dialog getTwoTimeSelectDialog(Context context, String hour, String minute,String twoHour,String twoMinute){
        DialogCView sDialogCView = new DialogCView(context) {
            @Override
            public void setEven(View view) {

            }
            @Override
            protected int getLayoutResId() {
                return R.layout.common_dialog_time_two_select_default;
            }
        };
        sDialogCView.setTimeSelectDialog(hour,minute,twoHour,twoMinute);
        return (Dialog) sDialogCView;
    }
}
