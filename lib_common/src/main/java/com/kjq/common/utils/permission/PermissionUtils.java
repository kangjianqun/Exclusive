package com.kjq.common.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自我检查权限
 *
 */


public class PermissionUtils {
    private static final String TAG = "myLog";
    public static final int CODE_CAMERA = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_ACCESS_FINE_LOCATION = 4;
    public static final int CODE_ACCESS_COARSE_LOCATION = 5;
    public static final int CODE_READ_EXTERNAL_STORAGE = 6;
    public static final int CODE_MULTI_PERMISSION = 100;


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 0x11) {
//            // If request is cancelled, the result arrays are empty.
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                Log.i("CMCC", "权限被允许");
//            } else {
////                Log.i("CMCC", "权限被拒绝");
//            }
//        }
//    }

//    group:android.permission-group.CONTACTS
//    permission:android.permission.WRITE_CONTACTS
//    permission:android.permission.GET_ACCOUNTS
//    permission:android.permission.READ_CONTACTS
//
//    group:android.permission-group.PHONE
//    permission:android.permission.READ_CALL_LOG
//    permission:android.permission.READ_PHONE_STATE
//    permission:android.permission.CALL_PHONE
//    permission:android.permission.WRITE_CALL_LOG
//    permission:android.permission.USE_SIP
//    permission:android.permission.PROCESS_OUTGOING_CALLS
//    permission:com.android.voicemail.permission.ADD_VOICEMAIL
//
//    group:android.permission-group.CALENDAR
//    permission:android.permission.READ_CALENDAR
//    permission:android.permission.WRITE_CALENDAR
//
//    group:android.permission-group.CAMERA
//    permission:android.permission.CAMERA
//
//    group:android.permission-group.SENSORS
//    permission:android.permission.BODY_SENSORS
//
//    group:android.permission-group.LOCATION
//    permission:android.permission.ACCESS_FINE_LOCATION
//    permission:android.permission.ACCESS_COARSE_LOCATION
//
//    group:android.permission-group.STORAGE
//    permission:android.permission.READ_EXTERNAL_STORAGE
//    permission:android.permission.WRITE_EXTERNAL_STORAGE
//
//    group:android.permission-group.MICROPHONE
//    permission:android.permission.RECORD_AUDIO
//
//    group:android.permission-group.SMS
//    permission:android.permission.READ_SMS
//    permission:android.permission.RECEIVE_WAP_PUSH
//    permission:android.permission.RECEIVE_MMS
//    permission:android.permission.RECEIVE_SMS
//    permission:android.permission.SEND_SMS
//    permission:android.permission.READ_CELL_BROADCASTS

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String CHANGE_WIFI_STATE = Manifest.permission.CHANGE_WIFI_STATE;
    public static final String ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE;
    public static final String CHANGE_WIFI_MULTICAST_STATE = Manifest.permission.CHANGE_WIFI_MULTICAST_STATE;

    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    private static final String[] mPermissions = {
            CAMERA,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
            CHANGE_WIFI_STATE,
            ACCESS_WIFI_STATE,
            CHANGE_WIFI_MULTICAST_STATE,
    };

    /**
     * 检查权限
     */
    @Contract("null, _ -> true")
    public static boolean checkPermission(Activity activity, ArrayList<String> requestList){
        if (activity == null||Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        if (requestList.size() <= 0 || requestList.size() > mPermissions.length){
            return false;
        }
        String [] sSList = new String[requestList.size()];
        for (int i=0;i<requestList.size();i++){
            sSList[i] = requestList.get(i);
        }

        List<String> sL_Permissions = getNoGrantedPermission(activity,sSList);

        if (sL_Permissions != null && sL_Permissions.size() == 0) {
            return true;
        }else {
            requestPermission(activity,sL_Permissions);
            return false;
        }
    }

//    private final int SDK_PERMISSION_REQUEST = 127;@TargetApi(23)
//      请求权限
    private static void requestPermission(Activity activity, List<String> requestList){
        if (requestList != null && requestList.size() > 0){
            ActivityCompat.requestPermissions(activity,requestList.toArray(
                    new String[requestList.size()]),CODE_MULTI_PERMISSION);
        }
//        for (int requestCode:requestCodeList){
//            String permissionName = mPermissions[requestCode];
//            if (ContextCompat.checkSelfPermission(activity,permissionName)!= PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(activity,new String[]{permissionName},1);
//            }
//        }
    }

//      批量请求权限
    public static void requestAllPermissions(Activity activity){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        List<String> sL_Permissions = getNoGrantedPermission(activity,mPermissions);
        if (sL_Permissions != null && sL_Permissions.size() > 0){
            ActivityCompat.requestPermissions(activity,sL_Permissions.toArray(
                    new String[sL_Permissions.size()]),CODE_MULTI_PERMISSION);
        }
    }

    @Nullable
    private static ArrayList<String> getNoGrantedPermission(Activity activity, @NotNull String[] sSList) {
        ArrayList<String> permissions = new ArrayList<>();
        for (String requestPermission : sSList) {
            //TODO checkSelfPermission
            int checkSelf = -1;
            try {
                checkSelf = ContextCompat.checkSelfPermission(activity,requestPermission);
            } catch (RuntimeException e) {
//                ToastUtils.showShort(activity,"please open those permission");
//                Log.e(TAG, "RuntimeException:" + e.getMessage());
                return null;
            }
            if (checkSelf != PackageManager.PERMISSION_GRANTED) {
//                Log.i(TAG, "getNoGrantedPermission ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED:" + requestPermission);
                permissions.add(requestPermission);
            }
        }
        return permissions;
    }
}
