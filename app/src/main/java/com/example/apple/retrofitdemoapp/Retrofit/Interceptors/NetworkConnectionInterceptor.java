package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import com.example.apple.retrofitdemoapp.Exceptions.NoConnectionException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class NetworkConnectionInterceptor implements Interceptor {

    public abstract boolean isNetworkAvailable();

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isNetworkAvailable()) {
            throw new NoConnectionException();
        }

        Request request = chain.request();
        return chain.proceed(request);
    }
}
