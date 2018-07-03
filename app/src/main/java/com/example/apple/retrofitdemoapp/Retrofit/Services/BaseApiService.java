package com.example.apple.retrofitdemoapp.Retrofit.Services;

import com.example.apple.retrofitdemoapp.Constants.ErrorCodes;
import com.example.apple.retrofitdemoapp.Exceptions.NoConnectionException;
import com.example.apple.retrofitdemoapp.Models.BackendError;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseApiService {

    protected ApiConfiguration configuration;
    protected CredentialsStorage credentialsStorage;

    public BaseApiService(ApiConfiguration configuration, CredentialsStorage credentialsStorage) {
        this.configuration = configuration;
        this.credentialsStorage = credentialsStorage;
    }

    public <T> void proceedAsync(Call<T> request, final OnRequestComplete<T> callback) {
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

    public <T> void proceedSync(Call<T> request, final OnRequestComplete<T> callback) {
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

    private <T> void handleFailResult(Throwable t, OnRequestComplete<T> callback) {
        if (callback == null) return;

        String message = t.getLocalizedMessage();
        int statusCode;
        if (t instanceof NoConnectionException) {
            statusCode = ErrorCodes.NO_CONNECTION;
        } else {
            statusCode = ErrorCodes.SERVER_UNAVAILABLE;
        }
        if (configuration.getListener() != null) {
            message = configuration.getListener().getLocalizedError(statusCode);
        }

        callback.onFail(new ErrorResult(statusCode, message));
    }

    private ErrorResult getServerError(int code, Response response) {

        String errorMessage = "";
        switch (code) {
            case ErrorCodes.NOT_FOUND:
            case ErrorCodes.INTERNAL_ERROR:
            case ErrorCodes.NO_CONNECTION:
                if (configuration.getListener() != null) {
                    return new ErrorResult(code, errorMessage);
                }
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
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return new ErrorResult(code, "");
    }
}
