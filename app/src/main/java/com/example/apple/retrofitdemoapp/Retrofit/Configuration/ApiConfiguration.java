package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import java.lang.ref.WeakReference;

public final class ApiConfiguration {

    public interface ApiConfigurationListener {
        void OnTokenExpired();
        String getLocalizedError(int statusCode);
        boolean isNetWorkAvailable();
    }

    private String mApiURL;
    private String mFileServerURL;
    private String mLanguage;
    private String mAppType;
    private WeakReference<ApiConfigurationListener> mListener;

    private ApiConfiguration() { }

    public ApiConfiguration(String apiUrl, String fileServerUrl,
                            String lang, String appType) {
        this.mApiURL = apiUrl;
        this.mFileServerURL = fileServerUrl;
        this.mLanguage = lang;
        this.mAppType = appType;
    }

    public String getApiURL() {
        return mApiURL;
    }

    public String getFileServerURL() {
        return mFileServerURL;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getAppType() {
        return mAppType;
    }

    public ApiConfigurationListener getListener() {
        return mListener.get();
    }

    public void setListener(ApiConfigurationListener mListener) {
        this.mListener = new WeakReference<>(mListener);
    }
}
