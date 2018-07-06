package com.example.apple.retrofitdemoapp.Retrofit.Services.Consultation;


import com.example.apple.retrofitdemoapp.Models.ConsultationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IConsultationService {
    String rootPath = "v1/consultation/";

    @POST(rootPath + "request")
    Call sendRequest(@Body ConsultationRequest request);
}