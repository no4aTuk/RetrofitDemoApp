package com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService;

import com.example.apple.retrofitdemoapp.Constants.ApiConstants;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Models.UserPermissions;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public final class AuthService extends BaseApiService {
    private static IAuthService sServiceInstance = sRetrofit.create(IAuthService.class);

    public static void token(String userName, String password, final OnRequestComplete<Token> completeCallback) {
        Call<Token> tokenRequest = sServiceInstance.token(ApiConstants.GRANT_TYPE_PASSWORD, userName,
                password, ApiConstants.CLIENT_ID);

        proceedAsync(tokenRequest, new OnRequestComplete<Token>() {
            @Override
            public void onSuccess(Token result) {
                if (result == null) {
                    completeCallback.onFail(null);
                    return;
                }

                updateAccessToken(result);
                completeCallback.onSuccess(result);
            }

            @Override
            public void onFail(ErrorResult error) {
                completeCallback.onFail(error);
            }
        });
    }

    public static int refreshToken() {
        String refreshToken = CredentialsStorage.getInstance().getRefreshToken();
        Call<Token> tokenRequest = sServiceInstance.refreshToken(ApiConstants.GRANT_TYPE_REFRESH_TOKEN, refreshToken, ApiConstants.CLIENT_ID);
        int statusCode = 0;
        try {
            Response<Token> response = tokenRequest.execute();
            if (response.isSuccessful()) {

                Token newToken = response.body();
                if (newToken != null) {
                    updateAccessToken(newToken);
                }
            }
            statusCode = response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statusCode;
    }

    public static void userPermissions(OnRequestComplete<UserPermissions> completeCallback) {
        Call<UserPermissions> permissionRequest = sServiceInstance.userPermissions();
        proceedAsync(permissionRequest, completeCallback);
    }

    private static void updateAccessToken(Token token) {
        CredentialsStorage credentialsStorage = CredentialsStorage.getInstance();
        credentialsStorage.setToken(String.format("%s %s", token.token_type, token.access_token));
        credentialsStorage.setRefreshToken(token.refresh_token);
    }
}
