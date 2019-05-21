package com.kjq.common.ui.designs.title;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import com.kjq.common.R;
import com.kjq.common.databinding.CommonBaseTitleBinding;
import com.kjq.common.ui.designs.title.annotation.AnnTitle;
import com.kjq.common.ui.designs.title.event.TitleClickListener;
import com.kjq.common.utils.SVGUtils;
import com.kjq.common.utils.ScreenSizeUtils;
import com.kjq.common.utils.Utils;

import static com.kjq.common.utils.data.Constant.AppInfo.TITLE_HEIGHT;


/**
 * <p>标题</p>
 *
 * @author 康建群 948182974---->>>2018/9/10 9:22
 * @version 1.0.0
 */
public class BaseTitle implements View.OnClickListener {

    public CommonBaseTitleBinding mBinding;
    private Activity mActivity;
    private TitleClickListener mTitleClickListener;
    private View view;
    private View mV_menu;

    public ObservableField<String> mSOField_txt = new ObservableField<>("默认标题");
    private boolean mB_ok;

    private BaseTitle(Activity activity,@ColorRes int themeColor) {
        mActivity = activity;
        if (insertRootLayout(themeColor)){
            mV_menu = mBinding.dBTitleMenu;
            mBinding.dBTitleTViewTxt.setSelected(true);
            mBinding.setBaseTitle(this);
            mBinding.setClick(this);
        }
    }

    /**
     * 将标题插入跟布局
     */
    @SuppressLint("ResourceAsColor")
    private boolean insertRootLayout(@ColorRes int themeColor){
        View root = Utils.getRootView(mActivity);
        mB_ok = root instanceof LinearLayout;
        if (mB_ok) {
            LinearLayout rootView = (LinearLayout) root;
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.common_base_title, null,false);
            mBinding.dBTitleRoot.setBackgroundColor(themeColor);
            view = mBinding.getRoot();
            int sI_height = ScreenSizeUtils.INSTANCE.dp2px(mActivity, TITLE_HEIGHT);
//            int top = 0;
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
//                top = Utils.getStatusBarHeight(mActivity);
//                view.setPadding(0,top,0,0);
//            }
//            LinearLayout.LayoutParams sLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, sI_height+top);
            LinearLayout.LayoutParams sLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, sI_height);
            view.setLayoutParams(sLayoutParams);
            rootView.addView(view, 0);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (mTitleClickListener == null) return;
        int sI_id = v.getId();
        if (sI_id == R.id.dBTitle_imgBtn_back) {
            mTitleClickListener.back();
        }else if (sI_id == R.id.dBTitle_menu
        || sI_id == R.id.dBTitle_menuTxt){
            mTitleClickListener.menu();
        }
    }

    public static AnnTitle getTitleAnn(@NonNull Object o){
        return o.getClass().getAnnotation(AnnTitle.class);
    }

    public static class Builder{
        private BaseTitle mBaseTitle;

        public Builder(Activity activity,@ColorRes int themeColor) {
            mBaseTitle = new BaseTitle(activity,themeColor);
        }

        public void setTitleGone(boolean isGoone){
            mBaseTitle.view.setVisibility(isGoone ? View.GONE : View.VISIBLE);
        }

        public String getSOField_txt() {
            return mBaseTitle.mSOField_txt.get();
        }

        public void setSOField_txt(String s_txt) {
            mBaseTitle.mSOField_txt.set(s_txt);
        }

        public void setTitleTxtColor(@ColorInt int colorRes){
            mBaseTitle.mBinding.dBTitleTViewTxt.setTextColor(colorRes);
        }

        public void setTitleLsftSignColor(@ColorRes int colorRes){
            SVGUtils.INSTANCE.svgHint(Utils.getContext(),mBaseTitle.mBinding.dBTitleImgBtnBack,R.drawable.common_svg_chevron_left_black_24dp,colorRes);
        }

        public TitleClickListener getTitleClickListener() {
            return mBaseTitle.mTitleClickListener;
        }

        public void setTitleClickListener(TitleClickListener titleClickListener) {
            mBaseTitle.mTitleClickListener = titleClickListener;
        }

        public void setRightDisplay(boolean visible){
            if (mBaseTitle.mV_menu == null){return;}
            mBaseTitle.mV_menu.setVisibility(visible ? View.VISIBLE : View.GONE);
            mBaseTitle.mBinding.dBTitleMenuTxt.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        public void setRightMenu(View.OnClickListener onClickListener){
            mBaseTitle.mBinding.dBTitleMenu.setOnClickListener(onClickListener);
        }

        public void setRightImage(@DrawableRes int image){
            if (mBaseTitle.mB_ok){
                mBaseTitle.mBinding.dBTitleMenuTxt.setVisibility(View.GONE);
                mBaseTitle.mBinding.dBTitleMenu.setVisibility(View.VISIBLE);
                mBaseTitle.mBinding.dBTitleMenu.setImageResource(image);
            }
        }

        public void setRightTxt(String rightTxt){
            if (mBaseTitle.mB_ok){

                mBaseTitle.mBinding.dBTitleMenu.setVisibility(View.GONE);
                mBaseTitle.mBinding.dBTitleMenuTxt.setVisibility(View.VISIBLE);
                mBaseTitle.mBinding.dBTitleMenuTxt.setText(rightTxt);
            }
        }


        public View getRightMenu(){
            return mBaseTitle.mBinding.dBTitleMenu;
        }

        public void setRootBackground(@ColorRes int color){
            if (mBaseTitle.mB_ok){
                mBaseTitle.view.setBackgroundColor(ContextCompat.getColor(mBaseTitle.mActivity, color));
            }
        }

        public void setTitleTextCenter(){
            mBaseTitle.mBinding.dBTitleImgBtnBack.setVisibility(View.GONE);
            mBaseTitle.mBinding.dBTitleTViewTxt.setGravity(Gravity.CENTER);
        }
    }
}
