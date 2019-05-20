package com.kjq.common.utils.network;



import com.kjq.common.utils.annotation.AnnotationUtils;
import com.kjq.common.utils.annotation.KeyValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;

/**
 * http 返回的数据对象
 * Created by Administrator on 2018/2/26 0026.
 */

public class DHttpReturn {

    @KeyValue(majorKey = "action")
    private String mS_action;
    @KeyValue(majorKey = "")
    private String mS_actionType;
    @KeyValue(majorKey = "responseResult")
    private boolean mB_result;
    /*需要发送tcp的目标，用来通知其他用户，true 代表发送共享的人，false 代表不发送，具体id代表发送单个目标*/
    @KeyValue(majorKey = "")
    private String mS_target;
    @KeyValue(majorKey = "resultCode")
    private String mS_hintCode;
    @KeyValue(majorKey = "return")
    private String mS_data;

    private Request mRequest;
    private String mS_errorWhy;

    public static DHttpReturn getInstance(String s_returnData){
        DHttpReturn sDHttpReturn = new DHttpReturn();
        try {
            JSONObject sJSONObject = new JSONObject(s_returnData);
            sDHttpReturn.setB_result(false);
            sDHttpReturn = AnnotationUtils.traverseData(sDHttpReturn,sJSONObject);
            sDHttpReturn.interpretingData();
        } catch (JSONException e) {
            sDHttpReturn.setS_hintCode("10000");
            sDHttpReturn.setS_action("000");
            sDHttpReturn.setS_action("");
            sDHttpReturn.setB_result(false);
            sDHttpReturn.setS_data("");
        }
        return sDHttpReturn;
    }

    public static DHttpReturn getInstance(Request request, IOException e){
        DHttpReturn sDHttpReturn = new DHttpReturn();
        sDHttpReturn.setRequest(request);
        sDHttpReturn.setS_errorWhy(e.getMessage());
        return sDHttpReturn;
    }

    /**
     * 解析数据
     * 格式化数据，转换一下，方面调用
     */
    private void interpretingData(){
        try {
            setS_action(getS_action().substring(0,getS_action().lastIndexOf("Response")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getS_hintCode() {
        return mS_hintCode;
    }

    private void setS_hintCode(String s_hintCode) {
        mS_hintCode = s_hintCode;
    }

    public String getS_data() {
        return mS_data;
    }

    private void setS_data(String s_data) {
        mS_data = s_data;
    }

    public boolean isB_result() {
        return mB_result;
    }

    private void setB_result(boolean b_result) {
        mB_result = b_result;
    }

    public Request getRequest() {
        return mRequest;
    }

    private void setRequest(Request request) {
        mRequest = request;
    }

    public String getS_errorWhy() {
        return mS_errorWhy;
    }

    private void setS_errorWhy(String s_errorWhy) {
        mS_errorWhy = s_errorWhy;
    }

    public String getS_action() {
        return mS_action;
    }

    private void setS_action(String s_action) {
        mS_action = s_action;
    }

    public void setS_actionType(String s_actionType) {
        mS_actionType = s_actionType;
    }

    public String getS_actionType() {
        return mS_actionType;
    }

    public String getS_target() {
        return mS_target;
    }

    public void setS_target(String s_target) {
        mS_target = s_target;
    }
}
