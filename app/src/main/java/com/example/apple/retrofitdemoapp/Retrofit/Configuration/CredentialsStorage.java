package com.example.apple.retrofitdemoapp.Retrofit.Configuration;

import com.example.apple.retrofitdemoapp.Models.Token;

import javax.inject.Singleton;

@Singleton
public class CredentialsStorage {

    private String mToken;
    private String mRefreshToken;
    private ApiConfiguration.ApiConfigurationListener configurationListener;

    public CredentialsStorage(ApiConfiguration.ApiConfigurationListener configurationListener) {
        this.configurationListener = configurationListener;
    }

    public void setup(Token token) {
        setToken(String.format("%s %s", token.token_type, token.access_token));
        setRefreshToken(token.refresh_token);
        configurationListener.onTokenChanged(token);
    }

    public String getToken() {
        return mToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setToken(String accessToken) {
        mToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
    }
}
