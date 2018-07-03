package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Helpers.Preferences;

import java.lang.ref.WeakReference;

public class CredentialsStorage {

    private String mToken;
    private String mRefreshToken;
    private WeakReference<Context> mContext;

    private CredentialsStorage() {}

    public CredentialsStorage(Context context, String accessToken, String refreshToken) {
        this.mContext = new WeakReference<>(context.getApplicationContext());
        this.mToken = accessToken;
        this.mRefreshToken = refreshToken;
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
            Preferences.setToken(mContext.get(), accessToken);
        }
    }

    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
        if (mContext != null) {
            Preferences.setRefreshToken(mContext.get(), refreshToken);
        }
    }
}
