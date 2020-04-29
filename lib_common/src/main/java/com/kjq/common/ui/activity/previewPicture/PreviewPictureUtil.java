package com.kjq.common.ui.activity.previewPicture;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kjq.common.ui.activity.previewPicture.entity.DataInfo;
import com.kjq.common.utils.GlideUtil;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;
import com.previewlibrary.loader.IZoomMediaLoader;
import com.previewlibrary.loader.MySimpleTarget;

import java.util.List;

public class PreviewPictureUtil {
//    public static void preview(Activity activity, int currentIndex, List<DataInfo> list) {
//        GPreviewBuilder
//                .from(activity)
//                .setData(list)
//                .setCurrentIndex(currentIndex)
//                .setSingleFling(true)
//                .setType(GPreviewBuilder.IndicatorType.Dot)
//                .start();
//    }
    public static void preview(Activity activity, int currentIndex, List<String> list) {
        ZoomMediaLoader.getInstance().init(new IZoomMediaLoader() {
            @Override
            public void displayImage(@NonNull Fragment fragment, @NonNull String s, ImageView imageView, @NonNull MySimpleTarget mySimpleTarget) {
                GlideUtil.newCacheInstance(s,imageView);
                mySimpleTarget.onResourceReady();
            }

            @Override
            public void displayGifImage(@NonNull Fragment fragment, @NonNull String s, ImageView imageView, @NonNull MySimpleTarget mySimpleTarget) {

            }

            @Override
            public void onStop(@NonNull Fragment fragment) {
                Glide.with(fragment).onStop();
            }

            @Override
            public void clearMemory(@NonNull Context c) {
                Glide.get(c).clearMemory();
            }
        });
        List<DataInfo> sDataInfos = DataInfo.getData(list);
        GPreviewBuilder
                .from(activity)
                .setData(sDataInfos)
                .setCurrentIndex(currentIndex)
                .setSingleFling(true)
                .setType(GPreviewBuilder.IndicatorType.Dot)
                .start();
    }
}
