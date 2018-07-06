package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Helpers.Preferences;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CredentialsStorage {

    private String mToken;
    private String mRefreshToken;
    private WeakReference<Context> mContext;

    private Context context;

    public CredentialsStorage() {}

    public CredentialsStorage(Context context) {
        this.context = context;
    }

    private static CredentialsStorage sInstance;

    public static void setupInstance(Context context, String accessToken, String refreshToken) {

        if (sInstance == null) {
            sInstance = new CredentialsStorage();
        }
        //Just to be sure that passed single global application context
        sInstance.mContext = new WeakReference<>(context.getApplicationContext());
        sInstance.mToken = accessToken;
        sInstance.mRefreshToken = refreshToken;
    }

    public void setup(String accessToken, String refreshToken) {
        setToken(accessToken);
        setRefreshToken(refreshToken);
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
        if (context != null) {
            Preferences.setToken(context, accessToken);
        }
    }

    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
        if (context != null) {
            Preferences.setRefreshToken(context, refreshToken);
        }
    }
}
