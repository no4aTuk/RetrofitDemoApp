package com.example.apple.retrofitdemoapp.Retrofit.Services;

import com.example.apple.retrofitdemoapp.Models.ConsultationRequest;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ConsultationService extends BaseApiService {

    private IConsultationService service;

    public ConsultationService(IConsultationService service, ApiConfiguration apiConfiguration, CredentialsStorage credentialsStorage) {
        super(apiConfiguration, credentialsStorage);
        this.service = service;
    }

    public void sendRequest(OnRequestComplete<ResponseBody> completeCallback) {
        ConsultationRequest req = new ConsultationRequest();
        req.Subject = "new reqweq eq qwe";
        req.Body = "asd adadsad asdasdad";
        Call<ResponseBody> consRequest = service.sendRequest(req);
        proceedAsync(consRequest, completeCallback);
    }

    public interface IConsultationService {
        String rootPath = "v1/consultation/";

        @POST(rootPath + "request")
        Call<ResponseBody> sendRequest(@Body ConsultationRequest request);
    }
}
