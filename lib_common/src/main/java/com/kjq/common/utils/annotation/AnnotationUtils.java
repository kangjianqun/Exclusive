package com.kjq.common.utils.annotation;




import androidx.databinding.ObservableField;

import com.kjq.common.utils.data.BooleanUtils;
import com.kjq.common.utils.data.IntUtil;
import com.kjq.common.utils.data.StringUtils;
import com.kjq.common.utils.data.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * 注解工具
 *
 * <ul>
 * <li>{@link KeyValue} </li>
 * </ul>
 */
public class AnnotationUtils {

    /**
     * 自动给对象属性添加值
     * @param model 对象
     * @param jsonObject JSON对象
     * @param <T> 对象
     * @return 带参对象
     */
    public static  <T> T traverseData(T model, JSONObject jsonObject){
        Iterator<String> sIterator = jsonObject.keys();
        while (sIterator.hasNext()){

            Object sO_value;
            String sS_key = sIterator.next();
            Field[] sFields = model.getClass().getDeclaredFields();
            for (Field sField : sFields) {
                sField.setAccessible(true);

                KeyValue sMyKey = sField.getAnnotation(KeyValue.class);
                if (sMyKey == null) {
                    continue;
                }
                String sS_fieldKey = sMyKey.majorKey();
                String sS_assistantKey = sMyKey.assistantKey();
                Class sClass_obsField = sMyKey.observableFieldType();
                if (!sS_key.equals(sS_fieldKey) && !sS_key.equals(sS_assistantKey)) {
                    continue;
                }

                Class sClassType = sField.getType();
                sO_value = JSONUtils.getToValue(jsonObject,sS_key);
                if (sClassType.equals(String.class)) {
                    sO_value = sO_value.toString();
                } else if (sClassType.equals(Integer.class) || sClassType.equals(int.class)){
                    sO_value = IntUtil.data(sO_value);
                }else if (sClassType.equals(Boolean.class) || sClassType.equals(boolean.class)) {
                    sO_value = BooleanUtils.valueOf(sO_value.toString());
                } else if (sClassType.equals(ObservableField.class)) {
                    if (sClass_obsField == String.class) {
                        sO_value = new ObservableField<>(sO_value.toString());
                    } else if (sClass_obsField == Boolean.class || sClass_obsField == boolean.class) {
                        sO_value = new ObservableField<>(BooleanUtils.valueOf(sO_value.toString()));
                    }
                }else if (sClassType != JSONObject.class && sClass_obsField != JSONArray.class) {

                    if (sMyKey.tClass() != Object.class && !StringUtils.isEmpty(sMyKey.methodName())){
                        Class sClass;
                        try {
                            sClass = Class.forName(sMyKey.tClass().getName());
                            Method m = sClass.getDeclaredMethod(sMyKey.methodName());
                            Object sO = m.invoke(sClass);
                            sO = traverseData(sO, (JSONObject) sO_value);
                            sO_value = sO;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    if (sO_value != null) {
                        sField.set(model, sO_value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return model;
    }

    /**
     * 自动给对象属性添加值
     * @param model 对象
     * @param s_json JSON字符串
     * @param <T> 对象
     * @return 带参对象
     */
    public static  <T> T traverseData(T model, String s_json){
        JSONObject sJSONObject;
        try{
            sJSONObject = new JSONObject(s_json);
            return traverseData(model,sJSONObject);
        } catch (JSONException e) {
            return model;
        }
    }

    /**
     * 赋值，将不完整的对象中有的值，赋予完整的模型
     * @param model 完整对象
     * @param model_incomplete 部分参数的对象
     * @param <T> 泛型
     * @return 返回赋值完成后的对象
     */
    public static <T> T assignmentValues(T model,T model_incomplete){
        Field[] sFields_incomplete = model_incomplete.getClass().getDeclaredFields();
        for (Field sField_incomplete : sFields_incomplete) {
            KeyValue sMyKey_incomplete = sField_incomplete.getAnnotation(KeyValue.class);
            if (sMyKey_incomplete == null){
                continue;
            }
            sField_incomplete.setAccessible(true);
            Object sO_value = null;
            try {
                sO_value = sField_incomplete.get(model_incomplete);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (sO_value == null){
                continue;
            }
            Class value_type = sField_incomplete.getType();
            if (value_type == ObservableField.class){
                sO_value = ((ObservableField)sO_value).get();
            }
            if (sO_value == null){
                continue;
            }
            Field[] sFields_model = model.getClass().getDeclaredFields();
            for (Field sField_model : sFields_model) {
                sField_model.setAccessible(true);
                KeyValue sMyKey_model = sField_model.getAnnotation(KeyValue.class);
                if (sMyKey_model == null){
                    continue;
                }
                if (sMyKey_incomplete.majorKey().equals(sMyKey_model.majorKey())){
                    try {
                        Class sClass_obsField = sMyKey_model.observableFieldType();

                        Class sC_value_type = sField_model.getType();
                        if (sC_value_type == ObservableField.class){
                            if (sClass_obsField == String.class){
                                sO_value = new ObservableField<>(sO_value.toString());
                            }else if (sClass_obsField == Boolean.class || sClass_obsField == boolean.class){
                                sO_value = new ObservableField<>(BooleanUtils.valueOf(sO_value.toString()));
                            }
                        }

                        sField_model.set(model,sO_value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return model;
    }
}
