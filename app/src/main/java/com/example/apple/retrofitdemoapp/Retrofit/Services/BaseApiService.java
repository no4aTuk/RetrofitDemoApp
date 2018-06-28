package com.example.apple.retrofitdemoapp.Retrofit.Services;

import android.util.Log;
import android.widget.Toast;

import com.example.apple.retrofitdemoapp.Helpers.HttpCodes;
import com.example.apple.retrofitdemoapp.Models.BackendError;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
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

    protected static Retrofit sRetrofit = RetrofitBuilder.getsInstance();

    protected static <T> void proceedAsync(Call<T> request, final OnRequestComplete<T> callback) {
        request.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && callback != null) {
                    callback.onSuccess(response.body());
                } else if (callback != null) {
                    callback.onFail(getServerError(response.code(), response));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (callback != null)
                callback.onFail(new ErrorResult(-1, t.getLocalizedMessage()));
            }
        });
    }

    protected static <T> void proceedSync(Call<T> request, final OnRequestComplete<T> callback) {
        try {
            Response<T> response = request.execute();
            if (response.isSuccessful() && callback != null) {
                callback.onSuccess(response.body());
            } else if (callback != null) {
                callback.onFail(getServerError(response.code(), response));
            }
        } catch (IOException ioexception) {
            if (callback != null)
            callback.onFail(new ErrorResult(-1, ioexception.getLocalizedMessage()));
        }
    }

    protected static ErrorResult getServerError(int code, Response response) {
        if (response.errorBody() == null) {
            return new ErrorResult(code, "");
        }

        //TODO get the ebaniy context here JUST TO... wait a second ..... TO SHOW FUCKING TOAST MESSAGE!!!!
        switch (code) {
            case HttpCodes.NOT_FOUND:
            case HttpCodes.BAD_REQUEST:
            case HttpCodes.INTERNAL_ERROR:
                return new ErrorResult(code, response.message());
        }

        try {
            String jsonString;
            jsonString = response.errorBody().string();
            BackendError backendError = new Gson().fromJson(jsonString, BackendError.class);
            String errorMessage = backendError.getErrorMessage();
            return new ErrorResult(code, errorMessage);
        } catch (com.google.gson.JsonSyntaxException ilse) {
            ilse.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return new ErrorResult(code, "");
    }
}
