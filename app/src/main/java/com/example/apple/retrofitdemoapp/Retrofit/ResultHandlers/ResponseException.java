package com.example.apple.retrofitdemoapp.Retrofit.ResultHandlers;

import com.example.apple.retrofitdemoapp.Models.ErrorResult;

public class ResponseException extends Exception {

    public ErrorResult getErrorResult() {
        return errorResult;
    }

    private ErrorResult errorResult;

    public ResponseException(ErrorResult errorResult) {

        this.errorResult = errorResult;
    }
}
