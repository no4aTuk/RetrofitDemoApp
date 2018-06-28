package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import android.util.Log;

import com.example.apple.retrofitdemoapp.Helpers.HttpHeaders;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadersInterceptor implements Interceptor {

    private final ApiConfiguration configuration = ApiConfiguration.getInstance();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request = original.newBuilder()
                .header(HttpHeaders.Accept.toString(), "application/json")
                .header(HttpHeaders.AcceptLanguage.toString(), configuration.getLanguage())
                .header(HttpHeaders.AppType.toString(), configuration.getAppType())
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);
    }
}
