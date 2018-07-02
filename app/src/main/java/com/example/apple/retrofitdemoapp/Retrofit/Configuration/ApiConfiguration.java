package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

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
    private ApiConfigurationListener mListener;

    private static ApiConfiguration sInstance;

    public static void setupInstance(String apiUrl, String fileServerUrl,
                              String lang, String appType) {

        if (sInstance == null) {
            sInstance = new ApiConfiguration();
        }
        sInstance.mApiURL = apiUrl;
        sInstance.mFileServerURL = fileServerUrl;
        sInstance.mLanguage = lang;
        sInstance.mAppType = appType;
    }

    public static ApiConfiguration getInstance() {
        return  sInstance;
    }

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
        return mListener;
    }

    public void setListener(ApiConfigurationListener mListener) {
        this.mListener = mListener;
    }
}
