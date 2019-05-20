package com.kjq.common.utils.network;


import com.kjq.common.utils.annotation.KeyValue;

import org.json.JSONArray;

public class DHMasterData {

    @KeyValue(majorKey = "rfPara")
    private JSONArray mJAL_rfData;

    @KeyValue(majorKey = "irPara")
    private JSONArray mJAL_irData;

    @KeyValue(majorKey = "linkPara")
    private String mS_linkData;

    @KeyValue(majorKey = "upgrade")
    private String mS_update;

    @KeyValue(majorKey = "wifi_rssi")
    private String mS_wifiRssi;

    @KeyValue(majorKey = "version")
    private String mS_version;
    public DHMasterData(JSONArray irData, JSONArray rfData){
        setJAL_irData(irData);
        setJAL_rfData(rfData);
    }

    public DHMasterData(String s_linkData){
        setS_linkData(s_linkData);
    }

    private DHMasterData(){

    }

    public static DHMasterData getNullInstance(){
        return new DHMasterData();
    }

    public String getS_update() {
        return mS_update;
    }

    public void setS_update(String s_update) {
        mS_update = s_update;
    }

    public String getS_linkData() {
        return mS_linkData;
    }

    public void setS_linkData(String s_linkData) {
        mS_linkData = s_linkData;
    }

    public JSONArray getJAL_irData() {
        return mJAL_irData;
    }

    public void setJAL_irData(JSONArray JAL_irData) {
        mJAL_irData = JAL_irData;
    }

    public JSONArray getJAL_rfData() {
        return mJAL_rfData;
    }

    public void setJAL_rfData(JSONArray JAL_rfData) {
        mJAL_rfData = JAL_rfData;
    }
    public String getS_version() {
        return mS_version;
    }

    public void setS_version(String s_version) {
        mS_version = s_version;
    }

    public String getS_wifiRssi() {
        return mS_wifiRssi;
    }

    public void setS_wifiRssi(String s_wifiRssi) {
        mS_wifiRssi = s_wifiRssi;
    }
}
