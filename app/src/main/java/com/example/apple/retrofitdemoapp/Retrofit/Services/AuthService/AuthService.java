package com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService;

import com.example.apple.retrofitdemoapp.Constants.ApiConstants;
import com.example.apple.retrofitdemoapp.Retrofit.Services.ServiceHolder;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Models.UserPermissions;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public final class AuthService extends BaseApiService<IAuthService> {

    @Inject
    public AuthService(Retrofit retrofit, CredentialsStorage storage, ApiConfiguration configuration, ServiceHolder<AuthService> serviceHolder) {
        super(retrofit, configuration, storage, IAuthService.class);
        serviceHolder.setService(this);
    }

    public void token(String userName, String password, final OnRequestComplete<Token> completeCallback) {
        Call<Token> tokenRequest = getService().token(ApiConstants.GRANT_TYPE_PASSWORD, userName,
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
        String refreshToken = mCredentialsStorage.getRefreshToken();
        Call<Token> tokenRequest = getService().refreshToken(ApiConstants.GRANT_TYPE_REFRESH_TOKEN, refreshToken, ApiConstants.CLIENT_ID);
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

    public void userPermissions(OnRequestComplete<UserPermissions> completeCallback) {
        Call<UserPermissions> permissionRequest = getService().userPermissions();
        proceedAsync(permissionRequest, completeCallback);
    }

    private void updateAccessToken(Token token) {
        mCredentialsStorage.setToken(String.format("%s %s", token.token_type, token.access_token));
        mCredentialsStorage.setRefreshToken(token.refresh_token);
    }
}
