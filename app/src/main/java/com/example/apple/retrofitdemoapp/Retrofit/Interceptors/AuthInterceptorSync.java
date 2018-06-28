package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import com.example.apple.retrofitdemoapp.Helpers.HttpCodes;
import com.example.apple.retrofitdemoapp.Helpers.HttpHeaders;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptorSync implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        CredentialsStorage credentialsStorage = CredentialsStorage.getInstance();
        String requestToken = credentialsStorage.getToken();

        Request request = chain.request();
        //Set Auth header
        Request.Builder builder = request.newBuilder();
        setAuthHeader(builder, requestToken);
        request = builder.build();

        //Perform request
        Response response = chain.proceed(request);

        if (response.code() == HttpCodes.UNAUTHORIZED ||
                response.code() == HttpCodes.FORBIDDEN) {

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
        ApiConfiguration configuration = ApiConfiguration.getInstance();
        if (configuration.getListener() != null) {
            configuration.getListener().OnTokenExpired();
        }
    }
}
