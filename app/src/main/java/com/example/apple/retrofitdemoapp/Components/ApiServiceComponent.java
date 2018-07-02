package com.example.apple.retrofitdemoapp.Components;

import com.example.apple.retrofitdemoapp.Modules.ApiModule;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService2;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.IAuthService;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import dagger.Component;

@ApiApplicationScope
@Component(modules = {ApiModule.class})
public interface ApiServiceComponent {
    AuthService2 authService();
}
