package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnFileRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.AuthInterceptorSync;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.FileDownloadProgressInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.HeadersInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.NetworkConnectionInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.FileService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static Retrofit sInstance;

    public static Retrofit getsInstance(ApiConfiguration configuration, CredentialsStorage storage) {
        if (sInstance == null) {
            sInstance = createInstance(configuration, storage);
        }
        return sInstance;
    }

    private static Retrofit createInstance(final ApiConfiguration configuration, CredentialsStorage credentialsStorage) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(new NetworkConnectionInterceptor() {
                    @Override
                    public boolean isNetworkAvailable() {
                        return configuration.getListener() != null
                                && configuration.getListener().isNetWorkAvailable();
                    }
                })
                .addInterceptor(new HeadersInterceptor(configuration))
                .addInterceptor(new AuthInterceptorSync(credentialsStorage, configuration))
                .addNetworkInterceptor(new FileDownloadProgressInterceptor() {
                    @Override
                    public OnFileRequestComplete<File> downloadProgressListener() {
                        return FileService.downloadFileListener;
                    }
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(configuration.getApiURL())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(client)
                .build();
    }

    private static Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }
}
