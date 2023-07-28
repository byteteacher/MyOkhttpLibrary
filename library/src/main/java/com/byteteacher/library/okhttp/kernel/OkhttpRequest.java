package com.byteteacher.library.okhttp.kernel;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;


import com.byteteacher.library.okhttp.bean.OkHttpRequestEntity;
import com.byteteacher.library.okhttp.bean.OkHttpSyncResponse;
import com.byteteacher.library.okhttp.bean.RequestMethod;
import com.byteteacher.library.okhttp.bean.SyncResponseType;
import com.byteteacher.library.okhttp.listener.OkhttpBitmapRequestListener;
import com.byteteacher.library.okhttp.listener.OkhttpByteRequestListener;
import com.byteteacher.library.okhttp.listener.OkhttpRequestListener;
import com.byteteacher.library.okhttp.listener.OkhttpStreamRequestListener;
import com.byteteacher.library.okhttp.listener.OkhttpStringRequestListener;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by cj on 2020/7/30.
 */
public class OkhttpRequest {

    private static final String ContentType = "Content-Type";
    private static final String ContentTypePlain = "text/plain;charset=utf-8";
    private static final String ContentTypeJson = "application/json;charset=utf-8";
    private static final String ContentTypeStream = "application/octet-stream";
    private static OkhttpRequest instance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    private OkhttpRequest() {
//        InputStream server = HZKernel.getInstance().getContext().getClass().getClassLoader().getResourceAsStream("assets/server.crt");
//        InputStream client = HZKernel.getInstance().getContext().getClass().getClassLoader().getResourceAsStream("assets/client.bks");
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{server}, client, "123456");

        mOkHttpClient = new OkHttpClient().newBuilder()
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .sslSocketFactory(OkhttpSafeUtils.getUnSafeSocketFactory(), OkhttpSafeUtils.UnSafeTrustManager)
                .hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static OkhttpRequest getInstance() {
        if (instance == null) {
            synchronized (OkhttpRequest.class) {
                if (instance == null) {
                    instance = new OkhttpRequest();
                }
            }
        }
        return instance;
    }


    public void request(OkHttpRequestEntity entity, OkhttpRequestListener okhttpRequestListener) {
        OkhttpCallback callback = createOkHttpCallback(entity, okhttpRequestListener);
        try {
            OkhttpClientUtils.requestAsync(mOkHttpClient, entity, createRequest(entity), callback);
        } catch (Exception e) {
            if (okhttpRequestListener != null) {
                okhttpRequestListener.onError(e.getMessage());
            }
        }
    }

    public OkHttpSyncResponse request(OkHttpRequestEntity entity, SyncResponseType type) {
        try {
            return OkhttpClientUtils.requestSync(mOkHttpClient, entity, createRequest(entity), type);
        } catch (Exception e) {
            OkHttpSyncResponse response = new OkHttpSyncResponse();
            response.setSuccessful(false);
            response.setError(e.getMessage());
            return response;
        }
    }

    /**
     * callback转listener，程序使用callback，用户使用listener
     */
    private OkhttpCallback createOkHttpCallback(OkHttpRequestEntity entity, OkhttpRequestListener listener) {
        Handler handler = entity.isBackOnUiThread ? mHandler : null;
        if (listener instanceof OkhttpStringRequestListener) {
            return new OkhttpStringCallback(listener, handler);
        }
        if (listener instanceof OkhttpBitmapRequestListener) {
            return new OkhttpBitmapCallback(listener, handler);
        }
        if (listener instanceof OkhttpStreamRequestListener) {
            return new OkhttpStreamCallback(listener, handler);
        }
        if (listener instanceof OkhttpByteRequestListener) {
            return new OkhttpByteCallback(listener, handler);
        }
        return null;
    }


