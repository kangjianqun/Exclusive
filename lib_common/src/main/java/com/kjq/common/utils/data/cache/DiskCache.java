package com.kjq.common.utils.data.cache;

import com.kjq.common.utils.data.GsonUtil;
import com.kjq.common.utils.hint.KLog;

import java.io.File;
import java.io.Serializable;

/**
 * DiskCache某个目录对应的缓存管理器，包含自动清理控制器AutoClearController、异步文件操作器AsyncFileCache
 * 1、记录文件大小，达到上限时删除最老的文件
 * 2、文件读写
 * Created by xxx on 2018/3/19
 */
public class DiskCache {
    private static final String TAG = "DiskCache";

    private static DiskCache mDiskCache;

    private AutoClearController mAutoClearController;

    /**
     * 异步
     */
    private AsyncFileCache mAsyncFileCache;
    /**
     * 同步
     */
    private FileCache mFileCache;

    private DiskCache() {
    }

    private DiskCache(String cacheDir, long maxSize) {
        mAsyncFileCache = new AsyncFileCache(cacheDir);
        mFileCache = new FileCache(cacheDir);
        mAutoClearController = new AutoClearController(cacheDir, maxSize) {
            @Override
            boolean deleteFile(File file) {
                KLog.d(TAG, "~~~~deleteFile " + file == null ? null : file.getAbsolutePath() + "~~~~");
                return mAsyncFileCache != null && mAsyncFileCache.deleteFile(file);
            }
        };
        mAsyncFileCache.setAutoClearController(mAutoClearController);
    }

    public static DiskCache getInstance(String cacheDir, long maxSize) {
        if (mDiskCache == null) {
            synchronized (DiskCache.class) {
                if (mDiskCache == null) {
                    mDiskCache = new DiskCache(cacheDir, maxSize);
                }
            }
        }
        return mDiskCache;
    }

    public void setAutoClearEnable(boolean enable) {
        if (mAutoClearController != null) {
            mAutoClearController.setAutoClearEnable(enable);
        }
        KLog.d(TAG, "~~~~setAutoClearEnable: " + enable + "~~~~");
    }

    public void put(String key, byte[] value) {
        if (mAsyncFileCache != null) {
            mAsyncFileCache.asyncPut(key, value);
        }
        KLog.d(TAG, "put: " + key );
    }

    public void putObject(String key, Serializable value) {
        if (mAsyncFileCache != null) {
            mAsyncFileCache.asyncPutObject(key, value);
        }
        KLog.d(TAG,"putObject:" + key );
    }

    public void putObject(String key, Object value) {
        KLog.d(TAG,"putObject: " + key);
        try {
            if (mAsyncFileCache != null) {
                mAsyncFileCache.asyncPutObject(key, value);
            }
        }catch (Exception e ){
            KLog.d(TAG,"putObject Exception: " + e.getMessage());
        }
    }

    public void putString(String key,String value){
        KLog.d(TAG,"putString: " + key);
        try {
            mFileCache.put(key,value.getBytes());
        }catch (Exception e ){
            KLog.d(TAG,"putObject Exception: " + e.getMessage());
        }
    }

    public String get(String key){
        return new String(mFileCache.get(key));
    }

    public <T> T get(String key,Class<T> classOfT){
        return GsonUtil.INSTANCE.parse(new String(mFileCache.get(key)),classOfT);
    }

    public void get(String key, AsyncCallback callback) {
        if (callback == null) {
            return;
        }
        if (mAsyncFileCache != null) {
            mAsyncFileCache.asyncGet(key, callback);
        }
        KLog.d(TAG, "~~~~get: " + key + "~~~~");
    }

    public void getObject(String key, AsyncCallback callback) {
        if (callback == null) {
            return;
        }
        if (mAsyncFileCache != null) {
            mAsyncFileCache.asyncGetObject(key, callback);
        }
        KLog.d(TAG,"~~~~getObject: " + key + "~~~~");
    }

    public void remove(String key) {
        if (mAsyncFileCache != null) {
            mAsyncFileCache.asyncRemove(key);
        }
        KLog.d(TAG,"~~~~remove: " + key + "~~~~");
    }

    public void clear() {
        if (mAsyncFileCache != null) {
            mAsyncFileCache.asyncClear();
        }
        KLog.d(TAG,"~~~~clear~~~~");
    }
}
