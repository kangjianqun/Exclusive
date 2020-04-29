package com.kjq.common.utils.data;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.kjq.common.utils.Utils;

public class ClipboardUtil {
    public static boolean save(String msg){
        ClipboardManager cmb = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(null, msg)); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        return true;
    }
}
