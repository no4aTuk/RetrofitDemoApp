package com.example.apple.retrofitdemoapp.presenter;

import android.os.Bundle;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.profileData.BaseEntity;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;

public abstract class BasePresenter<T extends MvpView, K extends BaseEntity> extends MvpPresenter<T> implements Presenter {

    public void loadData(Bundle initialData) {
        getInitInteractor().setParams(initialData);
        getInitInteractor().execute(getInitSubscriber());
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        getInitInteractor().unsubscribe();
    }

    protected abstract Interactor<K> getInitInteractor();

    protected abstract DefaultSubscriber<K> getInitSubscriber();
}
