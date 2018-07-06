package com.example.apple.retrofitdemoapp.profileData;

import android.os.Bundle;

import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Models.MedicalCard;
import com.example.apple.retrofitdemoapp.Retrofit.ResultHandlers.ApiResultHandler;
import com.example.apple.retrofitdemoapp.Retrofit.ResultHandlers.ResponseException;
import com.example.apple.retrofitdemoapp.Retrofit.Services.MedicalCardService.MedicalCardService;
import com.example.apple.retrofitdemoapp.interactor.Interactor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService.getServerError;

@Singleton
public class ProfileDataInteractor extends Interactor<ProfileDataEntity> {

    private MedicalCardService mMedicalCardService;

    @Inject
    public ProfileDataInteractor(MedicalCardService medicalCardService) {
        super();
        this.mMedicalCardService = medicalCardService;
    }

    @Override
    protected Observable<ProfileDataEntity> buildObservable() {
         return mMedicalCardService.getMedicalCard()
                .zipWith(mMedicalCardService.getMainScreenData(),
                        ProfileDataEntity::create)
                 .onErrorReturn(throwable -> {
                     ErrorResult result;
                     if (throwable instanceof ResponseException) {
                         result = ((ResponseException) throwable).getErrorResult();
                     } else {
                         result = getServerError(throwable);
                     }
                     return ProfileDataEntity.create(result);
                 });
    }

    @Override
    public void setParams(Bundle data) {
    }

    @Override
    protected ApiResultHandler<ProfileDataEntity> getResultHandler() {
        return null;
    }
}
