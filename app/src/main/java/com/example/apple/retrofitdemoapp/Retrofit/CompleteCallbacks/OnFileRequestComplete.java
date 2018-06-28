package com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks;

import java.io.File;

public interface OnFileRequestComplete<T> extends OnRequestComplete<T> {
    void onProgress(int percents);
}
