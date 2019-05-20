package com.kjq.common.utils.network;

import com.kjq.common.utils.annotation.KeyValue;

import org.json.JSONArray;

public class DHIRData {


    @KeyValue(majorKey = "ir_hostAddress")
    public String BindHostAddress ;
    @KeyValue(majorKey = "ir_rFAddress")
    public String RFAddress ;

    @KeyValue(majorKey = "ir_order")
    private String mS_order;
    @KeyValue(majorKey = "ir_len")
    private int mI_len;
    @KeyValue(majorKey = "irOrder")
    private JSONArray mZJAL_int_data;

    public DHIRData(String s_order, int i_len, JSONArray JALint_data){
        setS_order(s_order);
        setI_len(i_len);
        setZJAL_int_data(JALint_data);
    }

    public DHIRData(String bindHostAddress, String RFAddress, String s_order, int i_len, JSONArray ZJAL_int_data) {
        BindHostAddress = bindHostAddress;
        this.RFAddress = RFAddress;
        mS_order = s_order;
        mI_len = i_len;
        mZJAL_int_data = ZJAL_int_data;
    }

    private DHIRData(){

    }

    public static DHIRData getNullInstance(){
        return new DHIRData();
    }

    public String getBindHostAddress() {
        return BindHostAddress;
    }

    public void setBindHostAddress(String bindHostAddress) {
        BindHostAddress = bindHostAddress;
    }

    public String getRFAddress() {
        return RFAddress;
    }

    public void setRFAddress(String RFAddress) {
        this.RFAddress = RFAddress;
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
