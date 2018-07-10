package com.example.apple.retrofitdemoapp.di.modules;

import com.example.apple.retrofitdemoapp.di.PerActivity;
import com.example.apple.retrofitdemoapp.di.PerFragment;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.profileData.activity.AuthInteractor;
import com.example.apple.retrofitdemoapp.profileData.activity.MainPresenter;
import com.example.apple.retrofitdemoapp.profileData.fragment.ProfileDataFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = {AndroidSupportInjectionModule.class})
public interface MainActivityModule {

    @PerActivity
    @Binds
    Interactor interactor(AuthInteractor authInteractor);

    @PerActivity
    @Binds
    BasePresenter presenter(MainPresenter mainPresenter);

    @PerFragment
    @ContributesAndroidInjector(modules = {ProfileDataModule.class})
    ProfileDataFragment fragment();
}
