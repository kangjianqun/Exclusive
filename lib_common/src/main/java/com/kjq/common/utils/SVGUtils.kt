package com.kjq.common.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import java.lang.NullPointerException

/**
 *
 * SVG 工具类
 *
 * @author 康建群 948182974---->>>2018/8/11 16:16
 * @version 1.0.0
 */
object SVGUtils{

    fun svgHint(context: Context,imageView: ImageView,drawableRes: Int,colorRes: Int){
        //你需要改变的颜色
        val vectorDrawableCompat : VectorDrawableCompat = VectorDrawableCompat.create(context.resources,drawableRes,context.theme)
                ?: throw NullPointerException("VectorDrawableCompat object form by "+drawableRes.toString()+" null")
        /* 需改属性之前必须mutate，不然会导致其它地方图片也被修改 */
        vectorDrawableCompat.mutate()
        vectorDrawableCompat.setTint(ContextCompat.getColor(context,colorRes))
        imageView.setImageDrawable(vectorDrawableCompat)
    }

}
