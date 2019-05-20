package com.kjq.common.utils.performance;

import java.lang.reflect.Field;

/**
 * 内存优化管理
 * Created by Administrator on 2018/1/16 0016.
 */

public class MemoryUtils {
    /**
     * 释放(置null)当前类声明的属性或对象
     * 调用:
     * MemoryUtils.release(this)或
     * MemoryUtils.release(obj.getClass())
     * @param obj this
     */
    public static void release(Object obj){
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                field.set(obj, null);
            }
        } catch (Exception ignored) {}finally{
            try {
                Field[] fields = obj.getClass().getSuperclass().getDeclaredFields();
                for(Field field:fields){
                    field.setAccessible(true);
                    field.set(obj, null);
                }
            } catch (Exception ignored) { }
        }
        System.gc();
    }
}