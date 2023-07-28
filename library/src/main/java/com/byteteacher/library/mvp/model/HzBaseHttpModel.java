package com.byteteacher.library.mvp.model;


import com.byteteacher.library.okhttp.bean.OkHttpRequestEntity;
import com.byteteacher.library.okhttp.kernel.OkhttpRequest;
import com.byteteacher.library.okhttp.listener.OkhttpStringRequestListener;

/**
 * Created by cj on 2020/7/23.
 */
public abstract class HzBaseHttpModel<I, O> extends HzKernelModel<I, O> {


    OkhttpStringRequestListener stringRequestListener = new OkhttpStringRequestListener() {
        @Override
        public void onSuccess(String response) {
            O output = handleOutput(response);
            if (!isNotifyResult()) {
                responseSuccess(output);
            }
        }

        @Override
        public void onError(String errorMsg) {
            responseError(errorMsg);
        }
    };


    @Override
    protected void request(I request) {
        OkHttpRequestEntity entity = handleInput(request);
        OkhttpRequest.getInstance().request(entity, stringRequestListener);
    }

    /**
     * 将输入的参数转换成OkHttp请求所需要的参数
     *
     * @param input
     * @return
     */
    protected abstract OkHttpRequestEntity handleInput(I input);


    /**
     * OkHttp请求数据回来之后，是否由自己决定触发回调的时机，而不是由基类决定
     *
     * @return true 本类处理
     * false 本类不处理
     */
    protected boolean isNotifyResult() {
        return false;
    }


    /**
     * 解析OkHttp请求到的数据
     *
     * @param result
     * @return
     */
    protected abstract O handleOutput(String result);

    public void cancelRequest(String tag) {
        OkhttpRequest.getInstance().cancelRequest(tag);
    }


}
