package com.example.apple.retrofitdemoapp.Components;

import com.example.apple.retrofitdemoapp.Modules.ApiModule;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService2;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.IAuthService;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import dagger.Component;
import retrofit2.Retrofit;

@ApiApplicationScope
@Component(modules = {ApiModule.class})
public interface ApiServiceComponent {
    AuthService2 authService();
    ApiConfiguration apiConfiguration();
    CredentialsStorage credentialStorage();
    Retrofit retrofit();
}
