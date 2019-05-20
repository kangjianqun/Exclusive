package com.kjq.common.utils.network;


import com.kjq.common.utils.annotation.KeyValue;

import org.json.JSONObject;

/**
 * 与硬件通信数据
 */
public class DHPara {

    @KeyValue(majorKey = "userKey")
    private String mS_userKey;

    @KeyValue(majorKey = "data")
    private JSONObject mJO_masterData;

    @KeyValue(majorKey = "messageId")
    private String mS_messageId;

    @KeyValue(majorKey = "action")
    private String mS_action;

    @KeyValue(majorKey = "id")
    private String mS_id;

    public DHPara(String s_id, JSONObject jAL_masterData){
        setS_id(s_id);
        setJO_masterData(jAL_masterData);
    }
    public DHPara(String s_id,String s_action,String s_messageId, JSONObject jAL_masterData){
        setS_id(s_id);
        setS_action(s_action);
        setS_messageId(s_messageId);
        setJO_masterData(jAL_masterData);
    }

    private DHPara(){

    }

    public static DHPara getNullInstance(){
        return new DHPara();
    }

    public String getS_messageId() {
        return mS_messageId;
    }

    public void setS_messageId(String s_messageId) {
        mS_messageId = s_messageId;
    }

    public String getS_id() {
        return mS_id;
    }

    public void setS_id(String s_id) {
        mS_id = s_id;
    }

    public String getS_userKey() {
        return mS_userKey;
    }

    public void setS_userKey(String s_userKey) {
        mS_userKey = s_userKey;
    }



    public String getS_action() {
        return mS_action;
    }

    public void setS_action(String s_action) {
        mS_action = s_action;
    }

    public JSONObject getJO_masterData() {
        return mJO_masterData;
    }

    public void setJO_masterData(JSONObject JO_masterData) {
        mJO_masterData = JO_masterData;
    }
}
