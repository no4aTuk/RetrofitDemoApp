package com.example.apple.retrofitdemoapp.view;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface CustomMvpView {

    void setParentDelegate(MvpDelegate delegate);

    MvpDelegate getMvpDelegate();
}
