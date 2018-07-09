package com.example.apple.retrofitdemoapp.rx;

import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Exceptions.ResponseException;

import java.util.List;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.observers.DisposableObserver;

import static com.example.apple.retrofitdemoapp.Constants.ErrorCodes.SYSTEM_ERROR;


public class DefaultSubscriber<T> extends DisposableObserver<T> {

    private OnRequestComplete<T> mCallback;

    public DefaultSubscriber(OnRequestComplete<T> callback) {
        mCallback = callback;
    }

    @Override public void onError(Throwable exception) {
        if (exception instanceof CompositeException) {
            List<Throwable> exceptions = ((CompositeException) exception).getExceptions();
            exception = exceptions.get(exceptions.size() - 1);
        }
        ErrorResult result;
        if (exception instanceof ResponseException) {
            result = ((ResponseException) exception).getErrorResult();
        } else {
            exception.printStackTrace();
            result = new ErrorResult(SYSTEM_ERROR, exception.getLocalizedMessage());
        }
        mCallback.onFail(result);
        mCallback.onComplete();
    }

    @Override public void onNext(T result) {
        mCallback.onSuccess(result);
    }

    @Override
    public void onComplete() {
        mCallback.onComplete();
    }
}
