package com.kjq.common.utils.network;

/**
 *
 * Created by Administrator on 2018/3/16 0016.
 */

public class DCommFactory {
    private static final DCommFactory ourInstance = new DCommFactory();

    public static DCommFactory getInstance() {
        return ourInstance;
    }

    private DCommFactory() {

    }

    public <T> DComm<T> getDComm(String s_action, T model){
        return getDComm(s_action,null,null, String.valueOf(false),model,null);
    }

    public <T> DComm<T> getDComm(String s_action, String s_actionType, T model){
        return getDComm(s_action,s_actionType,null, String.valueOf(false),model,null);
    }

    public <T> DComm<T> getDCommUserId(String s_action, String s_actionType, String s_userId, T model){
        return getDComm(s_action,s_actionType,s_userId, String.valueOf(false),model,null);
    }

    public <T> DComm<T> getDCommUserId(String s_action, String s_userId, T model){
        return getDComm(s_action,null,s_userId, String.valueOf(false),model,null);
    }

    /*需要发送tcp的目标，用来通知其他用户，true 代表发送共享的人，false 代表不发送，具体id代表发送单个目标*/
    public <T> DComm<T> getDCommOfNotification(String s_action, String s_notificationTarget, T t_model){
        return getDComm(s_action,null,null,s_notificationTarget,t_model,null);
    }

    /*需要发送tcp的目标，用来通知其他用户，true 代表发送共享的人，false 代表不发送，具体id代表发送单个目标*/
    public <T> DComm<T> getDCommOfNotification(String s_action, String s_actionType, String s_notificationTarget, T t_model){
        return getDComm(s_action,s_actionType,null,s_notificationTarget,t_model,null);
    }

    private <T> DComm<T> getDComm(String s_action, String s_actionType, String s_userId, String s_notificationTarget, T t_model, String s_data){
        DComm<T> sTDComm;
        sTDComm = new DComm<>();
        sTDComm.initParam(s_action,s_actionType,s_userId,s_notificationTarget,t_model,s_data);
        return sTDComm;
    }


}
