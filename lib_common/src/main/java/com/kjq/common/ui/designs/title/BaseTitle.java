package com.kjq.common.ui.designs.title;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import com.kjq.common.R;
import com.kjq.common.base.mvvm.base.BaseViewModel;
import com.kjq.common.databinding.CommonDefaultTitleBinding;
import com.kjq.common.utils.ScreenSizeUtils;
import com.kjq.common.utils.Utils;

import static com.kjq.common.utils.data.Constant.AppInfo.TITLE_HEIGHT;


/**
 * <p>标题</p>
 *
 * @author 康建群 948182974---->>>2018/9/10 9:22
 * @version 1.0.0
 */
public class BaseTitle {

    /**
     * 将标题插入跟布局
     */
    public static void insertRootLayout(Activity activity, BaseViewModel baseViewModel){
        View sRootView = Utils.getRootView(activity);
        boolean sB_ok = sRootView instanceof LinearLayout;
        if (sB_ok) {
            LinearLayout rootView = (LinearLayout) sRootView;
            CommonDefaultTitleBinding sBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.common_default_title, null, false);
            sBinding.setBaseViewModel(baseViewModel);

            View sView = sBinding.getRoot();
            int sI_height = ScreenSizeUtils.INSTANCE.dp2px(activity, TITLE_HEIGHT);
//            int top = 0;
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
//                top = Utils.getStatusBarHeight(mActivity);
//                view.setPadding(0,top,0,0);
//            }
//            LinearLayout.LayoutParams sLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, sI_height+top);
            LinearLayout.LayoutParams sLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, sI_height);
            sView.setLayoutParams(sLayoutParams);
            rootView.addView(sView, 0);
            try {
                AppCompatActivity sAppCompatActivity = (AppCompatActivity) activity;
                sAppCompatActivity.setSupportActionBar((Toolbar) sView.findViewById(R.id.common_defaultTitle_toolbar));
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
