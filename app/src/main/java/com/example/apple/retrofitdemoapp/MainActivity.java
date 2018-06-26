package com.example.apple.retrofitdemoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.apple.retrofitdemoapp.Helpers.Preferences;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doSomething();
    }

    public void doSomething() {
        AuthService.token("+79085111864", "Qwerty123", new OnRequestComplete<Token>() {
            @Override
            public void onSuccess(Token result) {
                Preferences.setToken(MainActivity.this, result.access_token);
                Preferences.setRefreshToken(MainActivity.this, result.refresh_token);
            }

            @Override
            public void onFail(String error) {
                //TODO show error
            }
        });
    }
}
