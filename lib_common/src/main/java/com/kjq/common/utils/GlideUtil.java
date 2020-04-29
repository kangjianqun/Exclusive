package com.kjq.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.kjq.common.R;

public class GlideUtil {



    @SuppressLint("CheckResult")
    public static RequestBuilder<Drawable> newCacheInstance(String url){
        return Glide.with(Utils.getContext()).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .apply(RequestOptions.placeholderOf(R.drawable.common_png_ic_family_avatar));
    }

    public static void newCache(String url,int imageW,int imageH){
        Glide.with(Utils.getContext()).load(url).submit(imageW, imageH);
    }

    public static void newCacheInstance(final String url, final ImageView imageView){
        newCacheInstance(url,R.drawable.common_png_ic_family_avatar,imageView);
    }

    public static void newCacheInstance(String url,int placeholderRes, ImageView imageView){
        Glide.with(Utils.getContext()).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .apply(RequestOptions.placeholderOf(placeholderRes))
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void newCacheInstance(String url, RequestListener requestListener){
        newCacheInstance(url).addListener(requestListener).submit();
    }
}
