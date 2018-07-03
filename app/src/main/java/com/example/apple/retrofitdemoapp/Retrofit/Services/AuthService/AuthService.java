package com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService;

import android.util.Log;

import com.example.apple.retrofitdemoapp.Constants.ApiConstants;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Models.UserPermissions;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class AuthService extends BaseApiService {
    private IAuthService service;

    public AuthService(IAuthService service, ApiConfiguration apiConfiguration, CredentialsStorage credentialsStorage) {
        super(apiConfiguration, credentialsStorage);
        this.service = service;
    }

    public void token(String userName, String password, final OnRequestComplete<Token> completeCallback) {
        Call<Token> tokenRequest = service.token(ApiConstants.GRANT_TYPE_PASSWORD, userName,
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

    public int refreshToken() {
        String refreshToken = credentialsStorage.getRefreshToken();
        Call<Token> tokenRequest = service.refreshToken(ApiConstants.GRANT_TYPE_REFRESH_TOKEN, refreshToken, ApiConstants.CLIENT_ID);
        int statusCode = 0;
        try {
            Response<Token> response = tokenRequest.execute();
            if (response.isSuccessful()) {

                Token newToken = response.body();
                if (newToken != null) {
                    Log.d("TOKEN", "refreshToken: " + newToken.access_token);
                    updateAccessToken(newToken);
                }
            }
            statusCode = response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return statusCode;
    }

    public void userPermissions(OnRequestComplete<UserPermissions> completeCallback) {
        Call<UserPermissions> permissionRequest = service.userPermissions();
        proceedAsync(permissionRequest, completeCallback);
    }

    private void updateAccessToken(Token token) {
        credentialsStorage.setToken(String.format("%s %s", token.token_type, token.access_token));
        credentialsStorage.setRefreshToken(token.refresh_token);
    }
}
