package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import com.example.apple.retrofitdemoapp.Helpers.HttpCodes;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Request original = chain.request();

        final Response response = chain.proceed(original);
        if (response.code() == HttpCodes.UNAUTHORIZED) {
            //refresh token
            //TODO get refresh token value
            String newAccessToken = AuthService.refreshToken(CredentialsStorage.refreshtoken);
            Request newRequest = original.newBuilder()
                    .headers(original.headers())
                    .header("Authorization", newAccessToken)
                    .method(original.method(), original.body())
                    .build();
            chain.proceed(newRequest);
        }

        return response;
    }
}
