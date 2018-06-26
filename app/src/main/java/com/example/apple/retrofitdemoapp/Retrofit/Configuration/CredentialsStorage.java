package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Helpers.Preferences;

import java.lang.ref.WeakReference;

public class CredentialsStorage {

    private String mToken;
    private String mRefreshToken;
    private Context mContext;

    private CredentialsStorage() {}

    private static CredentialsStorage sInstance;

    public static void setupInstance(Context context, String accessToken, String refreshToken) {

        if (sInstance == null) {
            sInstance = new CredentialsStorage();
        }
        sInstance.mContext = context.getApplicationContext(); //Just to be sure that passed single global application context
        sInstance.mToken = accessToken;
        sInstance.mRefreshToken = refreshToken;
    }

    public static CredentialsStorage getInstance() {
        return  sInstance;
    }

    public String getToken() {
        return mToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setToken(String accessToken) {
        mToken = accessToken;
        if (mContext != null) {
            Preferences.setToken(mContext, accessToken);
        }
    }

    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
        if (mContext != null) {
            Preferences.setRefreshToken(mContext, refreshToken);
        }
    }
}
