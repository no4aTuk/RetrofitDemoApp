package com.example.apple.retrofitdemoapp.interactor;

import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public abstract class Interactor<Entity> {

    private DisposableObserver<Entity> mSubscription;
    private Consumer<Notification<Entity>> mStreamHandler = notification -> {};
    private Bundle mData;

    public void execute(@NonNull DisposableObserver<Entity> subscriber) {
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

    public void setStreamEventsHandler(@NonNull Consumer<Notification<Entity>> streamHandler) {
        mStreamHandler = streamHandler;
    }

    protected Consumer<Notification<Entity>> getStreamHandler() {
        return mStreamHandler;
    }

    public final void setData(Bundle data) {
        mData = data;
    }

    protected final Bundle getData() {
        return mData;
    }

    protected abstract Observable<Entity> buildObservable();
}
