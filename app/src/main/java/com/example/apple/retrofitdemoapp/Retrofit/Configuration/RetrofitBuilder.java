package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import com.example.apple.retrofitdemoapp.Helpers.ApiConstants;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.AuthInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.HeadersInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    public static Retrofit createInstance() {

        ApiConfiguration configuration = ApiConfiguration.getInstance();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeadersInterceptor())
                .addInterceptor(new AuthInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(configuration.getApiURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
