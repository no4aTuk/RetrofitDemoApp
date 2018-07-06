package com.example.apple.retrofitdemoapp;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.example.apple.retrofitdemoapp.Constants.ApiConstants;
import com.example.apple.retrofitdemoapp.Helpers.ApiErrorHelper;
import com.example.apple.retrofitdemoapp.Helpers.NetworkUtils;
import com.example.apple.retrofitdemoapp.Helpers.Preferences;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.di.components.ApplicationComponent;
import com.example.apple.retrofitdemoapp.di.components.DaggerApplicationComponent;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class BaseApplication extends Application implements ApiConfiguration.ApiConfigurationListener,
        HasActivityInjector {

    private static ApplicationComponent applicationComponent;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        //Configure api
//        ApiConfiguration.setupInstance(ApiConstants.BASE_API_URL, ApiConstants.FILE_SERVER_API_URL,
//                getAppLanguage(), ApiConstants.APP_TYPE);
//        ApiConfiguration.getInstance().setListener(this);
        initUserCredentials();
        if (applicationComponent == null) {
            ApiConfiguration configuration = new ApiConfiguration(ApiConstants.BASE_API_URL, ApiConstants.FILE_SERVER_API_URL,
                    getAppLanguage(), ApiConstants.APP_TYPE);
            configuration.setListener(this);
            applicationComponent = DaggerApplicationComponent.builder()
                    .context(this)
                    .api(configuration)
                    .storage(new CredentialsStorage(this))
                    .build();
            applicationComponent.inject(this);

        }
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

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    public static ApplicationComponent getAppComponent() {
        return applicationComponent;
    }
}
