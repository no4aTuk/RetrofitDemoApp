package com.example.apple.retrofitdemoapp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface MvpViewBase extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    default void showError(String error) {}

    default void showLoading() {}

    default void hideLoading() {}

}
