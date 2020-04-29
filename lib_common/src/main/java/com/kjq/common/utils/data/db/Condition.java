package com.kjq.common.utils.data.db;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * Created by Administrator on 2018/3/10 0010.
 */

class Condition {
    private String whereClause;
    private String[] whereArgs;

    Condition(ContentValues contentValues){
        ArrayList<String> list = new ArrayList<>();
        StringBuilder sStringBuilder = new StringBuilder();
        Set sSet_keys = contentValues.keySet();
        for (Object sSet_key : sSet_keys) {
            String sS_key = (String) sSet_key;
            try {
                String sS_value = contentValues.get(sS_key).toString();
                if (sS_value != null) {
                    sStringBuilder.append("and ").append(sS_key).append(" =? ");
                    list.add(sS_value);
                }
            }catch (Exception ignored){ }
        }
        String sS = sStringBuilder.substring(0,3);
        if (sS.equalsIgnoreCase("and")){
            sStringBuilder = sStringBuilder.delete(0,3);
        }
        this.whereClause = sStringBuilder.toString();
        this.whereArgs = list.toArray(new String[list.size()]);
    }

    String[] getWhereArgs() {
        return whereArgs;
    }

    String getWhereClause() {
        return whereClause;
    }
}
