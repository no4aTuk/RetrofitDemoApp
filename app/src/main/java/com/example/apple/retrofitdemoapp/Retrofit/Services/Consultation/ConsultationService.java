package com.example.apple.retrofitdemoapp.Retrofit.Services.Consultation;

import android.content.Context;

import com.example.apple.retrofitdemoapp.Models.ConsultationRequest;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Retrofit;

@Singleton
public final class ConsultationService extends BaseApiService<IConsultationService> {

    @Inject
    public ConsultationService(Context context, Retrofit retrofit, ApiConfiguration configuration, CredentialsStorage storage) {
        super(context, retrofit, configuration, storage, IConsultationService.class);
    }

    //todo: to rx
    public void sendRequest(OnRequestComplete completeCallback) {
        ConsultationRequest req = new ConsultationRequest();
        req.Subject = "newcons";
        req.Body = "bosasdsdds";
        Call consRequest = getService().sendRequest(req);
//        proceedAsync(consRequest, completeCallback);
    }
}
