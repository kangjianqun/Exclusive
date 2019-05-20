package com.kjq.common.utils.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.databinding.ObservableField;

import com.kjq.common.utils.annotation.DBField;
import com.kjq.common.utils.annotation.DBTable;
import com.kjq.common.utils.data.BooleanUtils;
import com.kjq.common.utils.data.Constant;
import com.kjq.common.utils.data.DataUtils;
import com.kjq.common.utils.data.StringUtils;

/**
 * 数据库
 * Created by Administrator on 2018/3/9 0009.
 */

class BaseDaoUtils<T> implements IBaseDao<T>{
    /* 持有数据库引用 */
    private SQLiteDatabase mDatabase;
    private Class<T> mModelClass;
    private HashMap<String,Field> mCacheMap;
    private String mS_tableName;
    private boolean mB_isInit;

    /**
     * 初始化
     * @param database 数据库
     * @param modelClass 实体类
     * @return 结果
     */
    protected synchronized boolean init(SQLiteDatabase database,Class<T> modelClass){
        if (!mB_isInit){
            mModelClass = modelClass;
            mDatabase = database;
            mS_tableName = modelClass.getAnnotation(DBTable.class).value();
            if (mDatabase == null || !mDatabase.isOpen()){
                return false;
            }
            if (!autoCreateTable()){
                return false;
            }
            mB_isInit = true;
        }

        initCacheMap();

        return mB_isInit;
    }

