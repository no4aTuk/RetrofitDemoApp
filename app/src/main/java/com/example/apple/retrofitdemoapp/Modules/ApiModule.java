package com.example.apple.retrofitdemoapp.Modules;

import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService2;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.IAuthService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {OkHttpModule.class, ApiConfigurationModule.class})
public class ApiModule {

    @Provides
    public AuthService2 authService2(Retrofit retrofit) {
        IAuthService serviceInstance = retrofit.create(IAuthService.class);
        return new AuthService2(serviceInstance);
    }

    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory,
                             Gson gson, ApiConfiguration configuration) {
        return new Retrofit.Builder()
                .baseUrl(configuration.getApiURL())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(okHttpClient)
                .build();
    }

    @Provides
    public Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }
}
