package com.byteteacher.library.mvp;

/**
 * Created by cj on 2020/7/23.
 */
public class HzBaseModel<T extends IHzModelCallback> implements IHzModel {

    T listener;

    public T getListener() {
        return listener;
    }

    public void setListener(T listener) {
        this.listener = listener;
    }
}
