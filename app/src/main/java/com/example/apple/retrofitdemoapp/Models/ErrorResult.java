package com.example.apple.retrofitdemoapp.Models;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Constants.ErrorCodes;
import com.example.apple.retrofitdemoapp.Exceptions.NoConnectionException;
import com.example.apple.retrofitdemoapp.Helpers.ApiErrorHelper;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

import static com.example.apple.retrofitdemoapp.Constants.ErrorCodes.SYSTEM_ERROR;

public class ErrorResult {

    private int statusCode;
    private String message;

    public static final ErrorResult EMPTY = new ErrorResult(SYSTEM_ERROR, null);

    private ErrorResult(int code, String msg) {
        this.statusCode = code;
        this.message = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorResult getServerError(Context context, Throwable t) {
        int statusCode;
        if (t instanceof NoConnectionException) {
            statusCode = ErrorCodes.NO_CONNECTION;
        } else {
            statusCode = ErrorCodes.SERVER_UNAVAILABLE;
        }
        String message = ApiErrorHelper.getErrorByCode(context, statusCode);

        return new ErrorResult(statusCode, message);
    }

    public static ErrorResult getServerError(Context context, Response response) {

        int code = response.code();
        switch (code) {
            case ErrorCodes.NOT_FOUND:
            case ErrorCodes.INTERNAL_ERROR:
            case ErrorCodes.NO_CONNECTION:
                if (response.errorBody() == null) {
                    return new ErrorResult(code, ApiErrorHelper.getErrorByCode(context, code));
                }
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
        return new ErrorResult(code, ApiErrorHelper.getErrorByCode(context, code));
    }

    @Override
    public String toString() {
        return "Error " + statusCode + ": " + message;
    }
}
