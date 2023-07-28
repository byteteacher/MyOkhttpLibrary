package com.byteteacher.library.okhttp.kernel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;


import com.byteteacher.library.okhttp.listener.OkhttpListener;

import okhttp3.ResponseBody;

/**
 * Created by cj on 2020/8/1.
 */
class OkhttpBitmapCallback extends OkhttpCallback<Bitmap> {

    public OkhttpBitmapCallback(OkhttpListener<Bitmap> mOkHttpListener, Handler mHandle) {
        super(mOkHttpListener, mHandle);
    }

    @Override
    protected void processResponseBody(ResponseBody responseBody) {
        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
        onSuccess(bitmap);
    }

}
