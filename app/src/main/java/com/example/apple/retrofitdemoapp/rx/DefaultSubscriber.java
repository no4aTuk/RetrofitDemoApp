package com.example.apple.retrofitdemoapp.rx;

import io.reactivex.observers.DisposableObserver;


public class DefaultSubscriber<T> extends DisposableObserver<T> {

    public DefaultSubscriber() {
    }

    @Override public void onError(Throwable e) {

    }

    @Override public void onNext(T t) {

    }

    @Override
    public void onComplete() {

    }
}
