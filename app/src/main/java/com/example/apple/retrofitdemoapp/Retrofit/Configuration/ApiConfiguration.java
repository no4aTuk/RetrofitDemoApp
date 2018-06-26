package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

public final class ApiConfiguration {
    private String mApiURL;
    private String mFileServerURL;
    private String mLanguage;
    private String mAppType;


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
}
