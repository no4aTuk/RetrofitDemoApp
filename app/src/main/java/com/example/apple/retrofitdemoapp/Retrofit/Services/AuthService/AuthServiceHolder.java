package com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService;

public class AuthServiceHolder {
    private AuthService authService = null;

    public AuthService get() {
        return authService;
    }

    public void set(AuthService tokenService) {
        this.authService = tokenService;
    }
}
