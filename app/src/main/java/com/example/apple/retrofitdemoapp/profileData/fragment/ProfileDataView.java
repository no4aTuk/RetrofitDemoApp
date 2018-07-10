package com.example.apple.retrofitdemoapp.profileData.fragment;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.apple.retrofitdemoapp.view.MvpViewBase;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ProfileDataView extends MvpViewBase {

    void setData(ProfileDataEntity object);

}
