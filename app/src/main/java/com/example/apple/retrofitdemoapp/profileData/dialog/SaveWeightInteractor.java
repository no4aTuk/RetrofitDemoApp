package com.example.apple.retrofitdemoapp.profileData.dialog;

import com.example.apple.retrofitdemoapp.Retrofit.Services.MedicalCardService.MedicalCardService;
import com.example.apple.retrofitdemoapp.interactor.Interactor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class SaveWeightInteractor extends Interactor<Void>{

    private MedicalCardService mMedicalCardService;

    public static final String WEIGHT_PARAM = "WEIGHT_PARAM";

    @Inject
    public SaveWeightInteractor(MedicalCardService medicalCardService) {
        this.mMedicalCardService = medicalCardService;
    }

    @Override
    protected Observable<Void> buildObservable() {
        int weight = getData().getInt(WEIGHT_PARAM);
        return mMedicalCardService.saveWeight(weight);
    }
}
