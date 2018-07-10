package com.example.apple.retrofitdemoapp.profileData.activity;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.apple.retrofitdemoapp.view.MvpViewBase;

public interface MainView extends MvpViewBase {

    @StateStrategyType(SkipStrategy.class)
    void gotToken();

    void hideInputFields();
}
