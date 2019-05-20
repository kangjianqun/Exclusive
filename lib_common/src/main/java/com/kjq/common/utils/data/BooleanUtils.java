package com.kjq.common.utils.data;

public class BooleanUtils {
    public static boolean valueOf(String s_data){
        boolean sB = false;
        if (s_data == null){
            return false;
        }
        if (s_data.equalsIgnoreCase("true")){
            sB = true;
        }else if (s_data.equalsIgnoreCase("yes")){
            sB = true;
        }else if (s_data.equalsIgnoreCase("ok")){
            sB = true;
        }
//        else if (s_data.equalsIgnoreCase("false")){
//            sB = false;
//        }
//        else if (s_data.equalsIgnoreCase("no")){
//            sB = false;
//        }
        return sB;
    }

    public static boolean valueOf(int i_data){
        return i_data == 1;
    }
}
