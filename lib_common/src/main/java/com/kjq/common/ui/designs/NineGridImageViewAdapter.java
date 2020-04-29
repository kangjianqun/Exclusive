package com.kjq.common.ui.designs;

import android.content.Context;
import android.widget.ImageView;

import com.kjq.common.utils.GlideUtil;

public abstract class NineGridImageViewAdapter<T> {
    protected abstract void onDisplayImage(Context context, ImageView imageView, T t);

    protected ImageView generateImageView(Context context){
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    static Default getDefaultAdapter(){
        return new Default();
    }

    public static class Default extends NineGridImageViewAdapter<String>{
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String t) {
            GlideUtil.newCacheInstance(t,imageView);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }
    }
}
