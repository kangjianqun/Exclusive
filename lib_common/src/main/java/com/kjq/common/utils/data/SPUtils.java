package com.kjq.common.utils.data;

import android.content.Context;
import android.content.SharedPreferences;


import com.kjq.common.utils.DeviceUtils;
import com.kjq.common.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

/**
 * SharedPreferences工具类
 * Created by Kang_ on 2017/8/14.
 */

public class SPUtils {
    private static final String FILE_NAME = "login_state";
    private static final String NULL = "";
    /*账号密码*/
    public static final String TYPE_ACCOUNT = "TYPE_ACCOUNT";
    public static final String TYPE_REMEMBER_ACCOUNT = "TYPE_REMEMBER_ACCOUNT";

    public static final String TYPE_DEVICE_ID = "TYPE_DEVICE_ID";
    public static final String TYPE_IS_LOGIN = "TYPE_IS_LOGIN";
    public static final String TYPE_IS_FIRST = "TYPE_IS_FIRST";
    public static final String TYPE_WIFI_PASSWORD = "type_wifi_password";
    public static final String TYPE_KICK_LOGIN = "type_kick_login";


    private static final String SET = "SET";
    private static final String GET = "GET";
    
    /**
     * 操作值
     * @param context con
     * @param setAndGet true ->set
     * @param valueKey 值的key
     * @param value 值
     * @return set -> null get -> 对应值
     */
    private static String operationValue(Context context, String setAndGet, String valueKey, String value){

        return operation(context,setAndGet,valueKey,value);
    }

    private static String operationValue(String setAndGet, String valueKey, String value){
        Context sContext = Utils.getContext();
        return operation(sContext,setAndGet,valueKey,value);
    }

    public static String read(String valueKey){
        return operationValue(SPUtils.GET,valueKey,null);
    }

    public static void put(String valueKey,String value){
        operationValue(SPUtils.SET,valueKey,value);
    }

    /**
     * 操作值
     * @param context context
     * @param setAndGet 保存或者取出
     * @param valueType 类型
     * @param value 需要保存值
     * @return 值
     */
    private static String operation(Context context,String setAndGet,String valueType,String value){
        if (context == null){
            return NULL;
        }
        switch (setAndGet){
            case SET:
                SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
                editor.putString(valueType,value);
                editor.apply();
                return NULL;
            case GET:
                SharedPreferences read = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
                return read.getString(valueType,NULL);
                default:
                    return NULL;
        }
    }

    /**
     * 得到特殊的值
     * @param context context
     * @param valueType 类型
     * @return 值
     */
    public static String getSpecialValue(Context context,String valueType){
        String sS_value = operationValue(context,SPUtils.GET,valueType,null);
        switch (valueType){
            default:
                return sS_value.equals(NULL) ? nullToDefault(context,valueType) : sS_value ;
            case TYPE_ACCOUNT:
                String sS_isLogin = SPUtils.operation(context,GET,TYPE_IS_LOGIN,null);
//                Log.d("myLog", "getSpecialValue: "+Boolean.valueOf(sS_isLogin));
                if (Boolean.valueOf(sS_isLogin)){
                    return sS_value.equals(NULL) ? nullToDefault(context,valueType) : sS_value ;
                }else {
                    return DeviceUtils.id(context);
                }
        }
    }

    /**
     * 如果是null 转成默认值
     * @param valueType 类型
     * @return 值
     */
    private static String nullToDefault(Context context,String valueType){
        String sS_value = null;
        switch (valueType){
            case TYPE_ACCOUNT:
                sS_value = DeviceUtils.id(context);
                break;
            case TYPE_DEVICE_ID:
                sS_value = "0";
                break;
        }
        return sS_value;
    }

}
