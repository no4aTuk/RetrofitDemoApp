package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnFileRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.ProgressResponseBody;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public abstract class FileDownloadProgressInterceptor implements Interceptor {

    public abstract OnFileRequestComplete<File> downloadProgressListener();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //if (FileService.downloadFileListener == null) return originalResponse;
        if (downloadProgressListener() == null) return originalResponse;

        return originalResponse.newBuilder()
                //.body(new ProgressResponseBody(originalResponse.body(), FileService.downloadFileListener))
                .body(new ProgressResponseBody(originalResponse.body(), downloadProgressListener()))
                .build();
    }
}
