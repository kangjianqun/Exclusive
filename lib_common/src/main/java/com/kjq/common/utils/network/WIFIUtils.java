package com.kjq.common.utils.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import com.kjq.common.utils.receiver.IReceiverListener;
import com.kjq.common.utils.receiver.ReceiverUtil;


/**
 *
 * Created by Administrator on 2017/11/24 0024.
 */

public class WIFIUtils {
    private static final int SECURITY_NONE = 0;
    private static final int SECURITY_WEP = 1;
    private static final int SECURITY_PSK = 2;

    public static final int SCAN_ALL_TYPE = 0;
    public static final int SCAN_SCAN_RESULTS = 1;
    public static final int SCAN_CONFIGURED_NETWORKS = 2;
//    private static final int SECURITY_EAP = 3;

    // 定义WifiManager对象
    public WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock;
    private ReceiverUtil mReceiverUtil;

    private WIFIListener mWifiListener;
    private WiFiReceiver mWiFiReceiver;
    private Context mContext;


    private static class WIFIUtilsHolder{
        @SuppressLint("StaticFieldLeak")
        private static final WIFIUtils INSTANCE = new WIFIUtils();
    }

    private WIFIUtils (){}

    public static WIFIUtils getInstance(){
        return WIFIUtilsHolder.INSTANCE;
    }

    public void setWifi(Context context, @Nullable WIFIListener wifiListener){
        startInit(context);
        if (wifiListener != null){
            mWifiListener = wifiListener;
            mWiFiReceiver = WiFiReceiver.getInstance();
        }
    }

    public void setWifi(Context context){
        startInit(context);
    }

    private void startInit(Context context){
        mContext = context.getApplicationContext();
        // 取得WifiManager对象
        mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        if (mWifiManager != null) {
            mWifiInfo = mWifiManager.getConnectionInfo();
        }
    }

    // 打开WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 开启wifi广播监听器
     */
    public void startReceiverListener(){
        if (mWiFiReceiver != null){
            mWiFiReceiver.observeWifiSwitch(mContext,mWifiListener);
        }
    }

    /**
     * 判断是否打开wifi
     * @param context context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = null;
        if (connectivityManager != null) {
            activeNetInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    // 检查当前WIFI状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    // 指定配置好的网络进行连接
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    public void connectWifiConfi(WifiConfiguration wifiConfiguration){
        if (wifiConfiguration != null){
            // 连接配置好的指定ID的网络
            mWifiManager.enableNetwork(wifiConfiguration.networkId,
                    true);
        }
    }
    /**wifi扫描*/
    public void startScan(final int i_type, final IWifiScanListener wifiScanListener) {
        try {
            boolean sB_result = mWifiManager.startScan();
            if (!sB_result){
                wifiScanListener.scanFailure();
            }
        }catch (Exception e ){
            e.printStackTrace();
        }

        ArrayList<String> sStrings = new ArrayList<>();
        sStrings.add(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mReceiverUtil = new ReceiverUtil(mContext, sStrings, new IReceiverListener() {
            @Override
            public void receiverReturn(String action) {
                if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                    switch (i_type){
                        case WIFIUtils.SCAN_ALL_TYPE:
                            mWifiList = mWifiManager.getScanResults();
                            mWifiConfiguration = mWifiManager.getConfiguredNetworks();
                            break;
                        case WIFIUtils.SCAN_SCAN_RESULTS:
                            // 得到扫描结果
                            mWifiList = mWifiManager.getScanResults();
                            break;
                        case WIFIUtils.SCAN_CONFIGURED_NETWORKS:
                            // 得到配置好的网络连接
                            mWifiConfiguration = mWifiManager.getConfiguredNetworks();
                            break;
                    }
                    wifiScanListener.scanSuccess();
                }
                mReceiverUtil.onClose();
            }
        });

        mReceiverUtil.observeReceiver();
    }

