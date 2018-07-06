package com.example.apple.retrofitdemoapp.Retrofit.Services.MedicalCardService;

import com.example.apple.retrofitdemoapp.Models.MainScreenData;
import com.example.apple.retrofitdemoapp.Models.MedicalCard;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public final class MedicalCardService extends BaseApiService<IMedicalCardService> {

    @Inject
    public MedicalCardService(Retrofit retrofit, ApiConfiguration configuration, CredentialsStorage storage) {
        super(retrofit, configuration, storage, IMedicalCardService.class);
    }

    public Observable<MedicalCard> getMedicalCard() {
        return validate(getService().getMedicalCard());
    }

    public Observable<MainScreenData> getMainScreenData() {
        return validate(getService().getMainScreenData());
    }
}
