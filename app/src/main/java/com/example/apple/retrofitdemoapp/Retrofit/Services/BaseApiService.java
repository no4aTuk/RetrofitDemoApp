package com.example.apple.retrofitdemoapp.Retrofit.Services;

import android.util.Log;

import com.example.apple.retrofitdemoapp.Models.BackendError;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.RetrofitBuilder;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
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
                    callback.onFail(getServerError(response.errorBody()));
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

    protected static String getServerError(ResponseBody errorBody) {
        if (errorBody == null) {
            return "";
        }
        try {
            String jsonString;
            jsonString = errorBody.string();
            BackendError backendError = new Gson().fromJson(jsonString, BackendError.class);
            return backendError.getErrorMessage();
        } catch (com.google.gson.JsonSyntaxException ilse) {
            ilse.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
