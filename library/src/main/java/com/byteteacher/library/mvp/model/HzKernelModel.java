package com.byteteacher.library.mvp.model;


import com.byteteacher.library.mvp.HzBaseModel;

/**
 * Created by cj on 2020/07/23.
 * 异步核心model，父类。
 */
public abstract class HzKernelModel<I, O> extends HzBaseModel<HzDataCallback<O>> {

    protected abstract void request(I request);

    protected void responseSuccess(O result) {
        if (getListener() == null) {
            return;
        }
        try {
            getListener().onSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void responseError(String response) {
        if (getListener() == null) {
            return;
        }
        try {
            getListener().onFailure(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
