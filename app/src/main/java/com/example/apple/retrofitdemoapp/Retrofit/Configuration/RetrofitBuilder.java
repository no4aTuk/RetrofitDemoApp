package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.AuthInterceptorSync;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.FileDownloadProgressInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.HeadersInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static Retrofit sInstance;

    public static Retrofit getsInstance() {
        if (sInstance == null) {
            sInstance = createInstance();
        }
        return sInstance;
    }

    public static void addInterceptor(Interceptor interceptor) {

    }

    private static Retrofit createInstance() {

        ApiConfiguration configuration = ApiConfiguration.getInstance();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeadersInterceptor())
                .addInterceptor(new AuthInterceptorSync())
                .addNetworkInterceptor(new FileDownloadProgressInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(configuration.getApiURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
