package com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService;

import com.example.apple.retrofitdemoapp.Models.SignUp;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Models.UserPermissions;
import com.example.apple.retrofitdemoapp.Models.VerifyCode;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface IAuthService {
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
