package com.example.apple.retrofitdemoapp.Retrofit.Services.MedicalCardService;

import com.example.apple.retrofitdemoapp.Models.MainScreenData;
import com.example.apple.retrofitdemoapp.Models.MedicalCard;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface IMedicalCardService {
    String ROOT_PATH = "v1/emc";

    @GET(ROOT_PATH)
    Observable<Response<MedicalCard>> getMedicalCard();

    @GET(ROOT_PATH + "/main-screen-data")
    Observable<Response<MainScreenData>> getMainScreenData();
}
