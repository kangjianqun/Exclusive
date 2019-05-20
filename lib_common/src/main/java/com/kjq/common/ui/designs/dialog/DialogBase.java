package com.kjq.common.ui.designs.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import com.kjq.common.ui.designs.dialog.litener.ViewEven;


public abstract class DialogBase implements IDialog{
    protected Context mContext;
    protected LayoutInflater mInflater;
    private int mI_layoutResId;
    private AlertDialog.Builder mBuilder;
    private String mS_titleTxt;
    private String mS_txt;
    private ViewEven mViewEven;

    private View mView;
    protected boolean mB_isBack = true; // 是否允许返回
    private AlertDialog mAlertDialog;
    private SparseArray<View> mSArray_views = new SparseArray<>();

    DialogBase(Context context){
        create(context);
    }

    DialogBase(Context context, @StyleRes int i_styleRes){
        create(context,i_styleRes);
    }

    @CallSuper
    protected void create(Context context){
        create(context,0);
    }

    private void create(Context context, @StyleRes int i_styleRes){
        mContext = context;
        mBuilder = i_styleRes == 0 ? new AlertDialog.Builder(mContext): new AlertDialog.Builder(mContext,i_styleRes);
        mInflater = LayoutInflater.from(mContext);
        mI_layoutResId = getLayoutResId();
        mView = mInflater.inflate(mI_layoutResId,null);
    }

    public View getView() {
        return mView;
    }

    /**
     * 显示视图，非空则直接显示
     */
    @CallSuper
    public void show(){
        if (mAlertDialog == null){
            setViewInfo(mView);
            mBuilder.setView(mView);
            setDialogEvent();
        }
        mAlertDialog.show();
    }

    /**
     * 显示视图，非空则直接显示
     */
    @CallSuper
    public void show(ViewEven even){
        setViewEven(even);

        if (mAlertDialog == null){
            setViewInfo(mView);
            mBuilder.setView(mView);
            setDialogEvent();
        }
        mAlertDialog.show();
    }

    @CallSuper
    protected void setDialogEvent(){
        mAlertDialog = mBuilder.create();
        if (mS_titleTxt != null && mS_titleTxt.length() > 0){
            mAlertDialog.setTitle(mS_titleTxt);
        }
        mAlertDialog.setCancelable(mB_isBack);
        mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (getViewEven() == null) {
                    return;
                }
                getViewEven().onBack(mView);
            }
        });
    }

    /**
     * View其他设置
     * @param view view
     */
    protected abstract void setViewInfo(View view);

    protected abstract int getLayoutResId();

    /**
     * 关闭dialog
     */
    public void dismissDialog(){
        if (mAlertDialog != null){
            mAlertDialog.dismiss();
        }
    }

    /**
     * 得到View
     *
     * @param id resID
     * @param <T> 泛型，继承view
     * @return 泛型
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        T t = (T) mSArray_views.get(id);
        if (t == null) {
            t = mView.findViewById(id);
            mSArray_views.put(id, t);
        }
        return t;
    }

    public ViewEven getViewEven() {
        return mViewEven;
    }

    @CallSuper
    public void setViewEven(ViewEven viewEven) {
        mViewEven = viewEven;
    }

    @Override
    public void setTxt(String msg) {
        mS_txt = msg;
    }

    @Override
    public void setTitleTxt(String msg) {
        mS_titleTxt = msg;
    }
}
