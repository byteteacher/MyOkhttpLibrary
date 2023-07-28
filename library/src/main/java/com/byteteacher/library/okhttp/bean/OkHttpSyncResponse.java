package com.byteteacher.library.okhttp.bean;

import java.io.InputStream;

/**
 * Created by cj on 2020/8/8.
 *
 * 同步请求，统一封装结果
 */
public class OkHttpSyncResponse {
    private boolean isSuccessful;
    private String response;
    private byte[] responseBytes;
    private InputStream responseStream;
    private String error;

    public String getResponse() {
        return response;
    }

    public void setResponse(InputStream response) {
        this.responseStream = response;
    }

    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public InputStream getResponseStream() {
        return responseStream;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setResponse(byte[] response) {
        this.responseBytes = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
