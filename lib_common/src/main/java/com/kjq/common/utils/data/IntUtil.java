package com.kjq.common.utils.data;

public class IntUtil {

    public static int data(Object o){

        int sI = 0;

        Class sClassType = o.getClass();

        if (sClassType == String.class){
            sI = Integer.valueOf((String) o);
        }else if (sClassType == Integer.class || sClassType == int.class){
            sI = (Integer) o;
        }

        return sI;
    }

}
