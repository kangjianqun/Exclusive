package com.kjq.common.utils.data;

import android.os.Environment;


public class Constant {
    public static class Service{
        /*C_代表是TCP服务器需要的key*/
        public static final String Local_TCP_IP = "192.168.1.192";
        public static final String Remote_TCP_IP = "47.92.110.230";
        public static final String REMOTE_TCP_PORT = "9999";

        public class Action{
            public static final String A_CONNECTIVITY_CHANG = "android.net.conn.CONNECTIVITY_CHANGE";

            /*有数据发送行为*/
            public static final String A_PUSH_DATA = "chuGuan.data.push.action";
            /*网络状态改变行为*/
            public static final String A_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
            /*需要给发送服务器行为*/
            public static final String A_SEND_TCP = "action_send_tcp";
            /*需要给发送UDP行为*/
            public static final String A_SEND_UDP = "action_send_udp";
            /*验证TCP是否连接*/
            public static final String A_CHECK_TCP_CONNECT = "action_check_tcp_connect";
            /*自己断开重连*/
            public static final String A_RECONNECTION = "action_reconnection";
            /*UDPAndTCP发送行为*/
            public static final String A_SEND_UDP_TCP = "action_send_udp_tcp";
            /*UDP扫描*/
            public static final String A_UDP_SCAN = "action_udp_scan";
        }
    }

    /**
     *  SharedPreferences
     *
     */
    public static class SPKey {

        public static final String K_FIRST_ROOM = "firstRoom";
        public static final String K_SCENE_NAME = "sceneName";
        public static final String K_AIR_DATA_NAME = "airInfrared";
        public static class Infrared{

        }
    }

    public static class AppInfo{
        public static final String TAG = "myLog";
        //        https://www.chuguansmart.com/  http://39.106.22.144:20000/
        public static final String SERVICE_PATH = "https://www.chuguansmart.com/";
        public static final int TITLE_HEIGHT = 48;

        public static final String PRIMARY_CHANNEL = "default";

        public static int W = 0;
        public static int H = 0;
    }

    public enum NetState {
        WIFI, MOBILE
    }

    public static class DB{
        public static final String DB_ASC = "ASC";
        public static final String DB_DESC = "DESC";
        public static final String DB_NAME = "chuGuan.db";
        public static final String DB_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ DB_NAME;
        //        TEXT
//        INTEGER
//        DOUBLE
//        BIGINT long
//        BLOB 完全根据它的输入存储
//        Boolean
        public static final String T_STRING = "TEXT";
        public static final String T_INTEGER = "INTEGER";
        public static final String T_DOUBLE = "DOUBLE";
        public static final String T_LONG = "BIGINT";
        public static final String T_BYTE = "BLOB";
        public static final String T_FLOAT = "FLOAT";

        public static class TB{
            public static final String TB_MODULE = "tb_module";
            public static final String TB_HARDWARE = "tb_hardware";
            public static final String TB_INFRARED = "tb_infrared";
            public static final String TB_INFRARED_KEY = "tb_infraredKey";
            public static final String TB_INFRARED_KEY_EX = "tb_infraredKeyEx";
        }

        public static class Field{
            public static class User{

            }
        }
    }

    /* 通信用的命令 */
    public static class Comm{

        public enum SendType{
            TCP,UDP,TCP_AND_UDP
        }

        public static final String S_END = "/r/n";

        public static class Key{

            public static final String K_HARDWARE_COMMAND = "deviceCommand";//传递给硬件用的
            public static final String K_HARDWARE = "hardware";//服务器单个硬件信息

            public static final String K_ACTION = "action";
            public static final String K_ACTION_TYPE = "actionType";
            public static final String K_RESULT = "result";
            public static final String K_MESSAGE_ID = "messageId";
            public static final String K_HINT_CODE = "resultCode";
            public static final String K_RESULT_DATA = "resultData";
            public static final String K_RESULT_MESSAGE = "resultMessage"; // tcp
            public static final String K_NOTIFICATION_ACTION = "notificationAction"; //用于服务器转发消息给其他用户，其他用户来识别消息的目的。
            public static final String K_NOTIFICATION_TARGET = "notificationHolder"; // 单个发送目标
            public static final String K_UNIQUE_ID = "uniqueId";
            public static final String K_HOLDER = "holder";

            public static final String K_ID = "id";
            public static final String K_COMMAND_PARAM = "data";

            public static final String K_ACCOUNT = "account";
            public static final String K_PASSWORD = "password";

            public static final String K_HARDWARE_ID = "hardwareId";

        }

        public static class URL{
            public static final String U_User = AppInfo.SERVICE_PATH+"api/UserHandler.ashx";
            public static final String U_HARDWARE = AppInfo.SERVICE_PATH + "api/HardwareHandler.ashx";
        }

        public static class Hint{

            public static final String H_20000 = "20000"; // 通用成功
            public static final String H_10000 = "10000"; // 通用错误
            public static final String H_10008 = "10008"; // 模块过期
            public static final String H_10009 = "10009"; // 设备过期
            public static final String H_10001 = "10001";//登录状态过期，或者被顶掉
        }

        public static class Action {
            public static final String C_000 = "000"; // 异常问题
            public static final String C_100 = "100"; // 生命周期

            public static final String C_104 = "deviceOperation"; //给模块发送数据

            public static final String A_HEARTBEAT = "heartbeat";
        }

        public static class ActionType{
            public static final String T_GET_KEY_CODE = "getKeyCode";
            public static final String T_BIND = "bind";
            public static final String T_GET_ALL = "getAll";
        }
    }

    /* 路由配置 */
    public static class Route{
        public static class Path{
            public static final String SOCKET_TEST = "/kang/socketTest";

        }
    }
}
