package com.kjq.common.utils.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

object BitmapUtil {
    /**
     * 得到bitmap 跟view大小一样
     * @param v view
     * @return bitmap
     */
    fun createBitmap(v: View): Bitmap {
        return createBitmap(v,-1,-1)
    }

    /**
     * 得到bitmap
     * @param v view
     * @param width 宽
     * @param height 高
     * @return bitmap
     */
    fun createBitmap(v: View, width: Int, height: Int): Bitmap {
        if (width != -1 && height != -1){
            //测量使得view指定大小
            val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            v.measure(measuredWidth, measuredHeight)
            //调用layout方法布局后，可以得到view的尺寸大小
            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        }

        val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        v.draw(c)
        return bmp
    }


}
