package com.kjq.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Canvas
import android.graphics.Color
import android.os.Environment
import android.text.TextUtils
import android.view.ViewTreeObserver
import java.io.IOException
import android.os.Environment.MEDIA_MOUNTED
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


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

    private const val IMAGE_FILE_NAME_TEMPLATE = "Image%s.jpg"
    private const val IMAGE_FILE_PATH_TEMPLATE = "%s/%s"

    @SuppressLint("SimpleDateFormat")
    fun createImagePath(): String {
        //判断sd卡是否存在
        if (Environment.getExternalStorageState() == MEDIA_MOUNTED) {
            //文件名
            val systemTime = System.currentTimeMillis()
            val imageDate = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Date(systemTime))
            val mFileName = String.format(IMAGE_FILE_NAME_TEMPLATE, imageDate)

            //文件全名
            val mstRootPath = Environment.getExternalStorageDirectory().absolutePath
            val filePath = String.format(IMAGE_FILE_PATH_TEMPLATE, mstRootPath, mFileName)
            val file = File(filePath)
            if (!file.exists()) {
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return filePath
        }
        return ""
    }

    /**
     * 回调
     */
    fun screenshot(targetView: View, shotCallback: ShotCallback) {
        // 核心代码start
        val bitmap = Bitmap.createBitmap(targetView.width, targetView.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        c.drawColor(Color.WHITE)
        targetView.draw(c)
        // end
        shotCallback.onShotComplete(bitmap)
    }

    /**
     * view转bitmap
     */
    fun viewConversionBitmap(v: View): Bitmap {
        val w = v.width
        val h = v.height

        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)

        c.drawColor(Color.WHITE)
        /** 如果不设置canvas画布为白色，则生成透明  */

        v.layout(0, 0, w, h)
        v.draw(c)

        return bmp
    }

    interface ShotCallback{
        fun onShotComplete(bitmap: Bitmap)
    }

    @Throws(IOException::class)
    fun compressAndGenImage(image: Bitmap, outPath: String, maxSize: Int) {
        val os = ByteArrayOutputStream()
        // scale
        var options = 100
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os)
        // Compress by loop
        if (maxSize != 0) {
            while (os.toByteArray().size / 1024 > maxSize) {
                // Clean up os
                os.reset()
                // interval 10
                options -= 10
                image.compress(Bitmap.CompressFormat.JPEG, options, os)
            }
        }

        // Generate compressed image file
        val fos = FileOutputStream(outPath)
        fos.write(os.toByteArray())
//        Logger.getLogger().d("compressAndGenImage--->文件大小：" + os.size() + "，压缩比例：" + options)
        fos.flush()
        fos.close()
    }

    @Throws(IOException::class)
    fun compressAndGenImage(image: Bitmap, outPath: String) {
        val os = ByteArrayOutputStream()
        // scale
        val options = 70
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os)

        // Generate compressed image file
        val fos = FileOutputStream(outPath)
        fos.write(os.toByteArray())
//        Logger.getLogger().d("compressAndGenImage--->文件大小：" + os.size() + "，压缩比例：" + options)
        fos.flush()
        fos.close()
    }

    fun scaleViewAndChildren(view: View,float: Float,isRoot: Boolean){
        val localLayoutParams = view.layoutParams
        if (localLayoutParams.width != -1  &&  localLayoutParams.width != -2){
            localLayoutParams.width = (float * localLayoutParams.width).toInt()
        }
        if (localLayoutParams.height != -1  &&  localLayoutParams.height != -2){
            localLayoutParams.height = (float * localLayoutParams.height).toInt()
        }

        if (!isRoot){
            if (localLayoutParams is ViewGroup.MarginLayoutParams){
                localLayoutParams.leftMargin = (float * localLayoutParams.leftMargin).toInt()
                localLayoutParams.rightMargin = (float * localLayoutParams.rightMargin).toInt()
                localLayoutParams.topMargin = (float * localLayoutParams.topMargin).toInt()
                localLayoutParams.bottomMargin = (float * localLayoutParams.bottomMargin).toInt()
            }
        }

        view.layoutParams = localLayoutParams
        view.setPadding(
                (float * view.paddingLeft).toInt(),
                (float * view.paddingTop).toInt(),
                (float * view.paddingRight).toInt(),
                (float * view.paddingBottom).toInt())

        if (view is TextView){
            view.textSize = float * view.textSize
        }
        if (view is ViewGroup){
            for (i in 0 until view.childCount){
                scaleViewAndChildren(view.getChildAt(i),float,false)
            }
        }
    }
}
