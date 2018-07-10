package com.example.apple.retrofitdemoapp.di.components;

import android.content.Context;

import com.example.apple.retrofitdemoapp.BaseApplication;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.di.modules.ApiModule;
import com.example.apple.retrofitdemoapp.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        @BindsInstance
        Builder api(ApiConfiguration apiConfiguration);

        @BindsInstance
        Builder storage(CredentialsStorage storage);

        ApplicationComponent build();
    }

    void inject(BaseApplication application);
}
