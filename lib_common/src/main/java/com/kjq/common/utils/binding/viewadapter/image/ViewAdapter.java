package com.kjq.common.utils.binding.viewadapter.image;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.kjq.common.utils.GlideUtil;

/**
 * Created by kjq on 2019/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            GlideUtil.newCacheInstance(url,placeholderRes,imageView);
        }
    }

    @BindingAdapter("commonDrawable")
    public static void setImageUri(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter(value = "commonBitmap")
    public static void setImageBitmap(ImageView imageView, Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
}

