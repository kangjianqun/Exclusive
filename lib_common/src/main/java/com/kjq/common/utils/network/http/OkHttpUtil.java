package com.kjq.common.utils.network.http;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 *
 * Created by Administrator on 2018/2/23 0023.
 */

public class OkHttpUtil {
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtil() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    @Contract(pure = true)
    public static OkHttpUtil getInstance(){
        return OkHttpUtilsHolder.INSTANCE;
    }

    private static class OkHttpUtilsHolder{
        private static final OkHttpUtil INSTANCE = new OkHttpUtil();
    }

    /**
     * 同步的Get请求
     *
     * @param url 网址
     * @return Response
     */
    @NotNull
    private Response getOfSync(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 同步的Get请求
     *
     * @param url 网址
     * @return 字符串
     */
    private String getSyncString(String url) throws IOException {
        Response execute = getOfSync(url);
        return execute.body().string();
    }

    /**
     * 异步的get请求
     *
     * @param url 网址
     * @param callback 回调
     */
    private void getOfAsync(String url, final StringCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }


    /**
     * 同步的Post请求
     *
     * @param url 网址
     * @param requestBody post的参数
     * @return 响应
     */
    private Response postOfSync(String url, RequestBody requestBody) throws IOException {
        Request request = buildPostRequest(url, requestBody);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 同步的Post请求
     *
     * @param url 网址
     * @param requestBody post的参数
     * @return 字符串
     */
    private String _postAsString(String url, RequestBody requestBody) throws IOException {
        Response response = postOfSync(url, requestBody);
        return response.body().string();
    }

    /**
     * 异步的post请求
     *
     * @param url 网址
     * @param callback 回调
     * @param requestBody 请求体 参数
     */
    private void postOfAsync(String url,RequestBody requestBody, final StringCallback callback) {
        Request request = buildPostRequest(url, requestBody);
        deliveryResult(callback, request);
    }

    /**
     * 异步的put请求
     *
     * @param url 网址
     * @param callback 回调
     * @param requestBody 请求体 参数
     */
    private void putOfAsync(String url,RequestBody requestBody,final  StringCallback callback){
        Request request = buildPutRequest(url, requestBody);
        deliveryResult(callback,request);
    }

    /**
     * 异步的patch请求
     *
     * @param url 网址
     * @param callback 回调
     * @param requestBody 请求体 参数
     */
    private void patchOfAsync(String url,RequestBody requestBody,final  StringCallback callback){
        Request request = buildPatchRequest(url, requestBody);
        deliveryResult(callback,request);
    }

    /**
     * 异步的delete请求
     *
     * @param url 网址
     * @param callback 回调
     * @param requestBody 请求体 参数
     */
    private void deleteOfAsync(String url,RequestBody requestBody,final  StringCallback callback){
        Request request = buildDeleteRequest(url, requestBody);
        deliveryResult(callback,request);
    }


//    /**
//     * 异步的post请求
//     *
//     * @param url
//     * @param callback
//     * @param params
//     */
//    private void postOfAsync(String url, final StringCallback callback, Map<String, String> params)
//    {
//        Param[] paramsArr = map2Params(params);
//        Request request = buildPostRequest(url, paramsArr);
//        deliveryResult(callback, request);
//    }

    /**
     * 同步基于post的文件上传
     *
     * @return Response
     */
    private Response postOfSync(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response postOfSync(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response postOfSync(String url, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url 网址
     * @param callback 回调
     * @param files 文件路径
     * @param fileKeys 为file对应的name，这个name不是文件的名字；对应于http中的
     * <input type="file" name="mFile" > 对应的是name后面的值，即mFile.
     * @throws IOException 异常
     */
    private void postOfAsync(String url, StringCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url 网址
     * @param callback 回调
     * @param file 文件路径
     * @param fileKey 为file对应的name，这个name不是文件的名字；对应于http中的
     * <input type="file" name="mFile" > 对应的是name后面的值，即mFile.
     * @throws IOException 异常
     */
    private void postOfAsync(String url, StringCallback callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url 网址
     * @param callback 回调
     * @param file 路径
     * @param fileKey 为file对应的name，这个name不是文件的名字；对应于http中的
     * <input type="file" name="mFile" > 对应的是name后面的值，即mFile.
     * @param params 参数
     * @throws IOException 异常处理
     */
    private void postOfAsync(String url, StringCallback callback, File file, String fileKey, Param... params) throws IOException{
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步下载文件
     *
     * @param url 网址
     * @param destFileDir 本地文件存储的文件夹
     * @param callback 回调
     */
    private void downloadOfAsync(final String url, final String destFileDir, final StringCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessStringCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException ignored) {

                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException ignored) {

                    }
                }
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }


    //*************对外公布的方法************

    @NotNull
    public static Response getSync(String url) throws IOException {
        return getInstance().getOfSync(url);
    }

    @NotNull
    public static String getAsString(String url) throws IOException {
        return getInstance().getSyncString(url);
    }

    public static void getAsync(String url, StringCallback callback) {
        getInstance().getOfAsync(url, callback);
    }

    public static void getAsync(String url,RequestBody requestBody,StringCallback callback){
        String sS_url = urlFormRequestBody(url, requestBody);
        getInstance().getOfAsync(sS_url, callback);
    }

    public static void putAsync(String url, RequestBody requestBody, final StringCallback callback) {
        getInstance().putOfAsync(url, requestBody,callback);
    }

    public static void patchAsync(String url, RequestBody requestBody, final StringCallback callback) {
        getInstance().patchOfAsync(url, requestBody,callback);
    }

    public static void deleteAsync(String url, RequestBody requestBody, final StringCallback callback) {
        getInstance().deleteOfAsync(url, requestBody,callback);
    }

    @NotNull
    public static Response postSync(String url, RequestBody requestBody) throws IOException {
        return getInstance().postOfSync(url, requestBody);
    }

    @NotNull
    public static String postAsString(String url, RequestBody requestBody) throws IOException {
        return getInstance()._postAsString(url, requestBody);
    }

    public static void postAsync(String url, RequestBody requestBody, final StringCallback callback) {
        getInstance().postOfAsync(url, requestBody,callback);
    }

    @NotNull
    public static Response postSync(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance().postOfSync(url, files, fileKeys, params);
    }

    @NotNull
    public static Response postSync(String url, File file, String fileKey) throws IOException {
        return getInstance().postOfSync(url, file, fileKey);
    }

    @NotNull
    public static Response postSync(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance().postOfSync(url, file, fileKey, params);
    }

    public static void postAsync(String url, StringCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        getInstance().postOfAsync(url, callback, files, fileKeys, params);
    }


    public static void postAsync(String url, StringCallback callback, File file, String fileKey) throws IOException {
        getInstance().postOfAsync(url, callback, file, fileKey);
    }


    public static void postAsync(String url, StringCallback callback, File file, String fileKey, Param... params) throws IOException {
        getInstance().postOfAsync(url, callback, file, fileKey, params);
    }

    public static void downloadAsync(String url, String destDir, StringCallback callback) {
        getInstance().downloadOfAsync(url, destDir, callback);
    }

    //****************************
    /**
     *  拼接 url 将参数拼接到url后面用于get请求
     * @param url 原url
     * @param requestBody 参数
     * @return 拼接完整的url 用于get
     */
    private static String urlFormRequestBody(String url,RequestBody requestBody){
        StringBuilder sS_url = new StringBuilder(url);

        if (requestBody != null){
            FormBody sFormBody = (FormBody) requestBody;
            if (sFormBody.size() > 0){
                sS_url.append("?");
                int sI_count = sFormBody.size();
                for (int i = 0; i < sI_count; i++) {
                    if (i != 0){
                        sS_url.append("&");
                    }
                    sS_url.append(sFormBody.encodedName(i)).append("=").append(sFormBody.encodedValue(i));
                }
            }
        }

        return sS_url.toString();
    }

    @NotNull
    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params) {
        params = validateParam(params);
        MultipartBody.Builder sMBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Param param : params) {
            sMBodyBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
//                TODO 根据文件名设置contentType
                sMBodyBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = sMBodyBuilder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    @Contract(value = "null -> new; !null -> param1", pure = true)
    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 结果
     * @param callback 字符串形式回调
     * @param request 请求
     */
    private void deliveryResult(final StringCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String string = response.body().string();
                    sendSuccessStringCallback(string, callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final IOException e, final StringCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onFailure(request, e);
            }
        });
    }

    private void sendSuccessStringCallback(final String string, final StringCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onResponse(string);
            }
        });
    }

    @NotNull
    private Request buildPostRequest(String url, RequestBody requestBody) {
        return new Request.Builder().url(url).post(requestBody).build();
    }

    @NotNull
    private Request buildPutRequest(String url, RequestBody requestBody){
        return new Request.Builder().url(url).put(requestBody).build();
    }

    @NotNull
    private Request buildPatchRequest(String url, RequestBody requestBody){
        return new Request.Builder().url(url).patch(requestBody).build();
    }

    @NotNull
    private Request buildDeleteRequest(String url, RequestBody requestBody){
        return new Request.Builder().url(url).delete(requestBody).build();
    }

    public interface StringCallback {
        void onFailure(Request request, IOException e);

        void onResponse(String response);
    }

    public static class Param {
        String key;
        String value;
        public Param() { }

        Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public enum SendType {
        Delete, Get, Patch, Post, Put
    }

}
