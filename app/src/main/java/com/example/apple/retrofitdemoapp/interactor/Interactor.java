package com.example.apple.retrofitdemoapp.interactor;

import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public abstract class Interactor<T> {

    private DisposableObserver<T> mSubscription;
    private Consumer<Notification<T>> mStreamHandler = notification -> {};
    private Bundle mData;

    public void execute(@NonNull DisposableObserver<T> subscriber) {
        mSubscription =  this.buildObservable()
                .doOnEach(getStreamHandler())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
    }

    public void unsubscribe() {
        if (!mSubscription.isDisposed()) {
            mSubscription.dispose();
        }
    }

    public void setStreamEventsHandler(@NonNull Consumer<Notification<T>> streamHandler) {
        mStreamHandler = streamHandler;
    }

    protected Consumer<Notification<T>> getStreamHandler() {
        return mStreamHandler;
    }

    public final void setData(Bundle data) {
        mData = data;
    }

    protected final Bundle getData() {
        return mData;
    }

    protected abstract Observable<T> buildObservable();
}
