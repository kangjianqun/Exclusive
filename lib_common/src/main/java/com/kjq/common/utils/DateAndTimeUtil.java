package com.kjq.common.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.kjq.common.R;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具
 * Created by stranger_k on 2017/10/20.
 */

public class DateAndTimeUtil extends SimpleDateFormat{

    private static Locale locale = Locale.CHINA;
    private static String weeks[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private static String moments[] = {"中午", "凌晨", "早上", "下午", "晚上"};
    public static final String TIME_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String TUNE_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    private String mFormat = "%s";

    public String from = "1970-01-01";
    public String to = "2030-01-01";

    public DateAndTimeUtil() {
        this("%s", "yyyy年", "M月d日", "HH:mm");
    }

    public DateAndTimeUtil(String format) {
        this();
        this.mFormat = format;
    }


    public DateAndTimeUtil(String yearFormat, String dateFormat, String timeFormat) {
        super(String.format(locale, "%s %s %s", yearFormat, dateFormat, timeFormat), locale);
    }

    public DateAndTimeUtil(String format, String yearFormat, String dateFormat, String timeFormat) {
        this(yearFormat, dateFormat, timeFormat);
        this.mFormat = format;
    }

    @Override
    public StringBuffer format(@NonNull Date date, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        toAppendTo = super.format(date, toAppendTo, pos);

        Calendar otherCalendar = calendar;
        Calendar todayCalendar = Calendar.getInstance();

        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);

        String[] times = toAppendTo.toString().split(" ");
        String moment = hour == 12 ? moments[0] : moments[hour / 6 + 1];
        String timeFormat = moment + " " + times[2];
        String dateFormat = times[1] + " " + timeFormat;
        String yearFormat = times[0] + dateFormat;
        toAppendTo.delete(0, toAppendTo.length());

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        toAppendTo.append(timeFormat);
                        break;
                    case 1:
                        toAppendTo.append("昨天 ");
                        toAppendTo.append(timeFormat);
                        break;
                    case 2:
                        toAppendTo.append("前天 ");
                        toAppendTo.append(timeFormat);
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                toAppendTo.append(weeks[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1]);
                                toAppendTo.append(' ');
                                toAppendTo.append(timeFormat);
                            } else {
                                toAppendTo.append(dateFormat);
                            }
                        } else {
                            toAppendTo.append(dateFormat);
                        }
                        break;
                    default:
                        toAppendTo.append(dateFormat);
                        break;
                }
            } else {
                toAppendTo.append(dateFormat);
            }
        } else {
            toAppendTo.append(yearFormat);
        }

        int length = toAppendTo.length();
        toAppendTo.append(String.format(locale, mFormat, toAppendTo.toString()));
        toAppendTo.delete(0, length);
        return toAppendTo;
    }

    public static Date getDate(long time){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sDateFormat = new SimpleDateFormat(TUNE_Y_M_D_H_M_S);
        return sDateFormat.parse(getDateToString(time,TUNE_Y_M_D_H_M_S),new ParsePosition(0));
    }

    /**
     * 日期转为汉字.
     * 把日期转换成汉字 dateToChinese("2002/01/01","/") out 二零零二年一月一日
     * 或dateToChinese("2002-01-01","-") out 二零零二年一月一日
     *
     * @param sDate 日期字符串
     * @param split 分隔符
     * @return 中文日期字符串
     * @since BASE 0.1
     */

    public static String dateToChinese(String sDate, String split) {
        String[] tmpArr = sDate.split(split);
        String[] dArr = {
                "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        for (int i = 0; i < 10; i++) {
            Integer x = i;
            String temp = x.toString();
            tmpArr[0] = tmpArr[0].replaceAll(temp, dArr[i]);
        }
        tmpArr[0] = tmpArr[0] + "年";
        if (tmpArr[1].length() == 1) {
            tmpArr[1] = dArr[Integer.parseInt(tmpArr[1])] + "月";
        }
        else {
            if (tmpArr[1].substring(0, 1).equals("0")) {
                tmpArr[1] =
                        dArr[Integer.parseInt(tmpArr[1].substring(tmpArr[1].length() - 1,
                                tmpArr[1].length()))]
                                + "月";
            }
            else {
                tmpArr[1] =
                        "十"
                                +
                                dArr[Integer.parseInt(tmpArr[1].substring(tmpArr[1].length() - 1,
                                        tmpArr[1].length()))]
                                + "月";
                tmpArr[1] = tmpArr[1].replaceAll("零", "");
            }

        }
        if (tmpArr[2].length() == 1) {
            tmpArr[2] = dArr[Integer.parseInt(tmpArr[2])] + "日";
        }
        else {
            if (tmpArr[2].substring(0, 1).equals("0") ) {
                tmpArr[2] =
                        dArr[Integer.parseInt(tmpArr[2].substring(tmpArr[2].length() - 1,
                                tmpArr[2].length()))]
                                + "日";
            }else if (tmpArr[2].substring(0,1).equals("1")){
                tmpArr[2] = "十" +
                        dArr[Integer.parseInt(tmpArr[2].substring(tmpArr[2].length() - 1,
                                tmpArr[2].length()))]
                                + "日";
                tmpArr[2] = tmpArr[2].replaceAll("零", "");
            }
            else {
                tmpArr[2] =
                        dArr[Integer.parseInt(tmpArr[2].substring(0, 1))]
                                +
                                "十"
                                +
                                dArr[Integer.parseInt(tmpArr[2].substring(tmpArr[2].length() - 1,
                                        tmpArr[2].length()))]
                                + "日";
                tmpArr[2] = tmpArr[2].replaceAll("零", "");
            }
        }
        return tmpArr[0] + tmpArr[1] + tmpArr[2];
    }

    /**
     * 时间差
     * @param time
     * @return
     */
    public static String timeDifference(long time){
        long minute = 60 * 1000;// 1分钟
        long hour = 60 * minute;// 1小时
        long day = 24 * hour;// 1天
        long month = 31 * day;// 月
        long year = 12 * month;// 年

        Date sDate = getDate(time);
        long sL = new Date().getTime() - sDate.getTime();
        long r;
//        if (sL > year) {
//            r = (sL / year);
//            return r + "年前";
//        }
//        if (sL > month) {
//            r = (sL / month);
//            return r + "个月前";
//        }
        if (sL > day) {
            r = (sL / day);
            return r + "天前";
        }
        if (sL > hour) {
            r = (sL / hour);
            return r + "小时前";
        }
        if (sL > minute) {
            r = (sL / minute);
            return r + "分钟前";
        }else {
            return "刚刚";
        }
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @return
     */
    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(TIME_YYYY_MM_DD);
        return format.format(date);
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 时间戳转换成字符窜 非毫秒
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateNoMillisecondToString(long milSecond, String pattern) {
        Date date = new Date(milSecond * 1000);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 获取年
     * @return
     */
    public static int getYear(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.YEAR);
    }
    /**
     * 获取月
     * @return
     */
    public static int getMonth(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.MONTH)+1;
    }
    /**
     * 获取日
     * @return
     */
    public static int getDay(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.DATE);
    }
    /**
     * 获取时
     * @return
     */
    public static int getHour(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.HOUR);
    }

    /**
     * 获取分
     * @return
     */
    public static int getMinute(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.MINUTE);
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

    public static String cal(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        return h + ":" + d + ":" + s ;
    }

    /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return long[] 返回值为：{天, 时, 分, 秒} 
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;

            } else {
                diff = time1 - time2;

            }
