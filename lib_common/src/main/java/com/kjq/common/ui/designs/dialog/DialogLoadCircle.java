package com.kjq.common.ui.designs.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kjq.common.R;


/**
 * <p>加载过度动画</p>
 *
 * @author 康建群 948182974---->>>2018/8/1 18:37
 * @version 1.3.0
 */

public class DialogLoadCircle {

    private static Dialog createDialog(Activity activity, String msg){
        View sView = LayoutInflater.from(activity).inflate(R.layout.common_dialog_load_circle,null);
//        获取整个布局
        LinearLayout sLayout = sView.findViewById(R.id.dialog_layout_bg);
//        页面中的Img
        ImageView img = sView.findViewById(R.id.dialog_iView_loading);
//        页面中的提示文本
        TextView textView = sView.findViewById(R.id.dialog_tView_message);
//        加载动画，动画用户使img图片不停的旋转
        Animation animation = AnimationUtils.loadAnimation(activity,R.anim.common_dialog_anim);
//        显示动画
        img.setAnimation(animation);
//        显示文本
        textView.setText(msg);
//        创建自定义样式
        Dialog _dialog = new Dialog(activity, R.style.CommonDialog);
//        设置返回键无效
//        _dialog.setCancelable(false);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        ViewGroup.LayoutParams sLayoutParams = new ViewGroup.LayoutParams(width, height);
        _dialog.setContentView(sLayout, sLayoutParams);
        return _dialog;
    }
    private static Dialog mDialog;
    //    打开
    public static void showDialog(Activity activity,String msg){
        if (mDialog == null){
            mDialog = DialogLoadCircle.createDialog(activity,msg);
            mDialog.show();
        }else {
            closeDialog();
            showDialog(activity,msg);
        }
    }

    //    关闭
    public static void closeDialog(){
        if (mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
