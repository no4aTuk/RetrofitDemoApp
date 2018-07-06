package com.example.apple.retrofitdemoapp.Retrofit.Services;

import com.example.apple.retrofitdemoapp.Constants.ErrorCodes;
import com.example.apple.retrofitdemoapp.Exceptions.NoConnectionException;
import com.example.apple.retrofitdemoapp.Models.BackendError;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.ResultHandlers.ApiResultHandler;
import com.example.apple.retrofitdemoapp.Retrofit.ResultHandlers.ResponseException;
import com.example.apple.retrofitdemoapp.profileData.ProfileDataEntity;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
                .flatMap(this::handleResponse);
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
            callback.onFail(getServerError(response.code(), response));
        }
    }

    private <T> Observable<T> handleResponse(Response<T> response) {
        if (!response.isSuccessful() || response.body() == null) {
            return Observable.error(new ResponseException(getServerError(response.code(), response)));
        } else {
            return Observable.just(response.body());
        }
    }

    private <T> void handleFailResult(Throwable t, OnRequestComplete<T> callback) {
        if (callback == null) return;

        callback.onFail(getServerError(t));
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

    public static ErrorResult getServerError(int code, Response response) {

        String errorMessage = "";
        switch (code) {
            case ErrorCodes.NOT_FOUND:
            case ErrorCodes.BAD_REQUEST:
            case ErrorCodes.INTERNAL_ERROR:
            case ErrorCodes.NO_CONNECTION:
//                if (mConfiguration.getListener() != null) {
//                    return new ErrorResult(code, errorMessage);
//                }
        }

        if (response.errorBody() == null) {
            return new ErrorResult(code, "");
        }

        try {
            String jsonString;
            jsonString = response.errorBody().string();
            BackendError backendError = new Gson().fromJson(jsonString, BackendError.class);
            errorMessage = backendError.getErrorMessage();
            return new ErrorResult(code, errorMessage);
        } catch (com.google.gson.JsonSyntaxException ilse) {
            ilse.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ErrorResult(code, "");
    }
}
