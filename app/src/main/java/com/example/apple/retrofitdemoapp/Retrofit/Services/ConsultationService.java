package com.example.apple.retrofitdemoapp.Retrofit.Services;

import com.example.apple.retrofitdemoapp.Models.ConsultationRequest;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ConsultationService extends BaseApiService {

    private static IConsultationService sServiceInstance = sRetrofit.create(IConsultationService.class);

    public static void sendRequest(OnRequestComplete completeCallback) {
        ConsultationRequest req = new ConsultationRequest();
        req.Subject = "newcons";
        req.Body = "bosasdsdds";
        Call consRequest = sServiceInstance.sendRequest(req);
        proceedAsync(consRequest, completeCallback);
    }

    private interface IConsultationService {
        String rootPath = "v1/consultation/";

        @POST(rootPath + "request")
        Call sendRequest(@Body ConsultationRequest request);
    }
}
