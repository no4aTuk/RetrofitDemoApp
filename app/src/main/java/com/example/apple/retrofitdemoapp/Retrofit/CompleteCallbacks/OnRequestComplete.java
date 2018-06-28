package com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks;

import com.example.apple.retrofitdemoapp.Models.ErrorResult;

public interface OnRequestComplete<T> {
    void onSuccess(T result);
    void onFail(ErrorResult error);
}
