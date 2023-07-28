package com.byteteacher.library.okhttp.kernel;



import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.byteteacher.library.okhttp.listener.OkhttpListener;
import com.google.gson.Gson;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by cj on 2020/8/1.
 */
public abstract class OkhttpCallback<T> implements Callback {


    protected OkhttpListener<T> mOkHttpListener;
    private Handler mHandle;

    public OkhttpCallback(OkhttpListener<T> mOkHttpListener, Handler mHandle) {
        this.mOkHttpListener = mOkHttpListener;
        this.mHandle = mHandle;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        String message = e.getMessage();
        String requestInfo = call.request().toString();
//        HzLogSaveUtil.saveLog("网络请求失败：exception=" + message + " requestInfo=" + requestInfo, HzLogSaveUtil.LogType.WEB, HzLogSaveUtil.AppType.NATIVE_APP);

        if (TextUtils.isEmpty(message)) {
            onError("接口请求失败，异常未知");
        } else {
            if (message.startsWith("Failed to connect to")) {
                onError("连接失败，请检查网络和IP端口");
            } else {
                onError(e.getMessage());
            }
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (call.isCanceled()) {
            onError("请求已取消");
            return;
        }
        if (response.isSuccessful() || response.code() == 401) {
            processResponseBody(response.body());
        } else {
            String string = "";
            try {
                string = response.body().string();
                Log.e("webrequest", "onResponse string: " + string + "\r\n" + "response = " + response.toString());

//                HzBaseEntity hzBaseEntity = new Gson().fromJson(string, HzBaseEntity.class);
//                if (hzBaseEntity != null) {
//                    onError(string);
//                } else {
                    onError(response.toString());
//                }

            } catch (Exception e) {
                e.printStackTrace();
                onError(response.toString() + "body.string=" + string);
            }
        }
    }


    protected void onError(final String error) {
        if (mHandle != null) {
            mHandle.post(new Runnable() {
                @Override
                public void run() {
                    if (mOkHttpListener != null) {
                        mOkHttpListener.onError(error);
                    }
                }
            });
        } else {
            if (mOkHttpListener != null) {
                mOkHttpListener.onError(error);
            }
        }
    }

    protected void onSuccess(final T response) {
        if (mHandle != null) {
            mHandle.post(new Runnable() {
                @Override
                public void run() {
                    if (mOkHttpListener != null) {
                        mOkHttpListener.onSuccess(response);
                    }
                }
            });
        } else {
            if (mOkHttpListener != null) {
                mOkHttpListener.onSuccess(response);
            }
        }
    }

    protected abstract void processResponseBody(ResponseBody responseBody);
}
