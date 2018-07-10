package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import com.example.apple.retrofitdemoapp.Models.Token;

import javax.inject.Singleton;

@Singleton
public final class ApiConfiguration {

    public interface ApiConfigurationListener {
        void OnTokenExpired();

        String getLocalizedError(int statusCode);

        boolean isNetWorkAvailable();

        void onTokenChanged(Token token);
    }

    private String mApiURL;
    private String mFileServerURL;
    private String mLanguage;
    private String mAppType;
    private ApiConfigurationListener mListener;

    public ApiConfiguration(String apiUrl, String fileServerUrl, String lang, String appType) {
        mApiURL = apiUrl;
        mFileServerURL = fileServerUrl;
        mLanguage = lang;
        mAppType = appType;
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
        return mListener;
    }

    public void setListener(ApiConfigurationListener mListener) {
        this.mListener = mListener;
    }
}
