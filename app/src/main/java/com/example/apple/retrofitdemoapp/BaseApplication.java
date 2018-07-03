package com.example.apple.retrofitdemoapp;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.example.apple.retrofitdemoapp.Components.ApiServiceComponent;
import com.example.apple.retrofitdemoapp.Components.DaggerApiServiceComponent;
import com.example.apple.retrofitdemoapp.Constants.ApiConstants;
import com.example.apple.retrofitdemoapp.Helpers.ApiErrorHelper;
import com.example.apple.retrofitdemoapp.Helpers.NetworkUtils;
import com.example.apple.retrofitdemoapp.Helpers.Preferences;
import com.example.apple.retrofitdemoapp.Modules.ApiConfigurationModule;
import com.example.apple.retrofitdemoapp.Modules.ApiModule;
import com.example.apple.retrofitdemoapp.Modules.AppContextModule;
import com.example.apple.retrofitdemoapp.Modules.CredentialsStorageModule;
import com.example.apple.retrofitdemoapp.Modules.OkHttpModule;
import com.example.apple.retrofitdemoapp.Retrofit.ApiEntryPoint;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;

import java.util.Locale;

public class BaseApplication extends Application implements ApiConfiguration.ApiConfigurationListener {

    private ApiServiceComponent apiServiceComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //Configure api
//        ApiConfiguration.setupInstance(ApiConstants.BASE_API_URL, ApiConstants.FILE_SERVER_API_URL,
//                getAppLanguage(), ApiConstants.APP_TYPE);
//        ApiConfiguration.getInstance().setListener(this);
//        initUserCredentials();

        String token = Preferences.getToken(getApplicationContext());
        String refreshToken = Preferences.getRefreshToken(getApplicationContext());

        ApiServiceComponent apiServiceComponent = DaggerApiServiceComponent.builder()
                .apiConfigurationModule(new ApiConfigurationModule(ApiConstants.BASE_API_URL,
                        ApiConstants.FILE_SERVER_API_URL, getAppLanguage(), ApiConstants.APP_TYPE, this))
                .credentialsStorageModule(new CredentialsStorageModule(token, refreshToken))
                .appContextModule(new AppContextModule(this))
                .build();
        this.apiServiceComponent = apiServiceComponent;
        ApiEntryPoint.getInstance().setApiServiceComponent(apiServiceComponent);
    }

    public static BaseApplication get(Activity activity) {
        return (BaseApplication) activity.getApplication();
    }

    public ApiServiceComponent api() {
        return apiServiceComponent;
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

    //-------------------------------------------
    // ApiConfiguration.ApiConfigurationListener
    //-------------------------------------------
    @Override
    public void OnTokenExpired() {
        //TODO go to login screen
        Log.d("LOGOUT", "OnTokenExpired: ");
    }

    @Override
    public String getLocalizedError(int statusCode) {
        return ApiErrorHelper.getErrorByCode(this, statusCode);
    }

    @Override
    public boolean isNetWorkAvailable() {
        return NetworkUtils.hasInternetConnection(this, true);
    }
}
