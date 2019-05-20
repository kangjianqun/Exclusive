package com.kjq.common.utils.network;

import android.content.Context;

import com.kjq.common.utils.DeviceUtils;
import com.kjq.common.utils.Utils;
import com.kjq.common.utils.annotation.KeyValue;
import com.kjq.common.utils.data.Constant;
import com.kjq.common.utils.data.SPUtils;


public class CommServer {

    @KeyValue(majorKey = Constant.Comm.Key.K_ACTION)
    private String mS_action;

    @KeyValue(majorKey = Constant.Comm.Key.K_UNIQUE_ID)
    private String mS_uniqueId;

    @KeyValue(majorKey = Constant.Comm.Key.K_HOLDER)
    private String mS_holder;

    @KeyValue(majorKey = Constant.Comm.Key.K_COMMAND_PARAM,tClass = ParamServerBase.class,methodName = "getNullInstance")
    private ParamServerBase mParam;
    private CommServer() {

    }

    public static CommServer getNullInstance() {
        return new CommServer();
    }
    CommServer(String s_action, ParamServerBase param){
        Context sContext = Utils.getContext();
        String sS_userAccount = SPUtils.getSpecialValue(sContext,SPUtils.TYPE_ACCOUNT);
        String sS_uniqueId = DeviceUtils.id(sContext);
        setS_holder(sS_userAccount);
        setS_uniqueId(sS_uniqueId);
        setS_action(s_action);
        setParam(param);
    }

    public void setS_holder(String s_holder) {
        mS_holder = s_holder;
    }

    public void setS_uniqueId(String s_uniqueId) {
        mS_uniqueId = s_uniqueId;
    }

    public void setS_action(String s_action) {
        mS_action = s_action;
    }

    public void setParam(ParamServerBase param) {
        mParam = param;
    }

    public String getS_holder() {
        return mS_holder;
    }

    public String getS_uniqueId() {
        return mS_uniqueId;
    }

    public String getS_action() {
        return mS_action;
    }

    public ParamServerBase getParam() {
        return mParam;
    }
}
