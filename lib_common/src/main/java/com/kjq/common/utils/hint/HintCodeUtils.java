package com.kjq.common.utils.hint;

import android.content.res.Resources;


import com.kjq.common.R;
import com.kjq.common.utils.data.Constant;
import com.kjq.common.utils.network.DHttpReturn;

import org.jetbrains.annotations.NotNull;


/**
 *
 * Created by Administrator on 2018/2/26 0026.
 */

public class HintCodeUtils {

    /**
     * 从服务器返回的代码中解析含义
     * @param dHttpReturn 代码
     */
    @NotNull
    public static String showToastOfCode(@NotNull DHttpReturn dHttpReturn, Resources res){
        int sS_hint = 0;
//        String sS_hintCode = dHttpReturn.getS_hintCode();
//        String sS_commandCode = dHttpReturn.getS_action();
//        String sS_actionType = dHttpReturn.getS_actionType();
//        boolean sB_result = dHttpReturn.isB_result();
//        switch (sS_hintCode){
//            default:
//                sS_hint = R.string.utils_unknown_error;
//                break;
//            case "10000":
//            case "20000":
//                switch (sS_commandCode){
////                    case Constant.Comm.Action.C_101:
////                        sS_hint = sB_result ? R.string.succeed_register : R.string.failure_register;
////                        break;
//                }
//                break;
//        }
//        if (sS_hint == 0){
//            sS_hint = sB_result ? R.string.utils_toast_action_ok : R.string.utils_toast_action_no;
//        }
        return sS_hint == 1 ? "" : res.getString(sS_hint);
    }
}