    /**
     * 自动建表
     * @return 结果
     */
    private boolean autoCreateTable() {
        StringBuilder sStringBuilder = new StringBuilder();
        sStringBuilder.append(" CREATE TABLE IF NOT EXISTS ")
                .append(mS_tableName)
                .append("(id INTEGER PRIMARY KEY AUTOINCREMENT, ");

        Field[] sFields = mModelClass.getDeclaredFields();

        for (Field sField : sFields) {
            DBField sDBField = sField.getAnnotation(DBField.class);
            if (sDBField != null){
                String sS_value = sDBField.columnName();
                String sS_type = sDBField.type();
                if (sS_type.equalsIgnoreCase("_id") || sS_type.equalsIgnoreCase("id")) {
                    continue;
                }
                sStringBuilder.append(sS_value).append(" ").append(sS_type).append(",");
            }
        }

        if (sStringBuilder.charAt(sStringBuilder.length()-1) == ','){
            sStringBuilder.deleteCharAt(sStringBuilder.length()-1);
        }
        sStringBuilder.append(" ) ");
        try {
            this.mDatabase.execSQL(sStringBuilder.toString());
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 初始化映射SQL的关系
     */
    private void initCacheMap(){
        mCacheMap = new HashMap<>();

        /*查询一次空表*/
        String sS_sql = "select * from " + this.mS_tableName+" limit 1,0";
        @SuppressLint("Recycle")
        Cursor sCursor = mDatabase.rawQuery(sS_sql,null);
        /*得到用于映射的值*/
        String[] sS_columnNames = sCursor.getColumnNames();
        Field[] sF_columnFields = mModelClass.getDeclaredFields();

        for (String sS_columnName : sS_columnNames){
            Field sF_result = null;
            for (Field sField : sF_columnFields){
                DBField sDBField =sField.getAnnotation(DBField.class);
                if (sDBField != null){
                    if (sS_columnName.equals(sDBField.columnName())){
                        sF_result = sField;
                        break;
                    }
                }
            }
            if (sF_result != null){
                mCacheMap.put(sS_columnName,sF_result);
            }
        }
        sCursor.close();
    }

    private ContentValues getCValues(T model) {
        ContentValues sValues = new ContentValues();
        for (Map.Entry<String, Field> sFieldEntry : mCacheMap.entrySet()) {
            Field sField = sFieldEntry.getValue();
            String sS_key = sFieldEntry.getKey();
            //剔除 主键id值 保存，由于框架默认设置id为主键自己主动增长
            if (sS_key.equalsIgnoreCase("id") || sS_key.equalsIgnoreCase("_id")) {
                continue;
            }
            sField.setAccessible(true);
            try {
                Object sO = sField.get(model);
                Class sType = sField.getType();
                if (sType == String.class) {
                    String sS_value = (String) sO;
                    sValues.put(sS_key, sS_value);
                } else if (sType == Double.class) {
                    Double sS_value = (Double) sO;
                    sValues.put(sS_key, sS_value);
                } else if (sType == Integer.class) {
                    Integer sS_value = (Integer) sO;
                    sValues.put(sS_key, sS_value);
                } else if (sType == Long.class) {
                    Long sS_value = (Long) sO;
                    sValues.put(sS_key, sS_value);
                } else if (sType == byte[].class) {
                    byte[] sS_value = (byte[]) sO;
                    sValues.put(sS_key, sS_value);
                } else if (sType == ObservableField.class){
                    try {
                        ObservableField sObservableField = (ObservableField) sO;
                        Object sO_value = sObservableField.get();
                        if (sO_value != null && !StringUtils.isEmpty(String.valueOf(sO_value))){
                            sValues.put(sS_key,String.valueOf(sO_value));
                        }
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
                }else {
                    continue;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sValues;
    }

    /**
     * 跳过值为空的键值对
     * @param model
     * @return
     */
    private ContentValues getCValuesSkipEmpty(T model) {
        ContentValues sValues = new ContentValues();
        for (Map.Entry<String, Field> sFieldEntry : mCacheMap.entrySet()) {
            Field sField = sFieldEntry.getValue();
            String sS_key = sFieldEntry.getKey();
            //剔除 主键id值 保存，由于框架默认设置id为主键自己主动增长
            if (sS_key.equalsIgnoreCase("id") || sS_key.equalsIgnoreCase("_id")) {
                continue;
            }
            sField.setAccessible(true);
            try {
                Object sO = sField.get(model);
                DBField sDBField = sField.getAnnotation(DBField.class);
                if (sDBField == null){
                    continue;
                }
                String sType = sDBField.type();
                Class sClass_type = sField.getType();
                if (sO == null){
                    continue;
                }
                if (sClass_type == ObservableField.class){
                    try {
                        ObservableField sObservableField = (ObservableField) sO;
                        Object sO_value = sObservableField.get();
                        if (sO_value != null && !StringUtils.isEmpty(String.valueOf(sO_value))){
                            sValues.put(sS_key,String.valueOf(sO_value));
                        }
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
                }else if (sType.equalsIgnoreCase(Constant.DB.T_STRING)) {
                    String sS_value = sO.toString();
                    sValues.put(sS_key, sS_value);
                } else if (sType.equalsIgnoreCase(Constant.DB.T_DOUBLE)) {
                    Double sS_value = (Double) sO;
                    sValues.put(sS_key, sS_value);
                } else if (sType.equalsIgnoreCase(Constant.DB.T_INTEGER)) {
                    Integer sS_value = (Integer) sO;
                    sValues.put(sS_key, sS_value);
                } else if (sType.equalsIgnoreCase(Constant.DB.T_LONG)) {
                    Long sS_value = (Long) sO;
                    sValues.put(sS_key, sS_value);
                } else if (sType.equalsIgnoreCase(Constant.DB.T_BYTE)) {
                    byte[] sS_value = (byte[]) sO;
                    sValues.put(sS_key, sS_value);
                }else {
                    continue;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sValues;
    }

    @Override
    public long insert(T model) {
        ContentValues sValues = getCValues(model);
        return mDatabase.insert(mS_tableName,null,sValues);
    }

    @Override
    public int delete(T model) {
        Condition sCondition = new Condition(getCValues(model));
        return mDatabase.delete(mS_tableName,sCondition.getWhereClause(),sCondition.getWhereArgs());
    }

    @Override
    public int update(T model,T where) {
        ContentValues sValues = null;
        Condition sCondition = null;
        try {
            sValues = getCValuesSkipEmpty(model);
            sCondition = new Condition(getCValues(where));
        }catch (Exception e){
            e.printStackTrace();
        }
        if (sValues == null || sCondition == null){
            return -1;
        }
        return mDatabase.update(mS_tableName,sValues,sCondition.getWhereClause(),sCondition.getWhereArgs());
    }

    @Override
    public ArrayList<T> query(T where) {
        return query(where,null,null,null);
    }

    @Override
    public ArrayList<T> query(T where, String orderBy) {
        return query(where,orderBy,null,null);
    }

    @Override
    public ArrayList<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        String sS_limit = null;
        if (startIndex != null && limit != null && startIndex > 0 && limit > 0) {
            sS_limit = startIndex + " , " + limit;
        }
        Cursor sCursor;
        if (where != null){
            Condition sCondition = new Condition(getCValues(where));
            sCursor = mDatabase.query(mS_tableName, null, sCondition.getWhereClause(),
                    sCondition.getWhereArgs(), null, null, orderBy, sS_limit);
        }else {
            sCursor = mDatabase.query(mS_tableName, null, null,
                    null, null, null, orderBy, sS_limit);
        }
        ArrayList<T> sL_result = new ArrayList<>();
        if (sCursor != null) {
            while (sCursor.moveToNext()) {
                try {
                    Object sO_item = mModelClass.newInstance();
                    for (Map.Entry<String, Field> sEntry : mCacheMap.entrySet()) {
                        String sS_columnName = sEntry.getKey();
                        Field sField = sEntry.getValue();
                        DBField sDBField = sField.getAnnotation(DBField.class);
                        if (sDBField == null){
                            continue;
                        }
                        int sI_index = sCursor.getColumnIndex(sS_columnName);
                        if (sI_index <= 0){
                            continue;
                        }
                        Class sClass_type = sField.getType();
                        Class sClass_obsField  = sDBField.observableFieldType();
                        String sS_type = sDBField.type();
                        sField.setAccessible(true);
                        Object sO_value;
                        if (sClass_type == ObservableField.class){
                            if (sClass_obsField == String.class){
                                String sS = sCursor.getString(sI_index);
                                sO_value = new ObservableField<>(sS);
                            }else if (sClass_obsField == Boolean.class || sClass_obsField == boolean.class){
                                boolean sB = BooleanUtils.valueOf(sCursor.getString(sI_index));
                                sO_value = new ObservableField<>(sB);
                            }else {
                                continue;
                            }
                        }else {
                            if (sS_type.equalsIgnoreCase(Constant.DB.T_STRING)) {
                                sO_value = DataUtils.objectToObject(sCursor.getString(sI_index),sClass_type);
                            } else if (sS_type.equalsIgnoreCase(Constant.DB.T_DOUBLE)) {
                                sO_value = sCursor.getDouble(sI_index);
                            } else if (sS_type.equalsIgnoreCase(Constant.DB.T_INTEGER)) {
                                sO_value = sCursor.getInt(sI_index);
                            } else if (sS_type.equalsIgnoreCase(Constant.DB.T_LONG)) {
                                sO_value = sCursor.getLong(sI_index);
                            } else if (sS_type.equalsIgnoreCase(Constant.DB.T_BYTE)) {
                                sO_value = sCursor.getBlob(sI_index);
                            } else if (sS_type.equalsIgnoreCase(Constant.DB.T_FLOAT)) {
                                sO_value = sCursor.getFloat(sI_index);
                            } else {
                                continue;
                            }
                        }

                        if (sO_value != null){
                            sField.set(sO_item,sO_value);
                        }
                    }
                    sL_result.add((T) sO_item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
            sCursor.close();
        }
        return sL_result;

    }



}
