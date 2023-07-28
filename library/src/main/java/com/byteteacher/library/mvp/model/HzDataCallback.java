package com.byteteacher.library.mvp.model;


import com.byteteacher.library.mvp.IHzModelCallback;

/**
 * Created by cj on 2020/7/23.
 */
public interface HzDataCallback<O> extends IHzModelCallback {

    void onSuccess(O result);

    void onFailure(String error);

}
