package com.example.apple.retrofitdemoapp.Retrofit;

import com.example.apple.retrofitdemoapp.Components.ApiServiceComponent;

public class ApiEntryPoint {
    private static ApiEntryPoint apiEntryPoint;

    private ApiEntryPoint() { }

    public synchronized static ApiEntryPoint getInstance() {
        if (apiEntryPoint == null) {
            apiEntryPoint = new ApiEntryPoint();
        }
        return apiEntryPoint;
    }

    private ApiServiceComponent apiServiceComponent;

    public ApiServiceComponent apiServiceComponent() {
        return apiServiceComponent;
    }

    public void setApiServiceComponent(ApiServiceComponent apiServiceComponent) {
        this.apiServiceComponent = apiServiceComponent;
    }
}
