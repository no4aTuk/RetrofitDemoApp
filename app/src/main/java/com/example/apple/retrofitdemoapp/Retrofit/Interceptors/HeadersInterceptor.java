package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import com.example.apple.retrofitdemoapp.Helpers.HttpHeaders;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadersInterceptor implements Interceptor {

    private ApiConfiguration configuration = ApiConfiguration.getInstance();

    private HeadersInterceptor() {}

    public HeadersInterceptor(ApiConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        //final ApiConfiguration configuration = ApiConfiguration.getInstance();

        Request request = original.newBuilder()
                .header(HttpHeaders.Accept.toString(), "application/json")
                .header(HttpHeaders.AcceptLanguage.toString(), configuration.getLanguage())
                .header(HttpHeaders.AppType.toString(), configuration.getAppType())
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);
    }
}
