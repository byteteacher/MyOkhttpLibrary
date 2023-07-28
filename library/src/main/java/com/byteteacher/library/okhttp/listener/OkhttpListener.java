package com.byteteacher.library.okhttp.listener;


/**
 * Created by cj on 2020/8/1.
 */
public interface OkhttpListener<T> {

    void onSuccess(T response);

    void onError(String msg);

    void onUploadProgress(long current, long total, boolean done);

    void onDownloadProgress(long current, long total, boolean done);
}
