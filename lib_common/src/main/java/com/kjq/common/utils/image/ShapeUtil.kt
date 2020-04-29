package com.kjq.common.utils.image

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.kjq.common.utils.Utils

object ShapeUtil {

    fun tintDrawable(drawable: Drawable, colorRes: Int):Drawable{
        val gd = drawable as GradientDrawable
        gd.setColor(ContextCompat.getColor(Utils.getContext(), colorRes))
        return gd
    }
}
