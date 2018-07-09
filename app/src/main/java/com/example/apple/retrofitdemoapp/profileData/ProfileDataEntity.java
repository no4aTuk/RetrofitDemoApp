package com.example.apple.retrofitdemoapp.profileData;

import com.example.apple.retrofitdemoapp.Models.MainScreenData;
import com.example.apple.retrofitdemoapp.Models.MedicalCard;

public class ProfileDataEntity {

    public MedicalCard medicalCard;
    public MainScreenData mainScreenData;

    private ProfileDataEntity(MedicalCard medicalCard, MainScreenData mainScreenData) {
        this.medicalCard = medicalCard;
        this.mainScreenData = mainScreenData;
    }
    public static ProfileDataEntity create(MedicalCard medicalCard,
                                           MainScreenData mainScreenData) {
        return new ProfileDataEntity(medicalCard, mainScreenData);
    }

    @Override
    public String toString() {
        return "ProfileDataEntity{" +
                "medicalCard=" + medicalCard +
                ", mainScreenData=" + mainScreenData +
                '}';
    }
}
