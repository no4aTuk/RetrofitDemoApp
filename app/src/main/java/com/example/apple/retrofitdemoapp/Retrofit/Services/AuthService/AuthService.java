package com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService;

import android.content.Context;

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

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public final class AuthService extends BaseApiService<IAuthService> {

    @Inject
    public AuthService(Context context, Retrofit retrofit, CredentialsStorage storage,
                       ApiConfiguration configuration, ServiceHolder<AuthService> serviceHolder) {
        super(context, retrofit, configuration, storage, IAuthService.class);
        serviceHolder.setService(this);
    }

    public Observable<Token> getToken(String userName, String password) {
        return validate(getService().getToken(ApiConstants.GRANT_TYPE_PASSWORD, userName,
                password, ApiConstants.CLIENT_ID))
                .doOnNext(this::updateAccessToken);
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

    public void updateAccessToken(Token token) {
        mCredentialsStorage.setup(token);
    }
}
