package com.example.apple.retrofitdemoapp.Retrofit.Services;

import com.example.apple.retrofitdemoapp.Helpers.ApiConstants;
import com.example.apple.retrofitdemoapp.Models.SignUpModel;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Models.VerifyCodeModel;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class AuthService extends BaseApiService {
    private static IAuthService sServiceInstance = sRetrofit.create(IAuthService.class);

    public static void token(String userName, String password, final OnRequestComplete<Token> completeCallback) {
        Call<Token> tokenRequest = sServiceInstance.token(ApiConstants.GRANT_TYPE_PASSWORD, userName,
                password, ApiConstants.CLIENT_ID);
        //proceedAsync(tokenRequest, completeCallback);
        tokenRequest.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token newToken = response.body();
                    if (newToken == null) {
                        completeCallback.onFail(null);
                        return;
                    }

                    CredentialsStorage.token = newToken.access_token;
                    CredentialsStorage.refreshtoken = newToken.refresh_token;
                    completeCallback.onSuccess(response.body());
                } else {
                    completeCallback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                completeCallback.onFail(t.getLocalizedMessage());
            }
        });
    }

    public static String refreshToken(String refreshToken) {
        Call<Token> tokenRequest = sServiceInstance.refreshToken(ApiConstants.GRANT_TYPE_REFRESH_TOKEN, refreshToken);
        String newAccessToken = null;
        try {
            Response<Token> response = tokenRequest.execute();
            if (response.isSuccessful()) {

                Token newToken = response.body();
                if (newToken == null) {
                    return "";
                }

                newAccessToken = newToken.access_token;
                CredentialsStorage.token = newToken.access_token;
                CredentialsStorage.refreshtoken = newToken.refresh_token;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newAccessToken;
    }

    private interface IAuthService {
        String rootPath = "v1/auth/";

        @POST(rootPath + "signup")
        Call<Void> signUp(@Body SignUpModel model);

        @POST(rootPath + "verify-code")
        Call<Void> verifyCode(@Body VerifyCodeModel model);

        @FormUrlEncoded
        @POST(rootPath + "token")
        Call<Token> token(@Field("grant_type") String grant_type, @Field("username") String userName,
                         @Field("password") String password, @Field("client_id") String client_id);

        Call<Token> refreshToken(@Field("grant_type") String grant_type,
                                 @Field("refresh_token") String refreshToken);
    }
}
