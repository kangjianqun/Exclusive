package com.kjq.common.utils.json;



import androidx.databinding.ObservableField;

import com.kjq.common.utils.annotation.KeyValue;
import com.kjq.common.utils.data.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONUtils {
    public static final String SEPARATOR = "-";

    /**
     * 拼接 key和值
     * @param s_key key
     * @param hashMap_param 值
     * @return 拼接好的字符串
     */
    public static String jointCommand(String s_key, HashMap<String,String> hashMap_param){
        return s_key + SEPARATOR + hashMap_param.get(s_key);
    }

    /**
     * 拼接 key和值
     * @param s_key key
     * @param value 值
     * @return 拼接好的字符串
     */
    public static String jointCommand(String s_key, String value){
        return s_key + SEPARATOR + value;
    }

    @NotNull
    private static String[] partition(@NotNull String value){
        int sI_separator = value.indexOf(SEPARATOR);
        String sS_key = value.substring(0,sI_separator);
        String sS_value = value.substring(sI_separator+1,value.length());
        return new String[]{sS_key,sS_value};
    }


    /**
     * 从JSON中取出值
     * @param jsonObject 对象
     * @param s_key key
     * @return 有则取出，无则反空
     */
    public static String getValue(JSONObject jsonObject, String s_key){
        if (jsonObject == null){
            return "";
        }
        try {
            return jsonObject.getString(s_key);
        } catch (JSONException e) {
            return "";
        }
    }

    public static String getValue(String json,String key){
        try {
            JSONObject sJSONObject = new JSONObject(json);
            return getValue(sJSONObject,key);
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * 从JSON中取出值
     * @param jsonObject 对象
     * @param s_key key
     * @return 有则取出，无则反空
     */
    public static Object getToValue(JSONObject jsonObject,String s_key){
        if (jsonObject == null){
            return "";
        }
        try {
            return jsonObject.get(s_key);
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * 从JSON中取出值
     * @param json 字符串
     * @param s_key key
     * @return 有则取出，无则返回“”
     */
    public static Object getToValue(String json,String s_key){
        try {
            JSONObject sJSONObject = new JSONObject(json);
            return getToValue(sJSONObject,s_key);
        } catch (JSONException e) {
            return "";
        }
    }

    public static<C> JSONArray getJA(ArrayList<C> arrayList){
        JSONArray sJSONArray = new JSONArray();
        for (C sC : arrayList){
            Class sClassTYpe = sC.getClass();
            if (sClassTYpe == String.class ||
                    sClassTYpe == int.class ||sClassTYpe == Integer.class ||
                    sClassTYpe == boolean.class || sClassTYpe == Boolean.class ||
                    sClassTYpe == long.class || sClassTYpe == Long.class) {
                sJSONArray.put(sC);
            }else {
                sJSONArray.put(createJson(sC));
            }
        }
        return sJSONArray;
    }

    public static String createJsonArray(ArrayList<JSONObject> arrayList){
        JSONArray sJSONArray = new JSONArray();
        for (JSONObject item :arrayList) {
            sJSONArray.put(item);
        }
        return sJSONArray.toString();
    }

    /**
     * 生成JSON对象
     * @param keyAndValue 数组
     * @return 对象
     */
    public static JSONObject createJson(ArrayList<String> keyAndValue){
        try {
            JSONObject sJSONObject = new JSONObject();
            for (int sI = 0; sI < keyAndValue.size(); sI++) {
                String[] sS_data = partition(keyAndValue.get(sI));
                StringBuilder s_value = new StringBuilder();
                if (sS_data.length > 2) {
                    for (int a = 1; a < sS_data.length; a++) {
                        if (sS_data[a].equals("")) {
                            s_value.append(SEPARATOR);
                        }
                        else {
                            s_value.append(sS_data[a]);
                        }
                        if (a != sS_data.length -1) {
                            s_value.append(SEPARATOR);
                        }
                    }
                }
                else {
                    s_value = new StringBuilder(sS_data[1]);
                }
                sJSONObject.put(sS_data[0], s_value);
            }
            return sJSONObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成Json
     * @param userInfo 用户的的固定信息
     * @param commandParam 命令的参数
     * @return 完整数组
     */
    public static JSONObject createJson(ArrayList<String> userInfo,ArrayList<String> commandParam){
        JSONObject sJSONObject ;
        String sS_paramName;
        try {
            sJSONObject = new JSONObject();
            for (int sI=0;sI<userInfo.size();sI++){
                String[] sS_data = partition(userInfo.get(sI));
                sJSONObject.put(sS_data[0],sS_data[1]);
            }

            JSONObject param = new JSONObject();
            for (int i=0;i<commandParam.size();i++){
                if (i>0){
                    String[] sS_data = partition(commandParam.get(i));
                    param.put(sS_data[0],sS_data[1]);
                }
            }
            sS_paramName = commandParam.get(0);
            sJSONObject.put(sS_paramName,param);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return sJSONObject;
    }

    /**
     * 生成Json对象
     * @param model 对象
     * @param <T> 泛型
     * @return 返回JSONObject对象
     */
    public static <T> JSONObject createJson(T model){
        JSONObject sJSONObject ;
        try {
            sJSONObject = new JSONObject();
            Field[] sFields = model.getClass().getDeclaredFields();
            for (Field sField : sFields){
                sField.setAccessible(true);
                String sS_majorKey;
                String sS_assistantKey;
                Object s0_value ;
                try {
                    KeyValue sMyKey = sField.getAnnotation(KeyValue.class);
                    if (sMyKey == null){
                        continue;
                    }

                    Object sO = sField.get(model);
                    if (sO == null){
                        continue;
                    }

                    Class sClassType = sField.getType();
                    if (sClassType == String.class
                            || sClassType == Integer.class || sClassType == int.class
                            || sClassType == Long.class || sClassType == long.class
                            || sClassType == Boolean.class || sClassType == boolean.class
                            || sClassType == Double.class || sClassType == double.class
                            || sClassType == Float.class || sClassType == float.class
                            || sClassType == Byte.class || sClassType == byte.class
                            || sClassType == Byte[].class || sClassType == byte[].class
                            || sClassType == Short.class || sClassType == short.class
                            || sClassType == ObservableField.class){
                        if (sClassType == ObservableField.class){
                            Object value = ((ObservableField)sO).get();
                            if (value != null && !StringUtils.isEmpty(String.valueOf(value))){
                                s0_value = String.valueOf(value);
                            }else {
                                continue;
                            }
                        }else {
                            s0_value = sO;
                        }
                    }else if (sClassType == JSONObject.class || sClassType == JSONArray.class){
                        s0_value = sO;
                    }else {
                        try {
//                            s0_value = createJson(sO).toString();
                            s0_value = createJson(sO);
                        }catch (Exception e){
                            s0_value = "";
                        }
                    }
                    sS_majorKey = sMyKey.majorKey();
                    sS_assistantKey = sMyKey.assistantKey();
                    sJSONObject.put(sS_majorKey,s0_value);
                    if (!sS_assistantKey.equals("")){
                        sJSONObject.put(sS_assistantKey,s0_value);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            sJSONObject = null;
        }

        return sJSONObject;
    }
}

