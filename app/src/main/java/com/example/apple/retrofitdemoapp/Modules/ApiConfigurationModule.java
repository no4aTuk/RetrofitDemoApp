package com.example.apple.retrofitdemoapp.Modules;

import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiConfigurationModule {

    private ApiConfiguration mApiConfiguration;

    public ApiConfigurationModule(String apiUrl, String fileServerUrl,
                                  String lang, String appType) {
        this.mApiConfiguration = new ApiConfiguration(apiUrl, fileServerUrl, lang, appType);
    }

    @ApiApplicationScope
    @Provides
    public ApiConfiguration apiConfiguration() {
        return mApiConfiguration;
    }
}
