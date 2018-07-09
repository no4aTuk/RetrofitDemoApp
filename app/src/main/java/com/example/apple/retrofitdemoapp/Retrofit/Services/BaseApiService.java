package com.example.apple.retrofitdemoapp.Retrofit.Services;

import com.example.apple.retrofitdemoapp.Constants.ErrorCodes;
import com.example.apple.retrofitdemoapp.Exceptions.NoConnectionException;
import com.example.apple.retrofitdemoapp.Models.BackendError;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Exceptions.ResponseException;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.exceptions.Exceptions;

public abstract class BaseApiService<C> {

    protected final Retrofit mRetrofit;
    protected final ApiConfiguration mConfiguration;
    protected final CredentialsStorage mCredentialsStorage;
    private Class<C> mServiceClass;
    private C mServiceInstance;

    public BaseApiService(Retrofit mRetrofit, ApiConfiguration configuration, CredentialsStorage credentialsStorage, Class<C> serviceClass) {
        this.mRetrofit = mRetrofit;
        this.mConfiguration = configuration;
        this.mCredentialsStorage = credentialsStorage;
        this.mServiceClass = serviceClass;
        initService();
    }

    private void initService() {
        mServiceInstance = mRetrofit.create(mServiceClass);
    }

    protected C getService() {
        return mServiceInstance;
    }

    protected <T> void proceedAsync(Call<T> request, final OnRequestComplete<T> callback) {
        request.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                handleSuccessResult(response, callback);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                handleFailResult(t, callback);
            }
        });
    }

    protected <T> Observable<T> validate(Observable<Response<T>> observable) {
        return observable
                .flatMap(this::handleSuccess)
                .doOnError(this::handleError);
    }

    protected <T> void proceedSync(Call<T> request, final OnRequestComplete<T> callback) {
        try {
            Response<T> response = request.execute();
            handleSuccessResult(response, callback);
        } catch (IOException ioexception) {
            handleFailResult(ioexception, callback);
        }
    }

    private <T> void handleSuccessResult(Response<T> response, OnRequestComplete<T> callback) {
        if (response.isSuccessful() && callback != null) {
            callback.onSuccess(response.body());
        } else if (callback != null) {
            callback.onFail(getServerError(response));
        }
    }

    private <T> Observable<T> handleResponse(Response<T> response) {
        if (!response.isSuccessful() || response.body() == null) {
            return Observable.error(new ResponseException(getServerError(response)));
        } else {
            return Observable.just(response.body());
        }
    }

    private <T> void handleFailResult(Throwable t, OnRequestComplete<T> callback) {
        if (callback == null) return;

        callback.onFail(getServerError(t));
    }

    public void handleError(Throwable throwable) {
        if (!(throwable instanceof ResponseException)) {
            ErrorResult errorResult = getServerError(throwable);
            throwable = new ResponseException(errorResult);
        }
        throw Exceptions.propagate(throwable);
    }

    public <T> Observable<T> handleSuccess(Response<T> response) {
        if (!response.isSuccessful()) {
            return Observable.error(new ResponseException(getServerError(response)));
        } else {
            T body = response.body();
            if (body == null) {
                return Observable.create(Emitter::onComplete);
            } else {
                return Observable.just(response.body());
            }
        }
    }

    public static ErrorResult getServerError(Throwable t) {
        String message = t.getLocalizedMessage();
        int statusCode;
        if (t instanceof NoConnectionException) {
            statusCode = ErrorCodes.NO_CONNECTION;
        } else {
            statusCode = ErrorCodes.SERVER_UNAVAILABLE;
        }

        return new ErrorResult(statusCode, message);
    }

    public static ErrorResult getServerError(Response response) {

        int code = response.code();
        switch (code) {
            case ErrorCodes.NOT_FOUND:
            case ErrorCodes.BAD_REQUEST:
            case ErrorCodes.INTERNAL_ERROR:
            case ErrorCodes.NO_CONNECTION:
        }

        if (response.errorBody() == null) {
            return new ErrorResult(code, "");
        }

        try {
            String jsonString;
            jsonString = response.errorBody().string();
            BackendError backendError = new Gson().fromJson(jsonString, BackendError.class);
            String errorMessage = backendError.getErrorMessage();
            return new ErrorResult(code, errorMessage);
        } catch (com.google.gson.JsonSyntaxException ilse) {
            ilse.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ErrorResult(code, "");
    }
}
