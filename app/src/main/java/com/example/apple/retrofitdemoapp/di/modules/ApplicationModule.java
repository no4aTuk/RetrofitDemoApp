package com.example.apple.retrofitdemoapp.di.modules;


import com.example.apple.retrofitdemoapp.di.PerActivity;
import com.example.apple.retrofitdemoapp.profileData.ActivityProfileData;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = {AndroidSupportInjectionModule.class})
public abstract class ApplicationModule {

    @PerActivity
    @ContributesAndroidInjector(modules = {ProfileDataModule.class})
    abstract ActivityProfileData profileData();

}
