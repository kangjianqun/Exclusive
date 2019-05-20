package com.kjq.common.utils.network;


import com.kjq.common.utils.annotation.KeyValue;
import com.kjq.common.utils.data.Constant;
import com.kjq.common.utils.json.JSONUtils;

import org.json.JSONArray;

/**
 * 服务器通信结构基类
 */
public class ParamServerBase {
    @KeyValue(majorKey = Constant.Comm.Key.K_HARDWARE_COMMAND,tClass = DHPara.class,methodName = "getNullInstance")
    private DHPara mDHPara;
    @KeyValue(majorKey = Constant.Comm.Key.K_HARDWARE_ID)
    private JSONArray mJSONArray;

    public static ParamServerBase getNullInstance() {
        return new ParamServerBase();
    }

    private ParamServerBase(){

    }

    ParamServerBase(JSONArray jsonArray) {
        setJSONArray(jsonArray);
    }

    public static ParamServerBase getLinkage(String hardwareId,String action,String messageId,String linkageData){
        ParamServerBase sParamServerBase = new ParamServerBase();
        DHMasterData sDHMasterData = new DHMasterData(linkageData);
        sParamServerBase.setDHPara(new DHPara(hardwareId, action,messageId, JSONUtils.createJson(sDHMasterData)));
        return sParamServerBase;
    }

    ParamServerBase(String s_hardwareId, String s_commandCode){
        DHRFData sDHRFData = new DHRFData(s_commandCode,0,null);
        JSONArray sJSONArray = new JSONArray();
        sJSONArray.put(JSONUtils.createJson(sDHRFData));
        DHMasterData sDHMasterData = new DHMasterData(null,sJSONArray);
        setDHPara(new DHPara(s_hardwareId,JSONUtils.createJson(sDHMasterData)));
    }

    ParamServerBase(String hardwareId, String commandCode, String upgrade, String userKey){
        DHMasterData sDHMasterData = new DHMasterData(null,null);
        sDHMasterData.setS_update(upgrade);
        setDHPara(new DHPara(hardwareId,JSONUtils.createJson(sDHMasterData)));
        mDHPara.setS_userKey(userKey);
    }

    ParamServerBase(String hardwareId, String commandCode, String upgrade){
        JSONArray sJSONArray = null;
        if (commandCode != null){
            DHRFData sDHRFData = new DHRFData(commandCode,0,null);
            sJSONArray = new JSONArray();
            sJSONArray.put(JSONUtils.createJson(sDHRFData));
        }
        DHMasterData sDHMasterData = new DHMasterData(null,sJSONArray);
        sDHMasterData.setS_update(upgrade);
        setDHPara(new DHPara(hardwareId,JSONUtils.createJson(sDHMasterData)));
    }

    ParamServerBase(String s_hardwareId, String s_irOrder, int byteLen, JSONArray jA_data){
        JSONArray sJSONArray = new JSONArray();
        sJSONArray.put(JSONUtils.createJson(new DHIRData(s_irOrder,byteLen,jA_data)));
        DHMasterData sDHMasterData = new DHMasterData(sJSONArray,null);
        setDHPara(new DHPara(s_hardwareId,JSONUtils.createJson(sDHMasterData)));
    }

    ParamServerBase(String s_hardwareId,String action,String messageId, String rFAddress, String hostAddress, String s_irOrder, int byteLen, JSONArray jA_data){
        JSONArray sJSONArray = new JSONArray();
        sJSONArray.put(JSONUtils.createJson(new DHIRData(hostAddress,rFAddress,s_irOrder,byteLen,jA_data)));
        DHMasterData sDHMasterData = new DHMasterData(sJSONArray,null);
        setDHPara(new DHPara(s_hardwareId,action,messageId,JSONUtils.createJson(sDHMasterData)));
    }



    void setDHPara(DHPara DHPara) {
        mDHPara = DHPara;
    }

    public void setJSONArray(JSONArray JSONArray) {
        mJSONArray = JSONArray;
    }

    public DHPara getDHPara() {
        return mDHPara;
    }

    public JSONArray getJSONArray() {
        return mJSONArray;
    }
}
