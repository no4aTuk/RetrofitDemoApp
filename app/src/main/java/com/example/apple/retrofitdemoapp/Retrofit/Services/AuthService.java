package com.example.apple.retrofitdemoapp.Retrofit.Services;

import com.example.apple.retrofitdemoapp.Helpers.ApiConstants;
import com.example.apple.retrofitdemoapp.Models.SignUp;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Models.UserPermissions;
import com.example.apple.retrofitdemoapp.Models.VerifyCode;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class AuthService extends BaseApiService {
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
            public void onFail(String error) {
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
                    //newAccessToken = String.format("%s %s", newToken.token_type, newToken.access_token);
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

    private interface IAuthService {
        String rootPath = "v1/auth/";

        @POST(rootPath + "signup")
        Call<Void> signUp(@Body SignUp model);

        @POST(rootPath + "verify-code")
        Call<Void> verifyCode(@Body VerifyCode model);

        @GET(rootPath + "permissions")
        Call<UserPermissions> userPermissions();

        @FormUrlEncoded
        @POST(rootPath + "token")
        Call<Token> token(@Field("grant_type") String grant_type, @Field("username") String userName,
                         @Field("password") String password, @Field("client_id") String clientId);

        @FormUrlEncoded
        @POST(rootPath + "token")
        Call<Token> refreshToken(@Field("grant_type") String grant_type,
                                 @Field("refresh_token") String refreshToken, @Field("client_id") String clientId);
    }
}
