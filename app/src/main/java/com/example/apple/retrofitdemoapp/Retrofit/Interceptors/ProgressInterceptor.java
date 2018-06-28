package com.example.apple.retrofitdemoapp.Retrofit.Interceptors;

import android.util.Log;

import com.example.apple.retrofitdemoapp.Helpers.FileHelpers.ProgressResponseBody;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.FileService;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ProgressInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("INTERCEPTOR", "NETWORK INTERCEPTOR CALLED");
        Response originalResponse = chain.proceed(chain.request());
        if (FileService.downloadFileListener == null) return originalResponse;

        Log.d("INTERCEPTOR", "FileService.downloadFileListener is not NULL");
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), FileService.downloadFileListener))
                .build();
    }
}
