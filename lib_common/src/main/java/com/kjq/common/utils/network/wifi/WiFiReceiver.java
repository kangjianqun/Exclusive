package com.kjq.common.utils.network.wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

/**
 *wifi 广播
 * Created by Administrator on 2017/11/25 0025.
 */

public class WiFiReceiver extends BroadcastReceiver {

    private WIFIListener mWIFIListener;
    private Context mContext;

    private static class WiFiReceiverHolder{
        @SuppressLint("StaticFieldLeak")
        private static final WiFiReceiver INSTANCE = new WiFiReceiver();
    }

    private WiFiReceiver() {
    }

    public static WiFiReceiver getInstance() {
        return WiFiReceiverHolder.INSTANCE;
    }

    public void onClose(){
        if (mWIFIListener != null){
            try{
                mContext.unregisterReceiver(this);
            }catch (Exception ignored){ }
        }
    }

    /**
     * 接受wifi广播
     * @param context  上下午
     * @param wifiListener 监听接口
     */
    public void observeWifiSwitch(Context context,WIFIListener wifiListener){
        mWIFIListener = wifiListener;
        mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mContext.registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
        switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                if (mWIFIListener != null) {
                    mWIFIListener.wifiSwitchState(WIFIListener.WIFI_STATE_DISABLED);
//                    }
//                    break;
//                case WifiManager.WIFI_STATE_DISABLING:
//                    if (mWifiSwitch_interface != null){
//                        mWifiSwitch_interface.wifiSwitchState(WifiSwitch_Interface.WIFI_STATE_DISABLING);
//                    }
//                    break;
//                case WifiManager.WIFI_STATE_ENABLED:
//                    if (mWifiSwitch_interface != null){
//                        mWifiSwitch_interface.wifiSwitchState(WifiSwitch_Interface.WIFI_STATE_ENABLED);
//                    }
//                    break;
//                case WifiManager.WIFI_STATE_ENABLING:
//                    if ( mWifiSwitch_interface != null ) {
//                        mWifiSwitch_interface.wifiSwitchState(WifiSwitch_Interface.WIFI_STATE_ENABLING);
//                    }
//                    break;
//                case WifiManager.WIFI_STATE_UNKNOWN:
//                    if ( mWifiSwitch_interface != null ){
//                        mWifiSwitch_interface.wifiSwitchState( WifiSwitch_Interface.WIFI_STATE_UNKNOWN );
                }
                break;
        }

        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                switch (networkInfo.getState()) {
                    case CONNECTED:
                        mWIFIListener.wifiSwitchState(WIFIListener.NETWORK_CONNECTION_WIFI);
//                        Log.e("APActivity", "CONNECTED");
                        break;
//                    case CONNECTING:
//                        Log.e("APActivity", "CONNECTING");
//                        break;
//                    case DISCONNECTED:
//                        Log.e("APActivity", "DISCONNECTED");
//                        break;
//                    case DISCONNECTING:
//                        Log.e("APActivity", "DISCONNECTING");
//                        break;
//                    case SUSPENDED:
//                        Log.e("APActivity", "SUSPENDED");
//                        break;
//                    case UNKNOWN:
////                        Log.e("APActivity", "UNKNOWN");
//                        break;
//                    default:
//                        break;
                }
            }
        }

//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//            if (mWIFIListener != null && info != null && info.getTypeName().equals("WIFI")) {
//                mWIFIListener.wifiSwitchState(WIFIListener.NETWORK_CONNECTION_WIFI);
//            }
//                if (info != null) {
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("info.getTypeName() : " + info.getTypeName() + "\n");
//                    sb.append("getSubtypeName() : " + info.getSubtypeName() + "\n");
//                    sb.append("getState() : " + info.getState() + "\n");
//                    sb.append("getDetailedState() : " + info.getDetailedState().name() + "\n");
//                    sb.append("getDetailedState() : " + info.getExtraInfo() + "\n");
//                    sb.append("getType() : " + info.getType());
//                    Log.e("bug", sb.toString());
//                }
//        }
    }
}
