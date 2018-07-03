package com.example.apple.retrofitdemoapp.Modules;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Qualifiers.ApplicationContext;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {

    private Context mContext;

    public AppContextModule(Context context) {
        this.mContext = context;
    }

    @ApiApplicationScope
    @ApplicationContext
    @Provides
    public Context context() {
        return mContext.getApplicationContext();
    }
}
