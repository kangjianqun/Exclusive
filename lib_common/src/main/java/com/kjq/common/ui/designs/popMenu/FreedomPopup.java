package com.kjq.common.ui.designs.popMenu;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.IntDef;
import androidx.databinding.DataBindingUtil;

import com.kjq.common.BR;
import com.kjq.common.R;
import com.kjq.common.base.mvvm.adapter.ItemBinding;
import com.kjq.common.databinding.CommonPopupRecyclerViewBinding;
import com.kjq.common.utils.performance.AppManager;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FreedomPopup extends PopupWindow {
    private CommonPopupRecyclerViewBinding mBinding;
    private Activity mActivity;
    private boolean mb_backIsAlpha = true;
    private DismissListener mDismissListener;

    public FreedomPopup(Activity activity) {
        super(activity);
        mActivity = activity;
        init();
    }

    public FreedomPopup(Activity activity, AttributeSet attrs) {
        super(activity, attrs);
        mActivity = activity;
        init();
    }

    public FreedomPopup(Activity activity, AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        mActivity = activity;
        init();
    }

    public FreedomPopup(Activity activity, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(activity, attrs, defStyleAttr, defStyleRes);
        mActivity = activity;
        init();
    }


    @NotNull
    @Contract("_ -> new")
    public static <I extends FreedomItemVM> FreedomPopupVM getFreedomPopupVM(ItemBinding<I> itemBinding){
        return new FreedomPopupVM<>(AppManager.getActivityStack().lastElement().getApplication(), itemBinding);
    }

    @NotNull
    @Contract(" -> new")
    public static  FreedomPopupVM getFreedomPopupVM(){
        return new FreedomPopupVM(AppManager.getActivityStack().lastElement().getApplication(), ItemBinding.of(BR.viewModel, R.layout.common_popup_recycler_view_item));
    }

    public static FreedomPopup getRVPopup(Activity activity,FreedomPopupVM freedomPopupVM){
        FreedomPopup sFreedomPopup = new FreedomPopup(activity);
        View sView = View.inflate(activity,R.layout.common_popup_recycler_view,null);
        CommonPopupRecyclerViewBinding sBinding = DataBindingUtil.bind(sView);
        if (sBinding != null) {
            sBinding.setViewModel(freedomPopupVM);
            sFreedomPopup.setBinding((CommonPopupRecyclerViewBinding) DataBindingUtil.bind(sView));
        }
        sFreedomPopup.setContentView(sView);
        return sFreedomPopup;
    }

    private void init(){
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mb_backIsAlpha){
                    backgroundAlpha(1);
                }
                if (mDismissListener != null){
                    mDismissListener.dismiss();
                }
            }
        });
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public CommonPopupRecyclerViewBinding getBinding() {
        return mBinding;
    }

    public void setBinding(CommonPopupRecyclerViewBinding binding) {
        mBinding = binding;
    }

    public void setSizeStyle(@SizeStyle.Size int sizeStyle){
        switch (sizeStyle){
            case SizeStyle.W_M:
                setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case SizeStyle.H_M:
                setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                break;
            case SizeStyle.ALL_M:
                setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case SizeStyle.ALL_W:
                setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                break;
        }
    }

    public void dismiss(boolean backIsAlpha) {
        mb_backIsAlpha = backIsAlpha;
        super.dismiss();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        mb_backIsAlpha = true;
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.7f);
    }

    public void showAtLocation(View parent, int gravity, int x, int y,boolean isAlpha) {
        mb_backIsAlpha = true;
        super.showAtLocation(parent, gravity, x, y);
        if (isAlpha){
            backgroundAlpha(0.7f);
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        mb_backIsAlpha = true;
        super.showAsDropDown(anchor);
        backgroundAlpha(0.7f);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        mb_backIsAlpha = true;
        super.showAsDropDown(anchor, xoff, yoff);
        backgroundAlpha(0.7f);
    }

    @Override
    public void showAsDropDown(View anchor, int gravity, int xoff, int yoff) {
        mb_backIsAlpha = true;
        super.showAsDropDown(anchor, xoff, yoff,gravity);
        backgroundAlpha(0.7f);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    public void setBg(int fan_png_bg_right_menu) {
        mBinding.commonPRViewRoot.setBackgroundResource(fan_png_bg_right_menu);
    }

    public static class SizeStyle{

        public static final int W_M = 1;
        public static final int H_M = 2;
        public static final int ALL_M = 3;
        public static final int ALL_W = 4;

        @IntDef({W_M, H_M,ALL_M,ALL_W})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Size{}
    }

    public void setDismissListener(DismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    public interface DismissListener {
        void dismiss();
    }
}
