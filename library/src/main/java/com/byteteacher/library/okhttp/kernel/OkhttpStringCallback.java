package com.byteteacher.library.okhttp.kernel;

import android.os.Handler;


import com.byteteacher.library.okhttp.listener.OkhttpListener;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by cj on 2020/8/1.
 */
class OkhttpStringCallback extends OkhttpCallback<String> {

    public OkhttpStringCallback(OkhttpListener<String> mOkHttpListener, Handler mHandle) {
        super(mOkHttpListener, mHandle);
    }

    @Override
    protected void processResponseBody(ResponseBody responseBody) {
        try {
            onSuccess(responseBody.string());
        } catch (IOException e) {
            e.printStackTrace();
            onError(e.toString());
        }
    }


}
