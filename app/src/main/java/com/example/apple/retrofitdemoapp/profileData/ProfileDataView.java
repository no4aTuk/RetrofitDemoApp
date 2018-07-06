package com.example.apple.retrofitdemoapp.profileData;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ProfileDataView extends MvpView {

    void setData(ProfileDataEntity object);

}