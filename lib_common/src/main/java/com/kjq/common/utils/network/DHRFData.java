package com.kjq.common.utils.network;


import com.kjq.common.utils.annotation.KeyValue;

import org.json.JSONArray;

public class DHRFData {
    @KeyValue(majorKey = "rf_order")
    private String mS_order;
    @KeyValue(majorKey = "rf_len")
    private int mI_len;
    @KeyValue(majorKey = "rfOrder")
    private JSONArray mZJAL_int_data;

    public DHRFData(String s_order, int i_len, JSONArray JALint_data){
        setS_order(s_order);
        setI_len(i_len);
        setZJAL_int_data(JALint_data);
    }

    private DHRFData(){

    }

    public static DHRFData getNullInstance(){
        return new DHRFData();
    }

    public String getS_order() {
        return mS_order;
    }

    public void setS_order(String s_order) {
        mS_order = s_order;
    }

    public int getI_len() {
        return mI_len;
    }

    public void setI_len(int i_len) {
        mI_len = i_len;
    }

    public JSONArray getZJAL_int_data() {
        return mZJAL_int_data;
    }

    public void setZJAL_int_data(JSONArray ZJAL_int_data) {
        mZJAL_int_data = ZJAL_int_data;
    }
}
