package com.example.apple.retrofitdemoapp.profileData.dialog;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.apple.retrofitdemoapp.view.DialogMvpView;

public interface WeightDialogView extends DialogMvpView {

    @StateStrategyType(SkipStrategy.class)
    void weightSaved();
}
