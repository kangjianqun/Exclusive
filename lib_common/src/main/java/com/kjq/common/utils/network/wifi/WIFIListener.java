package com.kjq.common.utils.network.wifi;

/**
 *
 * Created by Administrator on 2017/11/25 0025.
 */

public interface WIFIListener {

    public final int WIFI_STATE_ENABLING = 0 ;
    public final int WIFI_STATE_ENABLED = 1 ;
    public final int WIFI_STATE_DISABLING = 2 ;
    public final int WIFI_STATE_DISABLED = 3 ;
    public final int WIFI_STATE_UNKNOWN  = 4 ;
    public final int NETWORK_CONNECTION_WIFI = 5;
    public final int NETWORK_CONNECTION_MOBILE = 6;

    void wifiSwitchState(int state);

}
