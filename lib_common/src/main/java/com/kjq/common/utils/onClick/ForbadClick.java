package com.kjq.common.utils.onClick;

import android.view.View;


/**
 * 快速双击
 * Created by Administrator on 2018/1/16 0016.
 */

public class ForbadClick {

    private static long lastClickTime;
    private static View view;
    private static boolean isTwo = false;

    public static boolean isFastDoubleClick(View v) {
        long time = System.currentTimeMillis();
        if ( view == v && time - lastClickTime < 500) {
            return true;
        }
        view = v;
        lastClickTime = time;
        return false;
    }
    public static boolean isFastDoubleClick() {

        long time = System.currentTimeMillis();
//        Log.d(TAG, "isFastDoubleClick: time:"+time+"lastClickTime:"+lastClickTime+"result:"+(time - lastClickTime));
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
