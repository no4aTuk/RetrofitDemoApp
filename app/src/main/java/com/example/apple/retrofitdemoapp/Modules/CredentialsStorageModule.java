package com.example.apple.retrofitdemoapp.Modules;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Qualifiers.ApplicationContext;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppContextModule.class)
public class CredentialsStorageModule {

    private String accessToken;
    private String refreshToken;

    public CredentialsStorageModule(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @ApiApplicationScope
    @Provides
    CredentialsStorage credentialsStorage(@ApplicationContext Context context) {
        return new CredentialsStorage(context, accessToken, refreshToken);
    }
}
