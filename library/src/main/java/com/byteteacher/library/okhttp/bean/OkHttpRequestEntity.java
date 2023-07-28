package com.byteteacher.library.okhttp.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Authenticator;

/**
 * Created by cj on 2020/7/30.
 * okhttp请求实体类，包含发起一次请求的所有信息，按需传入。
 */
public class OkHttpRequestEntity {

    public final String tag;
    public final LinkedHashMap<String, String> header;
    public final LinkedHashMap<String, String> param;
    public String url;
    public long connectTime = 0;
    public long readTime = 0;
    public long writeTime = 0;
    public boolean needUploadProgress = false;
    public boolean needDownloadProgress = false;
    /**
     * 结果是否转到UI线程
     */
    public boolean isBackOnUiThread = true;
    public Authenticator authenticator;
    public RequestMethod requestMethod = RequestMethod.GET;
    public List<MultipartFileBean> multipartFileList;//上传文件
    public List<MultipartByteBean> multipartByteList;//上传文件分块的字节数组

    public OkHttpRequestEntity(String tag) {
        this.tag = tag;
        this.header = new LinkedHashMap<>();
        this.param = new LinkedHashMap<>();
        this.multipartFileList = new ArrayList<>();
        this.multipartByteList = new ArrayList<>();
    }

    public OkHttpRequestEntity(String tag, String url) {
        this.tag = tag;
        this.url = url;
        this.header = new LinkedHashMap<>();
        this.param = new LinkedHashMap<>();
        this.multipartFileList = new ArrayList<>();
        this.multipartByteList = new ArrayList<>();
    }

    public OkHttpRequestEntity url(String url) {
        this.url = url;
        return this;
    }

    public OkHttpRequestEntity addHead(String key, String value) {
        if (key == null || value == null) {
            return this;
        }
        header.put(key, value);
        return this;
    }

    public OkHttpRequestEntity addParam(String key, String value) {
        if (key == null || value == null) {
            return this;
        }
        param.put(key, value);
        return this;
    }

    public OkHttpRequestEntity needUploadProgress(boolean needUploadProgress) {
        this.needUploadProgress = needUploadProgress;
        return this;
    }

    public OkHttpRequestEntity needDownloadProgress(boolean needDownloadProgress) {
        this.needDownloadProgress = needDownloadProgress;
        return this;
    }

    public OkHttpRequestEntity isBackOnUiThread(boolean backOnUiThread) {
        isBackOnUiThread = backOnUiThread;
        return this;
    }

    public OkHttpRequestEntity connectTime(long connectTime) {
        this.connectTime = connectTime;
        return this;
    }

    public OkHttpRequestEntity readTime(long readTime) {
        this.readTime = readTime;
        return this;
    }

    public OkHttpRequestEntity writeTime(long writeTime) {
        this.writeTime = writeTime;
        return this;
    }

    public OkHttpRequestEntity authenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public OkHttpRequestEntity requestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public List<MultipartFileBean> getMultipartFileList() {
        return multipartFileList;
    }

    public void setMultipartFileList(List<MultipartFileBean> multipartFileList) {
        this.multipartFileList = multipartFileList;
    }

    public List<MultipartByteBean> getMultipartByteList() {
        return multipartByteList;
    }

    public void setMultipartByteList(List<MultipartByteBean> multipartByteList) {
        this.multipartByteList = multipartByteList;
    }

    public OkHttpRequestEntity addMultipartFile(MultipartFileBean file) {
        if (file == null) {
            return this;
        }
        multipartFileList.add(file);
        return this;
    }

    public OkHttpRequestEntity addMultipartByte(MultipartByteBean file) {
        if (file == null) {
            return this;
        }
        multipartByteList.add(file);
        return this;
    }

    public static class MultipartFileBean {
        public String fileKey;
        public String fileName;
        public File file;

        public MultipartFileBean(String fileKey, String fileName, File file) {
            this.fileKey = fileKey;
            this.fileName = fileName;
            this.file = file;
        }

        public String getFileKey() {
            return fileKey;
        }

        public void setFileKey(String fileKey) {
            this.fileKey = fileKey;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }

    public static class MultipartByteBean {
        public String fileKey;
        public String fileName;
        public byte[] file;

        public MultipartByteBean(String fileKey, String fileName, byte[] file) {
            this.fileKey = fileKey;
            this.fileName = fileName;
            this.file = file;
        }

        public String getFileKey() {
            return fileKey;
        }

        public void setFileKey(String fileKey) {
            this.fileKey = fileKey;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getFile() {
            return file;
        }

        public void setFile(byte[] file) {
            this.file = file;
        }
    }
}
