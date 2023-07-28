package com.byteteacher.library.okhttp.kernel;

import android.os.Handler;


import com.byteteacher.library.okhttp.listener.OkhttpListener;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by cj on 2020/8/1.
 */
class OkhttpByteCallback extends OkhttpCallback<byte[]> {

    public OkhttpByteCallback(OkhttpListener<byte[]> mOkHttpListener, Handler mHandle) {
        super(mOkHttpListener, mHandle);
    }

    @Override
    protected void processResponseBody(ResponseBody responseBody) {
        try {
            onSuccess(responseBody.bytes());
        } catch (IOException e) {
            e.printStackTrace();
            onError(e.toString());
        }
    }

}
