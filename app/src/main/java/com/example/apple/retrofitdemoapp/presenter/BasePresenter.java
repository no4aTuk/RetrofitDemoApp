package com.example.apple.retrofitdemoapp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;
import com.example.apple.retrofitdemoapp.utils.ObjectUtils;

public abstract class BasePresenter<View extends MvpView, Entity> extends MvpPresenter<View> implements Presenter {

    private Bundle mInitialData;
    private boolean mIsInitialDataLoaded;

    public void setInitialData(Bundle data) {
        if (data != null && (mInitialData == null || !ObjectUtils.equalBundles(mInitialData, data))) {
            this.mInitialData = data;
            this.mIsInitialDataLoaded = false;
            if (getInitInteractor() != null) {
                getInitInteractor().setData(mInitialData);
            }
        }
    }

    @Override
    protected void onFirstViewAttach() {
        if (getInitInteractor() == null) {
            return;
        }
        getInitInteractor().setData(mInitialData);
        getInitInteractor().setStreamEventsHandler(notification -> {
            if (notification.isOnNext()) {
                mIsInitialDataLoaded = true;
            }
        });
        loadInitialData();
    }

    public void loadInitialData() {
        if (getInitInteractor() == null || getInitSubscriber() == null) {
            return;
        }
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
        if (getInitInteractor() == null) {
            return;
        }
        getInitInteractor().unsubscribe();
    }

    @Nullable
    protected abstract Interactor<Entity> getInitInteractor();

    @Nullable
    protected abstract DefaultSubscriber<Entity> getInitSubscriber();
}
