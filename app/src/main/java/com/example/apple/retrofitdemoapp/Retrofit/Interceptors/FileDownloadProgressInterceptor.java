package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnFileRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.ProgressResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;

public abstract class FileDownloadProgressInterceptor implements Interceptor {

    public abstract Map<String, OnFileRequestComplete<File>> downloadProgressListeners();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (downloadProgressListeners() == null || downloadProgressListeners().size() == 0) return originalResponse;

        //Workaround to check what file is downloading now
        OnFileRequestComplete<File> downloadProgressListener = getProgressListenerForDownloadableFileId(originalResponse);
        if (downloadProgressListener == null) return originalResponse;

        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), downloadProgressListener))
                .build();
    }

    private OnFileRequestComplete<File> getProgressListenerForDownloadableFileId(Response response) {
        List<String> urlSegments = response.request().url().pathSegments();
        String fileId = urlSegments.get(urlSegments.size() - 1);
        if (fileId == null) {
            return null;
        }
        return downloadProgressListeners().get(fileId);
    }
}
