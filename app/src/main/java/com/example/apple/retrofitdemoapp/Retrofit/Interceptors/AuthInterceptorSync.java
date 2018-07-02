package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import android.util.Log;

import com.example.apple.retrofitdemoapp.Constants.ErrorCodes;
import com.example.apple.retrofitdemoapp.Enums.HttpHeaders;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptorSync implements Interceptor {

    private CredentialsStorage credentialsStorage;
    private ApiConfiguration configuration;

    private AuthInterceptorSync() { }

    public AuthInterceptorSync(CredentialsStorage storage, ApiConfiguration configuration) {
        this.credentialsStorage = storage;
        this.configuration = configuration;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        String requestToken = credentialsStorage.getToken();

        Request request = chain.request();
        //Set Auth header
        Request.Builder builder = request.newBuilder();
        setAuthHeader(builder, requestToken);
        request = builder.build();

        //Perform request
        Response response = chain.proceed(request);

        if (response.code() == ErrorCodes.UNAUTHORIZED ||
                response.code() == ErrorCodes.FORBIDDEN) {

            //Sync block, to avoid multiply token updates
            synchronized (this) {
                String currentToken = credentialsStorage.getToken(); //get currently stored token

                //compare current token with token that was stored before, if it was not updated - do update
                if(currentToken != null && currentToken.equals(requestToken)) {
                    int code = this.refreshToken() / 100;
                    //if refresh token failed for some reason
                    if(code != 2) {
                        if(code == 4 || code == 5) {
                            //only if response is 400 or 500 - logout
                            logout();
                        }

                        return response; //if token refresh failed - show error to user
                    }
                }

                //retry original request with new auth token
                if(credentialsStorage.getToken() != null) {
                    setAuthHeader(builder, credentialsStorage.getToken());
                    request = builder.build();
                    return chain.proceed(request); //repeat request with new token
                } else {
                    //Prevent requests with invalid token
                    return null;
                }
            }
        }

        return response;
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) {
            builder.header(HttpHeaders.Authorization.toString(), token);
        }
    }

    private int refreshToken() {
        return AuthService.refreshToken();
    }

    private void logout() {
        if (configuration == null) return;

        if (configuration.getListener() != null) {
            configuration.getListener().OnTokenExpired();
        }
    }
}
