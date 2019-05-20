package com.kjq.common.utils.onClick;

import android.content.Context;
import android.widget.Toast;

import java.util.Locale;

/**
 * 双击退出识别器
 * Created by Administrator on 2018/1/16 0016.
 */

public class DoubleClickExitDetector {
    public static String DEFAULT_HINT_MESSAGE_CHINA = "再按一次退出程序";
    public static String DEFAULT_HINT_MESSAGE_CHINA_TW = "再按壹次退出程序";
    public static String DEFAULT_HINT_MESSAGE_OTHER = "Press again to exit the program";
    private int effectiveIntervalTime;  // 有效的间隔时间，单位毫秒
    private long lastClickTime; // 上次点击时间
    private String hintMessage; // 提示消息
    private Context context;

    /**
     * 创建一个双击退出识别器
     * @param context Androdi上下文
     * @param hintMessage 提示消息
     * @param effectiveIntervalTime 有效间隔时间
     */
    public DoubleClickExitDetector(Context context, String hintMessage, int effectiveIntervalTime) {
        this.context = context;
        this.hintMessage = hintMessage;
        this.effectiveIntervalTime = effectiveIntervalTime;
    }

    /**
     * 创建一个双击退出识别器，有效间隔时间默认为2000毫秒
     * @param context Android上下文
     * @param hintContent 提示消息
     */
    public DoubleClickExitDetector(Context context, String hintContent) {
        this(context, hintContent, 2000);
    }

    /**
     * 创建一个双击退出识别器，中国环境下默认提示消息为“再按一次退出程序”，其它环境下默认提示消息为“Press again to exit the program”；有效间隔时间默认为2000毫秒
     * @param context Android上下文
     */
    public DoubleClickExitDetector(Context context) {
        String sS;
        if (Locale.getDefault().equals(Locale.TRADITIONAL_CHINESE)){
            sS = DEFAULT_HINT_MESSAGE_CHINA_TW;
        }else if (Locale.getDefault().equals(Locale.ENGLISH)){
            sS = DEFAULT_HINT_MESSAGE_OTHER;
        }else {
            sS = DEFAULT_HINT_MESSAGE_CHINA;
        }
        this.context = context;
        this.hintMessage = sS;
        this.effectiveIntervalTime = 2000;
//        this(context, sS, 2000);
    }

    /**
     * 点击，你需要重写Activity的onBackPressed()方法，先调用此方法，如果返回true就执行父类的onBackPressed()方法来关闭Activity否则不执行
     * @return 当两次点击时间间隔小于有效间隔时间时就会返回true，否则返回false
     */
    public boolean click(){
        long currentTime = System.currentTimeMillis();
        boolean result = (currentTime-lastClickTime) < effectiveIntervalTime;
        lastClickTime = currentTime;
        if(!result){
            Toast.makeText(context, hintMessage, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    /**
     * 设置有效间隔时间，单位毫秒
     * @param effectiveIntervalTime 效间隔时间，单位毫秒。当两次点击时间间隔小于有效间隔时间click()方法就会返回true
     */
    public void setEffectiveIntervalTime(int effectiveIntervalTime) {
        this.effectiveIntervalTime = effectiveIntervalTime;
    }

    /**
     * 设置提示消息
     * @param hintMessage 当前点击同上次点击时间间隔大于有效间隔时间click()方法就会返回false，并且显示提示消息
     */
    public void setHintMessage(String hintMessage) {
        this.hintMessage = hintMessage;
    }
}
