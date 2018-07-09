package com.example.apple.retrofitdemoapp.presenter;

import android.os.Bundle;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;

public abstract class BasePresenter<T extends MvpView, K> extends MvpPresenter<T> implements Presenter {

    private Bundle mInitialData;
    private boolean mIsInitialDataLoaded;

    public void setInitialData(Bundle data) {
        if (data != null && (mInitialData == null || !mInitialData.equals(data))) {
            this.mInitialData = data;
            this.mIsInitialDataLoaded = false;
            if (getInitInteractor() != null) {
                getInitInteractor().setData(mInitialData);
            }
        }
    }

    @Override
    protected void onFirstViewAttach() {
        getInitInteractor().setData(mInitialData);
        getInitInteractor().setStreamEventsHandler(notification -> {
            if (notification.isOnNext()) {
                mIsInitialDataLoaded = true;
            }
        });
        loadInitialData();
    }

    public void loadInitialData() {
        getInitInteractor().execute(getInitSubscriber());
    }

    @Override
    public void resume() {
        if (!mIsInitialDataLoaded) {
            loadInitialData();
        }
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
