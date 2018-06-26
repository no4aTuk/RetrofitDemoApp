package com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks;

public interface OnRequestComplete<T> {
    void onSuccess(T result);
    void onFail(String error);
}
