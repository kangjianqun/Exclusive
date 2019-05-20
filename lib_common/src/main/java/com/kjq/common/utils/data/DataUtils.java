package com.kjq.common.utils.data;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

/**
 * 数据转换
 * Created by Administrator on 2017/11/24 0024.
 */

public class DataUtils {
    public static final String GB2312 = "gb2312";
    public static final String UTF_8 = "UTF-8";


    public static Object objectToObject(Object value,Class class_type){
        if (value == null){
            return null;
        }

        if (class_type == String.class){
            return value.toString();
        }else if (class_type == Boolean.class || class_type == boolean.class){
            return BooleanUtils.valueOf(value.toString());
        }else {
            return null;
        }
    }


    /**
     * 字节数组转字符串
     * @param bytes 字节数组
     * @param charsetName 字符串编码格式
     * @return 字符串
     * @throws Exception 会转换异常
     */
    public static String bytesToString(byte[] bytes,String charsetName) throws Exception {
        return hex2String(bytes2HexString(bytes),charsetName);
    }

    /**
     * 字节转10进制
     * @param b 字节
     * @return 10进制
     */
    public static int byte2Int(byte b){
        return (int) b;
    }

    /**
     * 10进制转字节
     * @param i 10进制
     * @return 字节
     */
    public static byte int2Byte(int i){
        return (byte) i;
    }

    /**
     * 对每一个字节，先和0xFF做与运算，然后使用Integer.toHexString()函数，如果结果只有1位，需要在前面加0。
     * 字节数组转16进制字符串
     * @param b 字节数组
     * @return 16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        StringBuilder r = new StringBuilder();

        for (byte aB : b) {
            String hex = Integer.toHexString(aB & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r.append(hex.toUpperCase());
        }

        return r.toString();
    }

    /**
     * 16进制字符串转字节数组
     * 这个比较复杂，每一个16进制字符是4bit，一个字节是8bit，所以两个16进制字符转换成1个字节，对于第1个字符，转换成byte以后左移4位，然后和第2个字符的byte做或运算，这样就把两个字符转换为1个字节。
     *
     *字符转换为字节
     * @param c 字符
     * @return 字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {
        if ((hex == null) || (hex.equals(""))){
            return null;
        }
        else if (hex.length()%2 != 0){
            return null;
        }
        else{
            hex = hex.toUpperCase();
            int len = hex.length()/2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i=0; i<len; i++){
                int p=2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p+1]));
            }
            return b;
        }
    }

    /**
     * 直接使用new String()。
     * 字节数组转字符串
     */
    private static String bytes2String(byte[] b, String charsetName) throws Exception {
        return new String (b,charsetName);
    }

     /**
     *字符串转字节数组，直接使用getBytes()。
     * @param s 字符串
     * @return 字节数组
     */
    private static byte[] string2Bytes(String s){
        return s.getBytes();
    }

    /**
     *  先转换成byte[]，再转换成字符串。
     *  16进制字符串转字符串
     */
    public static String hex2String(String hex,String charsetName) throws Exception{
        return bytes2String(hexString2Bytes(hex),charsetName);
    }

    /**
     * 先转换为byte[]，再转换为16进制字符串。
     * 字符串转16进制字符串
     */
    public static String string2HexString(String s) throws Exception{
        return bytes2HexString(string2Bytes(s));
    }

    public static int ByteArrayToInt(byte b[]) throws Exception {

        //        int bytesLength = bytes.length;
//
//        int sI_currentLen = 0;
//        while (sI_currentLen < bytesLength){
//            byte sB_one = sI_currentLen >= bytesLength ? 0 : bytes[sI_currentLen];
//            byte sB_two = sI_currentLen+1 >= bytesLength ? 0 : bytes[sI_currentLen+1];
//            byte sB_three = sI_currentLen+2 >= bytesLength ? 0 : bytes[sI_currentLen+2];
//            byte sB_four = sI_currentLen+3 >= bytesLength ? 0 : bytes[sI_currentLen+3];
//            sAL_int.add(convertFourBytesToInt1(sB_one,sB_two,sB_three,sB_four));
//            sI_currentLen+=4;
//        }

        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        DataInputStream dis= new DataInputStream (buf);
        return dis.readInt();
    }

    public static int convertFourBytesToInt1 (byte b1, byte b2, byte b3, byte b4){
        return (b4 << 24) | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {(byte)(a & 0xFF),(byte) ((a >> 8) & 0xFF),(byte) ((a >> 16) & 0xFF),(byte) ((a >> 24) & 0xFF)
        };
    }

    public static ArrayList<Integer> byteArrayTwoIntArray(byte[] bytes){
        ArrayList<Integer> sAL_int = new ArrayList<>();
        for (byte sB : bytes){
            sAL_int.add((sB & 0xff));
        }
        return sAL_int;
    }

    public static byte[] intArrayToByteArray(JSONArray array){
        int sI_len = array.length();
        byte[] sBytes = new byte[sI_len];
        for (int i = 0; i < sI_len; i++) {
            try {
                sBytes[i] = (byte) (array.getInt(i) & 0xff);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sBytes;
    }
}
