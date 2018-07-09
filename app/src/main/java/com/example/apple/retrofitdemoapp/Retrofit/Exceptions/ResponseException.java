package com.example.apple.retrofitdemoapp.Retrofit.Exceptions;

import com.example.apple.retrofitdemoapp.Models.ErrorResult;

public class ResponseException extends RuntimeException {

    public ErrorResult getErrorResult() {
        return errorResult;
    }

    private ErrorResult errorResult;

    public ResponseException(ErrorResult errorResult) {

        this.errorResult = errorResult;
    }
}
