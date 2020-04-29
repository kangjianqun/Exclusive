package com.kjq.common.utils.data.cache;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 异步操作的回调函数
 * @param <T>
 */
public class AsyncCallback<T> {
    public void onResult(byte[] bytes) {
    }

    public void onResult(T object) {

    }

    public Type getType() {
        Type superClass = getClass().getGenericSuperclass();
        return  ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
}
