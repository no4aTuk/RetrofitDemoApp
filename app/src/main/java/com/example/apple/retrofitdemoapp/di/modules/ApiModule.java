package com.example.apple.retrofitdemoapp.di.modules;

import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;
import com.example.apple.retrofitdemoapp.Retrofit.Services.ServiceHolder;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.RetrofitBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    @Singleton
    public Retrofit retrofit(ApiConfiguration apiConfig, CredentialsStorage credentialsStorage, @Named("AuthService") ServiceHolder<AuthService> serviceHolder) {
        return RetrofitBuilder.createInstance(apiConfig, credentialsStorage, serviceHolder);
    }

    @Provides
    @Singleton
    @Named("AuthService")
    public ServiceHolder<AuthService> holder() {
        return new ServiceHolder<>();
    }

}
