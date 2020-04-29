package com.kjq.common.utils.data.cache;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 异步写入、删除、清空
 * AsyncFileCache文件异步操作类，异步操作文件的读、写、删，里面的线程池代码就不贴了，自己写一个就好。
 * Created by xxx on 2018/3/20
 */
class AsyncFileCache<T> extends FileCache {
    public AsyncFileCache(String dirPath) {
        super(dirPath);
    }

    public void asyncPut(final String key, final byte[] value) {
        if (value == null) {
            return;
        }
        ThreadPoolFactory.instance().fixExecutor(new Runnable() {
            @Override
            public void run() {
                put(key, value);
            }
        });
    }

    public void asyncRemove(final String key) {
        ThreadPoolFactory.instance().fixExecutor(new Runnable() {
            @Override
            public void run() {
                remove(key);
            }
        });
    }

    public void asyncClear() {
        ThreadPoolFactory.instance().fixExecutor(new Runnable() {
            @Override
            public void run() {
                clear();
            }
        });
    }

    public void asyncGet(String key, final AsyncCallback<T> callback) {
        if (callback == null) {
            return;
        }
        Type t = callback.getType();
        final Object object = get(key);
        if (object == null || !(object.getClass().equals(t))) {
            return;
        }
        ThreadPoolFactory.instance().fixExecutor(new Runnable() {
            @Override
            public void run() {
                callback.onResult((T)object);
            }
        });
    }

    void asyncPutObject(final String key, final Serializable value) {
        if (value == null) {
            return;
        }
        ThreadPoolFactory.instance().fixExecutor(new Runnable() {
            @Override
            public void run() {
                putObject(key, value);
            }
        });
    }

    void asyncPutObject(final String key, final Object value) {
        if (value == null) {
            return;
        }
        ThreadPoolFactory.instance().fixExecutor(new Runnable() {
            @Override
            public void run() {
                putObject(key, value);
            }
        });
    }

    void asyncGetObject(final String key, final AsyncCallback<T> callback) {
        if (callback == null) {
            return;
        }
        Type t = callback.getType();
        final Object object = getObject(key);
        if (object == null || !(object.getClass().equals(t))) {
            return;
        }
        ThreadPoolFactory.instance().fixExecutor(new Runnable() {
            @Override
            public void run() {
                callback.onResult((T)object);
            }
        });
    }
}
