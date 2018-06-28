package com.example.apple.retrofitdemoapp.Models;

public class ErrorResult {
    private int statusCode;
    private String message;

    public ErrorResult(int code, String msg) {
        this.statusCode = code;
        this.message = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
