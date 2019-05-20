package com.kjq.common.utils

import android.content.Context

/**
 *
 * 屏幕工具类
 *
 * 1.0.1 添加了，获取屏幕px
 * @author 康建群 948182974---->>>2018/8/22 17:32
 * @version 1.0.1
 */
object ScreenSizeUtils {

    val displayH: Int
        get() = Utils.getContext().resources.displayMetrics.heightPixels

    val displayW: Int
        get() = Utils.getContext().resources.displayMetrics.widthPixels

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    fun dip2px(dp: Int): Int {
        val density = Utils.getContext().resources.displayMetrics.density
        return (dp * density + 0.5).toInt()
    }

    /**
     * px转换dip
     */
    fun px2dip(px: Int): Int {
        val scale = Utils.getContext().resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    /**
     * px转换sp
     */
    fun px2sp(pxValue: Int): Int {
        val fontScale = Utils.getContext().resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * sp转换px
     */
    fun sp2px(spValue: Int): Int {
        val fontScale = Utils.getContext().resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * dp单位转换为px
     * @param context 上下文，需要通过上下文获取到当前屏幕的像素密度
     * @param dpValue dp值
     * @return px值
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        return (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()
    }

    /**
     * px单位转换为dp
     * @param context 上下文，需要通过上下文获取到当前屏幕的像素密度
     * @param pxValue px值
     * @return dp值
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        return (pxValue / context.resources.displayMetrics.density + 0.5f).toInt()
    }

}
