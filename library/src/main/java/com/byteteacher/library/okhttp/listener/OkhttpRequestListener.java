package com.byteteacher.library.okhttp.listener;

/**
 * Created by cj on 2020/8/1.
 */
public abstract class OkhttpRequestListener<T> implements OkhttpListener<T>{

    @Override
    public void onUploadProgress(long current, long total, boolean done) {

    }

    @Override
    public void onDownloadProgress(long current, long total, boolean done) {

    }
}
