package com.example.apple.retrofitdemoapp.Helpers;

public enum HttpHeaders {
    Accept ("Accept"),
    AcceptLanguage ("Accept-Language"),
    AppType ("AppType"),
    Authorization("Authorization");

    private final String name;

    HttpHeaders(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
