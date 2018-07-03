package com.example.apple.retrofitdemoapp.Modules;

import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiConfigurationModule {

    private ApiConfiguration mApiConfiguration;

    public ApiConfigurationModule(String apiUrl, String fileServerUrl,
                                  String lang, String appType, ApiConfiguration.ApiConfigurationListener configurationListener) {
        ApiConfiguration configuration = new ApiConfiguration(apiUrl, fileServerUrl, lang, appType);
        configuration.setListener(configurationListener);
        this.mApiConfiguration = configuration;
    }

    @ApiApplicationScope
    @Provides
    public ApiConfiguration apiConfiguration() {
        return mApiConfiguration;
    }
}
