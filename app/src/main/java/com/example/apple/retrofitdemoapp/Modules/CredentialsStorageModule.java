package com.example.apple.retrofitdemoapp.Modules;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Qualifiers.ApplicationContext;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppContextModule.class)
public class CredentialsStorageModule {

    private CredentialsStorage mCredentialsStorage;

    public CredentialsStorageModule(@ApplicationContext Context context, String accessToken, String refreshToken) {
        this.mCredentialsStorage = new CredentialsStorage(context, accessToken, refreshToken);
    }

    @ApiApplicationScope
    @Provides
    CredentialsStorage credentialsStorage() {
        return this.mCredentialsStorage;
    }
}
