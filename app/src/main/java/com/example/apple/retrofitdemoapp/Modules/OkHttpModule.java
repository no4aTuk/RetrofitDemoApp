package com.example.apple.retrofitdemoapp.Modules;

import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnFileRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.AuthInterceptorSync;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.FileDownloadProgressInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.HeadersInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Interceptors.NetworkConnectionInterceptor;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthServiceHolder;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.FileService;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = {ApiConfigurationModule.class, CredentialsStorageModule.class})
public class OkHttpModule {

    private static final int DEFAULT_TIMEOUT = 10; // seconds
    private static final int READ_WRITE_TIMEOUT = 5; //minutes

    @Provides
    public OkHttpClient okHttpClient(HeadersInterceptor headersInterceptor, AuthInterceptorSync authInterceptorSync,
                        NetworkConnectionInterceptor networkConnectionInterceptor,
                        FileDownloadProgressInterceptor fileDownloadProgressInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_WRITE_TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(READ_WRITE_TIMEOUT, TimeUnit.MINUTES)
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(headersInterceptor)
                .addInterceptor(authInterceptorSync)
                .addNetworkInterceptor(fileDownloadProgressInterceptor)
                .build();
    }

    @Provides
    public HeadersInterceptor headersInterceptor(ApiConfiguration apiConfiguration) {
        return new HeadersInterceptor(apiConfiguration);
    }

    @Provides
    AuthInterceptorSync authInterceptorSync(AuthServiceHolder authServiceHolder, ApiConfiguration apiConfiguration, CredentialsStorage credentialsStorage) {
        return new AuthInterceptorSync(credentialsStorage, apiConfiguration, authServiceHolder);
    }

    @ApiApplicationScope
    @Provides
    public AuthServiceHolder authServiceHolder() {
        return new AuthServiceHolder();
    }

    @Provides
    NetworkConnectionInterceptor networkConnectionInterceptor(final ApiConfiguration apiConfiguration) {
        return new NetworkConnectionInterceptor() {
            @Override
            public boolean isNetworkAvailable() {
                return apiConfiguration.getListener().isNetWorkAvailable();
            }
        };
    }

    //    @Provides
//    FileDownloadProgressInterceptor fileDownloadProgressInterceptor(final Map<String, OnFileRequestComplete<File>> downloadProgressCallbacks) {
//        return new FileDownloadProgressInterceptor() {
//            @Override
//            public Map<String, OnFileRequestComplete<File>> downloadProgressListeners() {
//                return downloadProgressCallbacks;
//            }
//        };
//    }
    @Provides
    FileDownloadProgressInterceptor fileDownloadProgressInterceptor() {
        return new FileDownloadProgressInterceptor() {
            @Override
            public Map<String, OnFileRequestComplete<File>> downloadProgressListeners() {
                return FileService.downloadFileListener; //TODO need to decide what implementation is better
            }
        };
    }
}
