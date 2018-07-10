package com.example.apple.retrofitdemoapp.view;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(SkipStrategy.class)
public interface DialogMvpView extends MvpViewBase {

    void dismissDialog();

}
