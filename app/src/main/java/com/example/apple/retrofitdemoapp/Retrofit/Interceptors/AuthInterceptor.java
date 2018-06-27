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

        CredentialsStorage credentialsStorage = CredentialsStorage.getInstance();

        final Response response = chain.proceed(original);
        if (response.code() == HttpCodes.UNAUTHORIZED) {
            //refresh token
            String refreshToken = credentialsStorage.getRefreshToken();
            String newAccessToken = AuthService.refreshToken(refreshToken);
            if (newAccessToken == null) {
                chain.call().cancel();
                return response;
            }
            Request newRequest = original.newBuilder()
                    .header("Authorization", newAccessToken)
                    .build();
            return chain.proceed(newRequest);
        }

        return response;
    }
}
