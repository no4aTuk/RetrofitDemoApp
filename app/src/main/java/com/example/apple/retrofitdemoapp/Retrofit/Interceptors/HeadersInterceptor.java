package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import android.util.Log;

import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        ApiConfiguration configuration = ApiConfiguration.getInstance();
        CredentialsStorage credStorage = CredentialsStorage.getInstance();
        if (credStorage.getToken() == null) {
            return chain.proceed(original);
        }

        Request request = original.newBuilder()
                .header("Accept", "application/json")
                .header("Accept-Language", configuration.getLanguage())
                .header("AppType", configuration.getAppType())
                .header("Authorization",  credStorage.getToken())
                .method(original.method(), original.body())
                .build();

        long t1 = System.nanoTime();
        Log.d("#INTERCEPTOR", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.d("#INTERCEPTOR", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
