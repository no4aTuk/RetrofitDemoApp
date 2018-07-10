package com.example.apple.retrofitdemoapp.di.modules;

import com.example.apple.retrofitdemoapp.di.PerDialog;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.profileData.dialog.SaveWeightInteractor;
import com.example.apple.retrofitdemoapp.profileData.dialog.WeightDialogPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public interface ProfileDialogModule {

    @PerDialog
    @Binds
    Interactor interactor(SaveWeightInteractor saveWeightInteractor);

    @PerDialog
    @Binds
    BasePresenter presenter(WeightDialogPresenter profileDataInteractor);
}
