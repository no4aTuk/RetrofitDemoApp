package com.example.apple.retrofitdemoapp.Retrofit.ResultHandlers;

import com.example.apple.retrofitdemoapp.profileData.BaseEntity;

import io.reactivex.Observable;
import retrofit2.Response;
import rx.exceptions.Exceptions;

import static com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService.getServerError;


public class ApiResultHandler<T extends BaseEntity> {

    private Class<T> tClass;

    public ApiResultHandler(Class<T> tClass) {

        this.tClass = tClass;
    }

    public Observable<T> handleError(Observable<T> observable) {
        return observable
                .onErrorReturn(throwable -> {
                    if (throwable instanceof ResponseException) {
                        return BaseEntity.create(((ResponseException)throwable).getErrorResult(), tClass);
                    } else {
                        throw Exceptions.propagate(throwable);
                    }
                });
    }

    public Observable<T> handleSuccess(Response<T> response) {
        if (!response.isSuccessful() || response.body() == null) {
            return Observable.error(new ResponseException(getServerError(response.code(), response)));
        } else {
            return Observable.just(response.body());
        }
    }
}
