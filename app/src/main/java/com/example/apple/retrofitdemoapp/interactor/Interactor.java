package com.example.apple.retrofitdemoapp.interactor;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.apple.retrofitdemoapp.Retrofit.ResultHandlers.ApiResultHandler;
import com.example.apple.retrofitdemoapp.profileData.BaseEntity;
import com.example.apple.retrofitdemoapp.profileData.ProfileDataEntity;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public abstract class Interactor<T extends BaseEntity> {

    private DisposableObserver<T> mSubscription;

    protected Interactor() {
    }

    public void execute(@NonNull DisposableObserver<T> subscriber) {
        mSubscription = this.buildObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
    }

    /**
     * Unsubscribes from current {@link Disposable}.
     */
    public void unsubscribe() {
        if (!mSubscription.isDisposed()) {
            mSubscription.dispose();
        }
    }

    protected abstract Observable<T> buildObservable();

    public abstract void setParams(Bundle data);

    protected abstract ApiResultHandler<T> getResultHandler();
}
