package com.example.apple.retrofitdemoapp;

import android.app.Application;

import com.example.apple.retrofitdemoapp.Helpers.ApiConstants;
import com.example.apple.retrofitdemoapp.Helpers.Preferences;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;

import java.util.Locale;

public class BaseApplication extends Application implements ApiConfiguration.ApiConfigurationListener {

    @Override
    public void onCreate() {
        super.onCreate();

        //Configure api
        ApiConfiguration.setupInstance(ApiConstants.BASE_API_URL, ApiConstants.FILE_SERVER_API_URL,
                getAppLanguage(), ApiConstants.APP_TYPE);
        ApiConfiguration.getInstance().setListener(this);
        initUserCredentials();
    }

    private String getAppLanguage() {
        String locale = Locale.getDefault().getLanguage();
        if (locale.contentEquals("en")) {
            return ApiConstants.APP_LANGUAGE_EN;
        } else if (locale.contentEquals("de")) {
            return ApiConstants.APP_LANGUAGE_DE;
        } else {
            return ApiConstants.APP_LANGUAGE_RU;
        }
    }

    private void initUserCredentials() {
        String token = Preferences.getToken(getApplicationContext());
        String refreshToken = Preferences.getRefreshToken(getApplicationContext());
        CredentialsStorage.setupInstance(getApplicationContext(), token, refreshToken);
    }

    //ApiConfiguration.ApiConfigurationListener
    @Override
    public void OnTokenExpired() {
        //TODO go to login screen
        int a = 0;
    }
}
