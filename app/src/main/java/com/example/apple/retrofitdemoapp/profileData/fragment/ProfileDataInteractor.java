package com.example.apple.retrofitdemoapp.profileData.fragment;

import com.example.apple.retrofitdemoapp.Retrofit.Services.MedicalCardService.MedicalCardService;
import com.example.apple.retrofitdemoapp.interactor.Interactor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class ProfileDataInteractor extends Interactor<ProfileDataEntity> {

    private MedicalCardService mMedicalCardService;

    @Inject
    public ProfileDataInteractor(MedicalCardService medicalCardService) {
        this.mMedicalCardService = medicalCardService;
    }

    @Override
    protected Observable<ProfileDataEntity> buildObservable() {
        return mMedicalCardService.getMedicalCard()
                .zipWith(mMedicalCardService.getMainScreenData(),
                        ProfileDataEntity::create);
    }
}
