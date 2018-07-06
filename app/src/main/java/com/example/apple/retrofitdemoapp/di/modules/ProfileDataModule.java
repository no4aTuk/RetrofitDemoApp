package com.example.apple.retrofitdemoapp.di.modules;

import com.example.apple.retrofitdemoapp.di.PerActivity;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.profileData.ProfileDataInteractor;

import dagger.Binds;
import dagger.Module;

@Module
public interface ProfileDataModule {

    @PerActivity
    @Binds
    Interactor interactor(ProfileDataInteractor profileDataInteractor);
}
