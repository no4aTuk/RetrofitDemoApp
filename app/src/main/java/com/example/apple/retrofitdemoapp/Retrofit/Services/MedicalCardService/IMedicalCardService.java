package com.example.apple.retrofitdemoapp.Retrofit.Services.MedicalCardService;

import com.example.apple.retrofitdemoapp.Models.MainScreenData;
import com.example.apple.retrofitdemoapp.Models.MedicalCard;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IMedicalCardService {
    String ROOT_PATH = "v1/emc";

    @GET(ROOT_PATH)
    Observable<Response<MedicalCard>> getMedicalCard();

    @GET(ROOT_PATH + "/main-screen-data")
    Observable<Response<MainScreenData>> getMainScreenData();

    @FormUrlEncoded
    @PUT(ROOT_PATH + "/weight")
    Observable<Response<Void>> saveWeight(@Field("Weight") int weight);
}
