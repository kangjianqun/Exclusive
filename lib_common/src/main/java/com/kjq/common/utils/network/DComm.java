package com.kjq.common.utils.network;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;


import com.kjq.common.utils.DeviceUtils;
import com.kjq.common.utils.Utils;
import com.kjq.common.utils.annotation.KeyValue;
import com.kjq.common.utils.data.Constant;
import com.kjq.common.utils.data.SPUtils;
import com.kjq.common.utils.data.StringUtils;

import java.lang.reflect.Field;

import okhttp3.FormBody;

/**
 * 通信
 * Created by Administrator on 2018/3/6 0006.
 */

class DComm<T> implements IDComm{

    @KeyValue(majorKey = Constant.Comm.Key.K_ACTION)
    private String mS_action;
    @KeyValue(majorKey = Constant.Comm.Key.K_ACTION_TYPE)
    private String mS_actionType;
    @KeyValue(majorKey = Constant.Comm.Key.K_MESSAGE_ID)
    private String mS_messageId;
    @KeyValue(majorKey = Constant.Comm.Key.K_NOTIFICATION_TARGET)
    private String mS_notificationTarget;
    @KeyValue(majorKey = Constant.Comm.Key.K_UNIQUE_ID)
    private String mS_uniqueId;
    @KeyValue(majorKey = Constant.Comm.Key.K_ID)
    private String mS_holder;
    @KeyValue(majorKey = Constant.Comm.Key.K_COMMAND_PARAM)
    private T mT_model;
    @KeyValue(majorKey = Constant.Comm.Key.K_COMMAND_PARAM)
    private String mS_data;

    private FormBody.Builder mBuilder;
    private StringBuilder mStringBuilder;

    void initParam(String s_action, String s_actionType, @Nullable String s_userId, @Nullable String s_notificationTarget, @Nullable T t_model, @Nullable String s_data){
        mBuilder = new FormBody.Builder();
        mStringBuilder = new StringBuilder();
        String sS_holder = s_userId != null ? s_userId : SPUtils.getSpecialValue(Utils.getContext(),SPUtils.TYPE_ACCOUNT);
        setS_holder(sS_holder);
        mS_messageId = "123456";
        String sS_uniqueId = DeviceUtils.id(Utils.getContext());
        setS_uniqueId(sS_uniqueId);
        setS_action(s_action);
        setS_actionType(s_actionType);
        if (t_model != null){
            setT_model(t_model);
        }

        if (s_data != null){
            setS_data(s_data);
        }

        if (!StringUtils.isEmpty(s_notificationTarget)){
            setS_notificationTarget(s_notificationTarget);
        }
    }

    private void setS_action(String s_action) {
        mS_action = s_action;
    }


    private void setS_notificationTarget(String s_notificationTarget) {
        mS_notificationTarget = s_notificationTarget;
    }

    private void setS_uniqueId(String s_uniqueId) {
        mS_uniqueId = s_uniqueId;
    }

    private void setS_holder(String s_holder) {
        mS_holder = s_holder;
    }

    private void setT_model(T t_model) {
        mT_model = t_model;
    }

    public void setS_data(String s_data) {
        mS_data = s_data;
    }

    public void setS_actionType(String s_actionType) {
        mS_actionType = s_actionType;
    }

    @Override
    public <C> FormBody.Builder getFormBody(C module) {
        return getFBody(module);
    }

    @Override
    public FormBody.Builder getFormBody() {
        return getFBody(this);
    }

    private<C> FormBody.Builder getFBody(C module){
        Field[] sFields = module.getClass().getDeclaredFields();
        for (Field sField : sFields){
            sField.setAccessible(true);
            KeyValue sMyKey = sField.getAnnotation(KeyValue.class);
            if (sMyKey == null){
                continue;
            }
            try {
                Object sO = sField.get(module);
                if (sO == null){
                    continue;
                }
                String sS_majorKey;
                String sS_assistantKey;
                String sS_value ;
                Class sClassType = sField.getType();
                if (sClassType == String.class
                        || sClassType == Integer.class
                        || sClassType == int.class
                        || sClassType == Long.class
                        || sClassType == long.class
                        || sClassType == Boolean.class
                        || sClassType == boolean.class
                        || sClassType == Double.class
                        || sClassType == double.class
                        || sClassType == Float.class
                        || sClassType == float.class
                        || sClassType == Byte.class
                        || sClassType == byte.class
                        || sClassType == Short.class
                        || sClassType == short.class
                        || sClassType == ObservableField.class){

                    if (sClassType == ObservableField.class){
                        Object sO_value = ((ObservableField)sO).get();
                        if (sO_value != null && !StringUtils.isEmpty(String.valueOf(sO_value))){
                            sS_value = String.valueOf(sO_value);
                        }else {
                            continue;
                        }
                    }else {
                        sS_value = sO.toString();
                    }
                    sS_majorKey = sMyKey.majorKey();
                    sS_assistantKey = sMyKey.assistantKey();
                    mBuilder.add(sS_majorKey,sS_value);
                    if (!sS_assistantKey.equals("")) {
                        mBuilder.add(sS_assistantKey, sS_value);
                    }
                }else {
                    try {
                        getFBody(sO);
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mBuilder;
    }

    private <C> String getString(C model){
        Field[] sFields = model.getClass().getDeclaredFields();
        for (Field sField : sFields){
            sField.setAccessible(true);
            try {
                Object sO = sField.get(model);
                if (sO == null){
                    continue;
                }
                String sS_majorKey;
                String sS_assistantKey;
                String sS_value ;

                Class sClassType = sField.getType();
                if (sClassType == String.class
                        || sClassType == Integer.class
                        || sClassType == int.class
                        || sClassType == Long.class
                        || sClassType == long.class
                        || sClassType == Boolean.class
                        || sClassType == boolean.class
                        || sClassType == Double.class
                        || sClassType == double.class
                        || sClassType == Float.class
                        || sClassType == float.class
                        || sClassType == Byte.class
                        || sClassType == byte.class
                        || sClassType == Short.class
                        || sClassType == short.class){
                    sS_value = sO.toString();
                    KeyValue sMyKey = sField.getAnnotation(KeyValue.class);
                    if (sMyKey != null){
                        sS_majorKey = sMyKey.majorKey();
                        sS_assistantKey = sMyKey.assistantKey();
                        mStringBuilder.append(sS_majorKey).append("=").append(sS_value).append("&");
                        if (!sS_assistantKey.equals("")){
                            mStringBuilder.append(sS_assistantKey).append("=").append(sS_value).append("&");
                        }
                    }
                }else {
                    try {
                        getString(sO);
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if ( mStringBuilder.charAt(mStringBuilder.length() -1 ) == '&'){
            mStringBuilder.deleteCharAt(mStringBuilder.length() -1);
        }
        return mStringBuilder.toString();
    }
}
