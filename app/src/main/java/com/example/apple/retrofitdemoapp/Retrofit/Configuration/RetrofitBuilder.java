package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;
import com.example.apple.retrofitdemoapp.Retrofit.Services.ServiceHolder;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnFileRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.AuthInterceptorSync;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.FileDownloadProgressInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.HeadersInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.NetworkConnectionInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.FileService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static final int DEFAULT_TIMEOUT = 10; // seconds
    private static final int READ_WRITE_TIMEOUT = 5; //minutes

    public static Retrofit createInstance(final ApiConfiguration configuration, CredentialsStorage storage, ServiceHolder<AuthService> serviceHolder) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_WRITE_TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(READ_WRITE_TIMEOUT, TimeUnit.MINUTES)
                .addInterceptor(new NetworkConnectionInterceptor() {
                    @Override
                    public boolean isNetworkAvailable() {
                        return configuration.getListener() != null
                                && configuration.getListener().isNetWorkAvailable();
                    }
                })
                .addInterceptor(new HeadersInterceptor(configuration))
                .addInterceptor(new AuthInterceptorSync(storage, configuration, serviceHolder))
                .addNetworkInterceptor(new FileDownloadProgressInterceptor() {
                    @Override
                    public Map<String, OnFileRequestComplete<File>> downloadProgressListeners() {
                        return FileService.downloadFileListener;
                    }
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(configuration.getApiURL())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private static Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }
}
