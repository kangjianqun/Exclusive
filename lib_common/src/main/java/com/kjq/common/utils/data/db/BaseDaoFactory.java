package com.kjq.common.utils.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kjq.common.utils.Utils;


/**
 *
 * Created by Administrator on 2018/3/9 0009.
 */

class BaseDaoFactory {
    private SQLiteDatabase mSQLiteDatabase;

    private static final BaseDaoFactory ourInstance = new BaseDaoFactory();

    public static BaseDaoFactory getInstance() {
        return ourInstance;
    }

    private BaseDaoFactory() {
        Context sContext = Utils.getContext();
//       private String mS_sqlPath = FileUtils.getDataBasePath(sContext,"chuGuan", CValue.DB.DB_NAME);
        try {
//            mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(mS_sqlPath,null);
            mSQLiteDatabase = new DataBaseHelper(sContext).getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized <T> BaseDaoUtils<T> getBaseDaoUtils(Class<T> model){
        BaseDaoUtils<T> sBaseDaoUtils = null;
        try {
            sBaseDaoUtils = BaseDaoUtils.class.newInstance();
            sBaseDaoUtils.init(mSQLiteDatabase,model);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sBaseDaoUtils;
    }


}
