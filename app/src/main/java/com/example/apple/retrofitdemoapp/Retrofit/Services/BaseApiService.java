package com.example.apple.retrofitdemoapp.Retrofit.Services;

import android.util.Log;

import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.RetrofitBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BaseApiService {

    protected static Retrofit sRetrofit = RetrofitBuilder.createInstance();

    protected static <T> void proceedAsync(Call<T> request, final OnRequestComplete<T> callback) {
        request.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFail(t.getLocalizedMessage());
            }
        });
    }

    protected static <T> void proceedSync(Call<T> request, final OnRequestComplete<T> callback) {
        try {
            Response<T> response = request.execute();
            if (response.isSuccessful()) {
                callback.onSuccess(response.body());
            } else {
                callback.onFail(response.message());
            }
        } catch (IOException ioexception) {
            callback.onFail(ioexception.getLocalizedMessage());
        }
    }
}
