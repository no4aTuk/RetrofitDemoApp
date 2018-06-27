package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import android.util.Log;

import com.example.apple.retrofitdemoapp.Helpers.HttpCodes;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
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

        ApiConfiguration configuration = ApiConfiguration.getInstance();
        CredentialsStorage credentialsStorage = CredentialsStorage.getInstance();

        final Response response = chain.proceed(original);
        if (response.code() == HttpCodes.UNAUTHORIZED) {
            //refresh token
            String refreshToken = credentialsStorage.getRefreshToken();
            Log.d("TOKEN", "DO REFRESH TOKEN");
            String newAccessToken = AuthService.refreshToken(refreshToken);
            Log.d("TOKEN", "NEW REFRESH TOKEN IS" + newAccessToken);
            if (newAccessToken == null) {
                if (configuration.getListener() != null) {
                    chain.call().cancel();
                    configuration.getListener().OnTokenExpired();
                }
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
