package com.byteteacher.library.okhttp.kernel;

import android.os.Handler;


import com.byteteacher.library.okhttp.listener.OkhttpListener;

import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created by cj on 2020/8/1.
 */
class OkhttpStreamCallback extends OkhttpCallback<InputStream> {

    public OkhttpStreamCallback(OkhttpListener<InputStream> mOkHttpListener, Handler mHandle) {
        super(mOkHttpListener, mHandle);
    }

    @Override
    protected void processResponseBody(ResponseBody responseBody) {
        onSuccess(responseBody.byteStream());
    }


}