//            day = diff / (24 * 60 * 60 * 1000);
//            hour = (diff / (60 * 60 * 1000) - day * 24);
//            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000));
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new long[]{hour, min, sec};
    }
    /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return long[] 返回值为：{天, 时, 分, 秒} 
     */
    public static long[] getDistanceTimes(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
//            day = diff / (24 * 60 * 60 * 1000);
//            hour = (diff / (60 * 60 * 1000) - day * 24);
//            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000));
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new long[]{hour, min, sec};
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
        @SuppressLint("SimpleDateFormat")
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

    @SuppressLint("SimpleDateFormat")
    public static boolean DateBiJiaoShiFen(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.getContext().getString(R.string.common_time_y_m_d_h_m));
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

    @SuppressLint("SimpleDateFormat")
    public static String GetDateShiFenString(String strDate) {
        if (strDate.equals("")) {
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
    public static String getDateAddInt(String day, int dayAddNum) {
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
        return getDateAddSubtract(TIME_YYYY_MM_DD,sDate.getTime(),dayAddNum);
    }

    public static String getDateAddSubtract(String pattern,long time,int dayNum){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        Date newDate2 = new Date(time + dayNum * 24 * 60 * 60 * 1000);
        return sDateFormat.format(newDate2);
    }

    /**
     * 时间转换，将小于10的值自动补0
     * @param value 原值
     * @return 新值
     */
    public static String format(int value){
        return value < 10 ? "0"+ String.valueOf(value) : String.valueOf(value);
    }

    /**
     * 获取指定时间的时间戳
     * @param date 天数 正数是加，负数是减
     * @param hourOfDay 小时
     * @param minute 分钟
     * @param second 秒
     * @return 返回时间戳
     */
    public static long getScheduleTime(boolean isMillisecond,Integer date,Integer hourOfDay,Integer minute,Integer second){
        Calendar calendar = Calendar.getInstance();
        if (date != null){
            calendar.add(Calendar.DATE,date);
        }
        if (hourOfDay != null){
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        }
        if (minute != null){
            calendar.set(Calendar.MINUTE, minute);
        }
        if (second != null){
            calendar.set(Calendar.SECOND, second);
        }
        return isMillisecond ? calendar.getTimeInMillis() : calendar.getTimeInMillis() / 1000;
    }
}
