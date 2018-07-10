package com.example.apple.retrofitdemoapp.profileData.activity;

import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;
import com.example.apple.retrofitdemoapp.interactor.Interactor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class AuthInteractor extends Interactor<Token> {

    private AuthService mService;

    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";

    @Inject
    public AuthInteractor(AuthService service) {
        this.mService = service;
    }

    @Override
    protected Observable<Token> buildObservable() {
        String user = getData().getString(USER_NAME);
        String password = getData().getString(PASSWORD);
        return mService.getToken(user, password);
    }
}
