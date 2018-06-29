package com.example.apple.retrofitdemoapp.Exceptions;

import java.io.IOException;

public class NoConnectionException extends IOException {
    @Override
    public String getMessage() {
        return "No Internet connection";
    }
}
