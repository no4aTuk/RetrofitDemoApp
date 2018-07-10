package com.example.apple.retrofitdemoapp.Retrofit.Services;

import android.content.Context;
import android.util.Log;

import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Exceptions.ResponseException;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.exceptions.Exceptions;

import static com.example.apple.retrofitdemoapp.Models.ErrorResult.getServerError;

public abstract class BaseApiService<C> {

    public static final String TAG = BaseApiService.class.getSimpleName();

    protected final Retrofit mRetrofit;
    protected final ApiConfiguration mConfiguration;
    protected final CredentialsStorage mCredentialsStorage;

    private final Context mContext;
    private Class<C> mServiceClass;
    private C mServiceInstance;

    public BaseApiService(Context context, Retrofit mRetrofit, ApiConfiguration configuration,
                          CredentialsStorage credentialsStorage, Class<C> serviceClass) {
        this.mRetrofit = mRetrofit;
        this.mConfiguration = configuration;
        this.mCredentialsStorage = credentialsStorage;
        this.mServiceClass = serviceClass;
        this.mContext = context;
        initService();
    }

    private void initService() {
        mServiceInstance = mRetrofit.create(mServiceClass);
    }

    protected C getService() {
        return mServiceInstance;
    }

    protected <T> Observable<T> validate(Observable<Response<T>> observable) {
        return observable
                .flatMap(this::handleSuccess)
                .doOnError(this::handleError)
                .take(1);
    }

    private void handleError(Throwable throwable) {
        if (!(throwable instanceof ResponseException)) {
            Log.e("Internal error", TAG, throwable);
            ErrorResult errorResult = getServerError(mContext, throwable);
            throwable = new ResponseException(errorResult);
        }
        throw Exceptions.propagate(throwable);
    }

    private <T> Observable<T> handleSuccess(Response<T> response) {
        if (!response.isSuccessful()) {
            return Observable.error(new ResponseException(getServerError(mContext, response)));
        } else {
            T body = response.body();
            if (body == null) {
                return Observable.create(Emitter::onComplete);
            } else {
                return Observable.just(response.body());
            }
        }
    }

}
