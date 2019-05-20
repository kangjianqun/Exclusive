package com.kjq.common.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 * Created by stranger_k on 2017/10/20.
 */

public class DateAndTimeUtil implements DatePickerDialog.OnDateSetListener{
    private Context mContext;

    private static final String TIME_YYYY_MM_DD = "yyyy-mm-dd";
    private TextView mTexV_display;

    // 定义显示时间控件
    private Calendar mCalendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    public String from = "1970-01-01";
    public String to = "2030-01-01";

    private DateAndTimeUtil(){}

    public static DateAndTimeUtil getInstance(){
        return DateAndTimeUtilsHolder.INSTANCE;
    }

    @SuppressLint("StaticFieldLeak")
    private static class DateAndTimeUtilsHolder{
        private static final DateAndTimeUtil INSTANCE = new DateAndTimeUtil();
    }
    
    public void setDateAndTimeUtils(Context context){
        mContext = context;
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR); //获取年
        mMonth = mCalendar.get(Calendar.MONTH);//获取月
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        mHour = mCalendar.get(Calendar.HOUR);
        mMinute = mCalendar.get(Calendar.MINUTE);
    }
    

    /**
     * 返回日期字符串格式(2017-1-1)  去掉时分秒
     * By:Faner 2017.7.6
     */
    public static String GetDateString(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date sDate = formatter.parse(strDate, pos);
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(sDate);
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean DateBiJiao(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1,new ParsePosition(0));
            dt2 = sdf.parse(str2,new ParsePosition(0));
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }
        if (dt1 == null ||dt2 == null){
            return false ;
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    public static boolean DateBiJiaoShiFen(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1,new ParsePosition(0));
            dt2 = sdf.parse(str2,new ParsePosition(0));
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }
        if (dt1 == null ||dt2 == null){
            return false ;
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    //把string转化为float
    public static float convertToFloat(String number, float defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    public static String GetDateShiFenString(String strDate) {
        if (strDate.equals(""))
        {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(strtodate);
    }

    /**
     * 得到当前时间
     * @return 时间
     */
    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }

    /***
     *  获取指定日后 后 dayAddNum 天的 日期
     *  @param day  日期，格式为String："2013-9-3";
     *  @param dayAddNum 增加天数 格式为int;
     *  @return 时间字符串
     */
    public static String getDateAddInt(String day, long dayAddNum) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sDateFormat = new SimpleDateFormat(TIME_YYYY_MM_DD);
        Date sDate = null;
        try {
            sDate = sDateFormat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (sDate==null){
            return null;
        }
        Date newDate2 = new Date(sDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
        return sDateFormat.format(newDate2);
    }

    /**
     *
     * @param view：该事件关联的组件
     * @param myYear：当前选择的年
     * @param monthOfYear：当前选择的月
     * @param dayOfMonth：当前选择的日
     */
    public void onDateSet(DatePicker view, int myYear, int monthOfYear, int dayOfMonth) {
        //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
        mYear = myYear;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        //更新日期
        updateDate();
    }

    //当DatePickerDialog关闭时，更新日期显示
    private void updateDate() {
        //在TextView上显示日期
        String sS_day = mYear + "-" + (mMonth + 1) + "-" + mDay;
        mCalendar.set(mYear, mMonth, mDay);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sDateFormat = new SimpleDateFormat(TIME_YYYY_MM_DD);
        String str = sDateFormat.format(mCalendar.getTime());
        from = str;
        mTexV_display.setText(sS_day);
        mTexV_display.setText(str.substring(0, 10));
    }

    /**
     * 时间转换，将小于10的值自动补0
     * @param value 原值
     * @return 新值
     */
    public static String format(int value){
        return value < 10 ? "0"+ String.valueOf(value) : String.valueOf(value);
    }
}
