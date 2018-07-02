package com.example.apple.retrofitdemoapp.Helpers;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Constants.ErrorCodes;
import com.example.apple.retrofitdemoapp.R;

public class ApiErrorHelper {
    public static String getErrorByCode(Context context, int statusCode) {
        switch (statusCode) {
            case ErrorCodes.NOT_FOUND: return context.getString(R.string.not_found_error);
            case ErrorCodes.BAD_REQUEST: return context.getString(R.string.bad_request_error);
            case ErrorCodes.INTERNAL_ERROR: return context.getString(R.string.internal_error);
            case ErrorCodes.NO_CONNECTION: return context.getString(R.string.no_connection_error);
            case ErrorCodes.SERVER_UNAVAILABLE: return context.getString(R.string.timeout_error);
            default: return context.getString(R.string.unknown_error);
        }
    }
}