    /**
     * 生成Request
     */
    private Request createRequest(OkHttpRequestEntity entity) {
        Request.Builder builder = null;
        RequestMethod requestMethod = entity.requestMethod;
        if (requestMethod == RequestMethod.GET) {
            builder = new Request.Builder().url(createUrl(entity.url, entity.param)).tag(entity.tag).get();
        }
        if (requestMethod == RequestMethod.HEAD) {
            builder = new Request.Builder().url(createUrl(entity.url, entity.param)).tag(entity.tag).get();
        }
        if (requestMethod == RequestMethod.POST) {
            builder = new Request.Builder().url(entity.url).tag(entity.tag).post(createRequestBody(entity));
        }
        if (requestMethod == RequestMethod.PUT) {
            builder = new Request.Builder().url(entity.url).tag(entity.tag).put(createRequestBody(entity));
        }
        if (requestMethod == RequestMethod.DELETE) {
            builder = new Request.Builder().url(entity.url).tag(entity.tag).delete(createRequestBody(entity));
        }
        if (requestMethod == RequestMethod.PATCH) {
            builder = new Request.Builder().url(entity.url).tag(entity.tag).patch(createRequestBody(entity));
        }
        if (builder == null) {
            builder = new Request.Builder().url(createUrl(entity.url, entity.param)).tag(entity.tag).get();
        }

        Iterator<Map.Entry<String, String>> headIterator = entity.header.entrySet().iterator();
        while (headIterator.hasNext()) {
            Map.Entry<String, String> entry = headIterator.next();
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        /** 防重放配置开始 **/
//        try {
//            builder.addHeader("msp-client-id", DeviceUtil.getDeviceIMEI(HZKernel.getInstance().getContext()));
//            builder.addHeader("msp-req-sn", UUIDUtil.getUUIDString());
//            builder.addHeader("msp-req-timestamp", SM4Utils.encryptData_CBC(ConstantSM4.SM4_KEY, ConstantSM4.SM4_IV, ByteTools.str2HexStr(String.valueOf(System.currentTimeMillis()))));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /** 防重放配置结束 **/

        return builder.build();
    }

    /**
     * 为get、head请求组装url，将参数追加到method?后面。
     * url可以是方法名method，也可以是method?key1=value1
     */
    public String createUrl(String url, LinkedHashMap<String, String> params) {
        Uri.Builder urlBuilder = Uri.parse(url).buildUpon();
        Iterator<Map.Entry<String, String>> paramIterator = params.entrySet().iterator();
        while (paramIterator.hasNext()) {
            Map.Entry<String, String> entry = paramIterator.next();
            urlBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return urlBuilder.build().toString();
    }


    /**
     * post请求生成Request需要的RequestBody
     */
    private RequestBody createRequestBody(OkHttpRequestEntity entity) {
        RequestBody requestBody = null;

        if (entity.header.containsKey(ContentType)) {
            String contentType = entity.header.get(ContentType);
            if ("text/plain".equals(contentType)) {
                if (entity.param.containsKey("string")) {
                    requestBody = RequestBody.create(MediaType.parse(ContentTypePlain), entity.param.get("string"));
                }
            } else if ("application/json".equals(contentType) || "application/json;charset=UTF-8".equals(contentType)) {
                if (entity.param.containsKey("json")) {
                    requestBody = RequestBody.create(MediaType.parse(ContentTypeJson), entity.param.get("json"));
                }
            } else if ("image/jpeg".equals(contentType)) {
                if (entity.param.containsKey("file")) {
                    requestBody = RequestBody.create(MediaType.parse(ContentTypeStream), new File(entity.param.get("file")));
                }
            } else if ("multipart/form-data".equals(contentType)) {
                //multipart表单
                MultipartBody.Builder builder = new MultipartBody.Builder();
                Iterator<Map.Entry<String, String>> paramIterator = entity.param.entrySet().iterator();
                while (paramIterator.hasNext()) {
                    Map.Entry<String, String> entry = paramIterator.next();
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
                for (OkHttpRequestEntity.MultipartFileBean multipartFileBean : entity.multipartFileList) {
                    builder.addFormDataPart(multipartFileBean.getFileKey(), multipartFileBean.getFileName(),
                            RequestBody.create(MediaType.parse(ContentTypeStream), multipartFileBean.getFile()));
                }
                for (OkHttpRequestEntity.MultipartByteBean multipartByteBean : entity.multipartByteList) {
                    builder.addFormDataPart(multipartByteBean.getFileKey(), multipartByteBean.getFileName(),
                            RequestBody.create(MediaType.parse(ContentTypeStream), multipartByteBean.getFile()));
                }
                builder.setType(MultipartBody.FORM);
                requestBody = builder.build();
            }
        }

        //默认"application/x-www-form-urlencoded"
        if (requestBody == null) {
            FormBody.Builder builder = new FormBody.Builder();
            Iterator<Map.Entry<String, String>> paramIterator = entity.param.entrySet().iterator();
            while (paramIterator.hasNext()) {
                Map.Entry<String, String> entry = paramIterator.next();
                builder.add(entry.getKey(), entry.getValue());
            }
            requestBody = builder.build();
        }


        return requestBody;

    }

    /**
     * 根据tag取消对应请求。
     *
     * @param tag
     */
    public void cancelRequest(String tag) {

        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


}
