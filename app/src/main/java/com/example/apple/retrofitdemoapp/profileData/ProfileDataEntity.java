package com.example.apple.retrofitdemoapp.profileData;

import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Models.MainScreenData;
import com.example.apple.retrofitdemoapp.Models.MedicalCard;

public class ProfileDataEntity extends BaseEntity {

    public MedicalCard medicalCard;
    public MainScreenData mainScreenData;

    private ProfileDataEntity(MedicalCard medicalCard, MainScreenData mainScreenData) {
        this.medicalCard = medicalCard;
        this.mainScreenData = mainScreenData;
    }

    public ProfileDataEntity() {
    }

    public static ProfileDataEntity create(MedicalCard medicalCard,
                                           MainScreenData mainScreenData) {
        return new ProfileDataEntity(medicalCard, mainScreenData);
    }

    public static ProfileDataEntity create(ErrorResult errorResult) {
        ProfileDataEntity entity = new ProfileDataEntity();
        entity.setErrorResult(errorResult);
        return entity;
    }

    @Override
    public String toString() {
        return "ProfileDataEntity{" +
                "medicalCard=" + medicalCard +
                ", mainScreenData=" + mainScreenData +
                '}';
    }
}
