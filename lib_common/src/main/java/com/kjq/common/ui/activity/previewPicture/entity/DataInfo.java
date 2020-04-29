package com.kjq.common.ui.activity.previewPicture.entity;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.previewlibrary.enitity.IThumbViewInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DataInfo implements IThumbViewInfo {
    private String url;

    private DataInfo(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Rect getBounds() {
        return null;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    protected DataInfo(Parcel parcel){
        this.url = parcel.readString();
    }

    public static final Creator<DataInfo> CREATOR = new Creator<DataInfo>() {
        @Override
        public DataInfo createFromParcel(Parcel source) {
            return new DataInfo(source);
        }

        @Override
        public DataInfo[] newArray(int size) {
            return new DataInfo[size];
        }
    };

    public static List<DataInfo> getData(@NotNull List<String> list){
        List<DataInfo> sDataInfos = new ArrayList<>();
        for (String sS : list){
            sDataInfos.add(new DataInfo(sS));
        }
        return sDataInfos;
    }
}