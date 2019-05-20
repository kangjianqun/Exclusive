package com.kjq.common.utils.network;


import com.kjq.common.utils.data.Constant;
import com.kjq.common.utils.data.DataUtils;
import com.kjq.common.utils.json.JSONUtils;

import org.json.JSONArray;

import java.util.ArrayList;

public class DHFactory {

    public static ParamServerBase getParamControl(String s_hardwareId, String s_commandCode){
        return new ParamServerBase(s_hardwareId,s_commandCode);
    }

    public static ParamServerBase getParamControlIR(String s_hardwareId,String action ,String messageId,String rFAddress, String hostAddress, String order, byte[] bytes){
        // 发送码
        JSONArray sJSONArray = null;
        int sI_len = 0;
        if (bytes != null){
            ArrayList<Integer> sIntegers = DataUtils.byteArrayTwoIntArray(bytes);
            sJSONArray = JSONUtils.getJA(sIntegers);
            sI_len = bytes.length;
        }
        return new ParamServerBase(s_hardwareId,action,messageId,rFAddress,hostAddress,order,sI_len,sJSONArray);
    }

    public static ParamServerBase getParamUpgrade(String hardwareId, String commandCode, String upgrade){
        return new ParamServerBase(hardwareId,commandCode,upgrade);
    }

    public static ParamServerBase getParamQueryHardwareInfo(String hardwareId, String upgrade, String userKey){
        return new ParamServerBase(hardwareId,null,upgrade,userKey);
    }

    public static ParamServerBase getParamLinkage(String hardwareId,String action,String message ,String linkageData){
        return ParamServerBase.getLinkage(hardwareId,action,message,linkageData);
    }

    public static ParamServerBase getParamLearn(String s_hardwareId, String s_order, int byteLen, byte[] bytes){
        // 发送码
        JSONArray sJSONArray = null;
        if (bytes != null){
            ArrayList<Integer> sIntegers = DataUtils.byteArrayTwoIntArray(bytes);
            sJSONArray = JSONUtils.getJA(sIntegers);
        }
        return new ParamServerBase(s_hardwareId,s_order,byteLen,sJSONArray);
    }

    public static ParamServerBase getParamHardware(ArrayList<Object> aL_hardware){
        JSONArray sJSONArray = new JSONArray();
        for (Object sHardware : aL_hardware){
            sJSONArray.put(JSONUtils.createJson(sHardware));
        }
        return new ParamServerBase(sJSONArray);
    }

    /**
     * 得到服务器用的通信结构
     * @param s_action 动作
     * @param param 参数
     * @return jsonString
     */
    public static String getCommServer(String s_action, ParamServerBase param){
        CommServer sCommServer = new CommServer(s_action,param);
        return JSONUtils.createJson(sCommServer).toString()+ Constant.Comm.S_END;
    }
}