    /**得到密码加密方式*/
    public int getSecurity(WifiConfiguration config) {

        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
//        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
//            return SECURITY_EAP;
//        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }

    /**
     * 得到已经保存的wifi配置
     * @return WifiConfiguration
     */
    public WifiConfiguration getWifiConfiguration(String s_SSID,WifiInfo wifiInfo){
        WifiConfiguration sWifiConfiguration = null;
        for (WifiConfiguration wifiConfiguration : mWifiConfiguration) {
            //比较networkId，防止配置网络保存相同的SSID
            String id = wifiConfiguration.SSID.replace("\"","");
            int net = wifiConfiguration.networkId;
            if (s_SSID.equals(id)&&wifiInfo.getNetworkId()==net) {
                sWifiConfiguration = wifiConfiguration;
            }
        }
        return sWifiConfiguration;
    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    public void getScanResultOfSSID(int i_type, final String ssid, final IWifiScanResultListener<ScanResult> wifiScanResultListener){
        startScan(i_type, new IWifiScanListener() {
            @Override
            public void scanSuccess() {
                for (ScanResult s :
                        getWifiList()) {
                    if (ssid.equals(s.SSID)) {
                        wifiScanResultListener.scanResult(s);
                    }
                }
                wifiScanResultListener.scanResult(null);
            }

            @Override
            public void scanFailure() {
                wifiScanResultListener.scanResult(null);
            }
        });
    }

    public void getWifiConOfSSID(final String ssid, final IWifiScanResultListener<WifiConfiguration> wifiScanResultListener) {
        startScan(WIFIUtils.SCAN_CONFIGURED_NETWORKS, new IWifiScanListener() {
            @Override
            public void scanSuccess() {
                for (WifiConfiguration wifiCon :
                        mWifiConfiguration) {
                    if (wifiCon.SSID.equals(ssid)) {
                        wifiScanResultListener.scanResult(wifiCon);
                    }
                }
                wifiScanResultListener.scanResult(null);
            }

            @Override
            public void scanFailure() {
                wifiScanResultListener.scanResult(null);
            }
        });
    }

    // 查看扫描结果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder.append("Index_").append(Integer.toString(i + 1)).append(":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    // 得到MAC地址
    @SuppressLint("HardwareIds")
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getSSID(){
        String sS = null;
        if (mWifiInfo !=null){
            sS = mWifiInfo.getSSID();
        }
        return (sS == null) ? "NULL" : sS.substring(1,sS.length()-1);
    }

    // 得到接入点的BSSID
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到IP地址
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到连接的ID
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到WifiInfo的所有信息包
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * ip 地址转为字符串
     * @param i_IpAddress ip 地址
     * @return 字符串
     */
    public static String intIpToStringIp (int i_IpAddress) {
        return (i_IpAddress & 0xFF ) + "." + ((i_IpAddress >> 8 ) & 0xFF) + "." +
                ((i_IpAddress >> 16 ) & 0xFF) + "." + ( i_IpAddress >> 24 & 0xFF) ;
    }

//    public int addWifiItem (String ssid,String password) {
//
//        List<WifiConfiguration> wifiConfigurations = ((WifiManager)mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConfiguredNetworks();
//        int networkId = -10;
//        if (null != wifiConfigurations) {
//            for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
//                if (wifiConfiguration.SSID.equals("\"" + ssid + "\"")) {
//                    networkId = wifiConfiguration.networkId;
//                    break;
//                }
//            }
//        }
//        if (networkId == -10) {
//            networkId = mWifiManager.addNetwork(createWifiInfo(ssid,password,password.length() == 0 ? 0:2));
//        }
//        return networkId;
//    }

    public int addWifiItem (String ssid,String password) {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int networkId = wifiManager.addNetwork(createWifiInfo(ssid,password,password.length() == 0 ? 0:2));
        boolean sB_save = mWifiManager.saveConfiguration();
        return networkId;
    }

    public boolean connectToSpecSsid(int netId) {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.enableNetwork(netId,true);
    }

    /**
     * 连接wifi
     * @param wcg wifi配置
     */
    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean sB_enable = mWifiManager.enableNetwork(wcgID, true);
    }

    // 断开指定ID的网络
    public void disconnectWifi(boolean b,int netId) {
        if (b){
            mWifiManager.disableNetwork(netId);
        }
        mWifiManager.disconnect();
    }

    /**
     * 释放资源
     */
    public void onClose(){
        try {
            if ( mWiFiReceiver != null ){
                mWiFiReceiver.onClose();
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
        if (mWifiManager != null){
            mWifiManager = null;
            mWifiInfo = null;
        }
        if (mContext!=null){
            mContext = null;
        }
    }

    /**
     * 是否配置过
     * @param SSID 账号
     * @return 配置
     */
    private WifiConfiguration isExist(String SSID) {
        try{
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\""+SSID+"\"")) {
                    return existingConfig;
                }
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public WifiConfiguration createWifiInfo(ScanResult scan, String Password) {
        WifiConfiguration config = new WifiConfiguration();
        config.hiddenSSID = false;
        config.SSID=scan.SSID;
        config.status = WifiConfiguration.Status.ENABLED;
        if (scan.capabilities.contains("WEP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.wepTxKeyIndex = 0;
            config.wepKeys[0] = Password;
        } else if (scan.capabilities.contains("PSK")) {
            config.preSharedKey = "\"" + Password + "\"";
        } else if (scan.capabilities.contains("EAP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.preSharedKey = "\"" + Password + "\"";
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.preSharedKey = null;
            config.wepKeys[0] = "\"" + "\"";
            config.wepTxKeyIndex = 0;
        }
        return config;
    }

    //然后是一个实际应用方法，只验证过没有密码的情况：
    public WifiConfiguration createWifiInfo(String SSID, String password, int type) {

//        Log.v(TAG, "SSID = " + SSID + "## Password = " + password + "## Type = " + type);

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";


        WifiConfiguration tempConfig = this.isExist(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        // 分为三种情况：1没有密码2用wep加密3用wpa加密
        if (type == 0) {// WIFICIPHER_NOPASS
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if (type == 1) {  //  WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.wepKeys[0] = "\"".concat(password).concat("\"");
            config.wepTxKeyIndex = 0;
        } else if (type == 2) {   // WIFICIPHER_WPA
            config.hiddenSSID = true;
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.preSharedKey = "\"".concat(password).concat("\"");

        }

        return config;
    }

    public interface IWifiScanListener{
        void scanSuccess();
        void scanFailure();
    }

    public interface IWifiScanResultListener<T>{
        void scanResult(T scanResult);
    }

}
