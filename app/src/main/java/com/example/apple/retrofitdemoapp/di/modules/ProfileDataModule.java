package com.example.apple.retrofitdemoapp.di.modules;

import com.example.apple.retrofitdemoapp.di.PerDialog;
import com.example.apple.retrofitdemoapp.di.PerFragment;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.profileData.dialog.WeightDialog;
import com.example.apple.retrofitdemoapp.profileData.fragment.ProfileDataInteractor;
import com.example.apple.retrofitdemoapp.profileData.fragment.ProfileDataPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = {AndroidSupportInjectionModule.class})
public interface ProfileDataModule {

    @PerFragment
    @Binds
    Interactor interactor(ProfileDataInteractor profileDataInteractor);

    @PerFragment
    @Binds
    BasePresenter presenter(ProfileDataPresenter profileDataPresenter);

    @PerDialog
    @ContributesAndroidInjector(modules = {ProfileDialogModule.class})
    WeightDialog dialog();
}
