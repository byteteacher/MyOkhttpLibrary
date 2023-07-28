package com.byteteacher.library.okhttp.kernel;



import com.byteteacher.library.okhttp.bean.OkHttpRequestEntity;
import com.byteteacher.library.okhttp.bean.OkHttpSyncResponse;
import com.byteteacher.library.okhttp.bean.SyncResponseType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cj on 2020/7/30.
 */
public class OkhttpClientUtils {

    /**
     * 异步请求
     */
    public static void requestAsync(OkHttpClient client, Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
    }

    /**
     * 重载异步请求，重组okhttpClient，通过entity里设置的参数
     */
    public static void requestAsync(OkHttpClient client, OkHttpRequestEntity entity, Request request, Callback callback) {
        requestAsync(handleOkHttpClient(client, entity), request, callback);
    }

    /**
     * 同步请求
     */
    public static Response requestSync(OkHttpClient client, Request request) throws IOException {
        return client.newCall(request).execute();
    }

    /**
     * 同步请求，返回值封装，统一封装结果为OkHttpSyncResponse对象
     */
    public static OkHttpSyncResponse requestSync(OkHttpClient client, Request request, SyncResponseType type) {
        OkHttpSyncResponse okHttpSyncResponse = new OkHttpSyncResponse();
        Response response = null;
        try {
            response = requestSync(client, request);
            if (response == null) {
                okHttpSyncResponse.setSuccessful(false);
                okHttpSyncResponse.setError("response is not successful");
            } else if (!response.isSuccessful()) {
                okHttpSyncResponse.setSuccessful(false);
                okHttpSyncResponse.setError(response.message());
            } else {
                okHttpSyncResponse.setSuccessful(true);
                if (SyncResponseType.TypeBytes == type) {
                    okHttpSyncResponse.setResponse(response.body().bytes());
                } else if (SyncResponseType.TypeString == type) {
                    okHttpSyncResponse.setResponse(response.body().string());
                } else if (SyncResponseType.TypeStream == type) {
                    okHttpSyncResponse.setResponse(response.body().byteStream());
                }
            }
        } catch (IOException e) {
            okHttpSyncResponse.setSuccessful(false);
            okHttpSyncResponse.setError(e.getMessage());
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
        return okHttpSyncResponse;
    }

    /**
     * 重载同步请求，重组okhttpClient，通过entity里设置的参数
     */
    public static OkHttpSyncResponse requestSync(OkHttpClient client, OkHttpRequestEntity entity, Request request, SyncResponseType type) {
        return requestSync(handleOkHttpClient(client, entity), request, type);
    }


    /**
     * 根据重组okhttpClient
     *
     * @param client
     * @param entity
     * @return new okhttpclient
     */
    private static OkHttpClient handleOkHttpClient(OkHttpClient client, OkHttpRequestEntity entity) {
        if (client != null && entity != null) {
            if (entity.connectTime > 0 || entity.readTime > 0 || entity.writeTime > 0) {
                return client.newBuilder()
                        .connectTimeout(entity.connectTime > 0 ? entity.connectTime : client.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
                        .readTimeout(entity.readTime > 0 ? entity.readTime : client.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                        .writeTimeout(entity.writeTime > 0 ? entity.writeTime : client.writeTimeoutMillis(), TimeUnit.MILLISECONDS)
                        .authenticator(entity.authenticator == null ? Authenticator.NONE : entity.authenticator)
                        .build();
            }
        }
        return client;
    }


}
