package com.kjq.common.utils.performance;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityUtils {

    // 声明一个集合用于记录所有打开的活动
    private static ArrayList<Activity> list = new ArrayList<Activity>();

    // 加入活动对象--------->onCreate
    public static void add(Activity activity) {
        list.add(activity);
    }

    // 移除活动对象--------->onDestroy
    public static void remove(Activity activity) {
        list.remove(activity);
    }

    // 关闭所有的活动--------->close
    public static void removeAll() {
        for (Activity activity : list) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static int getSurvival() {
        return list.size();
    }
}
