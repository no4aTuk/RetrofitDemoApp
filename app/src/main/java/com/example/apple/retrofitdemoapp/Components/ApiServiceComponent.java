package com.example.apple.retrofitdemoapp.Components;

import com.example.apple.retrofitdemoapp.MainActivity;
import com.example.apple.retrofitdemoapp.Modules.ApiModule;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import dagger.Component;

@ApiApplicationScope
@Component(modules = {ApiModule.class})
public interface ApiServiceComponent {
    void inject(MainActivity activity);
}
