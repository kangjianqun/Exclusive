package com.kjq.common.utils.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.kjq.common.utils.annotation.DBField;
import com.kjq.common.utils.annotation.DBTable;

import java.lang.reflect.Field;

/**
 * 数据库Helper类，必须继承自 SQLiteOpenHelper
 * 当一个继承自 SQLiteOpenHelper 后需要复写两个方法，分别是 onCreate()  和 onUpgrade()
 * onCreate()： onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
 * onUpgrade()：onUpGrade是在数据库版本升级的时候调用的，主要用来改变表结构
 *
 *
 *  数据库帮助类要做的事情特别简单：
 *  1、复写onCreate()  和 onUpgrade()方法
 *  2、在这两个方法里面填写相关的sql语句
 *
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "device_data"; //数据库名称
    private static final int DB_VERSION = 1; //数据库版本
    /**
     * 参数说明：
     *
     * 第一个参数： 上下文
     * 第二个参数：数据库的名称
     * 第三个参数：null代表的是默认的游标工厂
     * 第四个参数：是数据库的版本号  数据库只能升级,不能降级,版本号只能变大不能变小
     */
    DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
     * 当数据库第一次被创建的时候调用的方法,适合在这个方法里面把数据库的表结构定义出来.
     * 所以只有程序第一次运行的时候才会执行
     * 如果想再看到这个函数执行，必须写入程序然后重新安装这个app
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

//        String sS = autoCreateTable(MHardware.class);
//        sqLiteDatabase.execSQL(sS);
    }

    private<T> String autoCreateTable(Class<T> mModelClass) {
        String sS_tableName = mModelClass.getAnnotation(DBTable.class).value();
        StringBuilder sStringBuilder = new StringBuilder();
        sStringBuilder.append(" CREATE TABLE IF NOT EXISTS ")
                .append(sS_tableName)
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
        return sStringBuilder.toString();
    }

    /**
     * 当数据库更新的时候调用的方法（super(context, "itheima.db", null, 2); ）
     * 这个要显示出来得在上面的super语句里面版本号发生改变时才会 打印
     * 注意，数据库的版本号只可以变大，不能变小，假设我们当前写的版本号是3，运行，然后又改成1，运行则报错。不能变小
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
