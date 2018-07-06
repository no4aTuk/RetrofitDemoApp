package com.example.apple.retrofitdemoapp.Retrofit.Services;


import javax.inject.Singleton;

@Singleton
public class ServiceHolder<T extends BaseApiService> {

    private T service;

    public T getService() {
        return service;
    }

    public void setService(T service) {
        this.service = service;
    }


}
