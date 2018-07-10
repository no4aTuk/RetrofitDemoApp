package com.example.apple.retrofitdemoapp.di.modules;


import com.example.apple.retrofitdemoapp.di.PerActivity;
import com.example.apple.retrofitdemoapp.profileData.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = {AndroidSupportInjectionModule.class})
public interface ApplicationModule {

    @PerActivity
    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    MainActivity profileData();

}
