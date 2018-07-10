package com.example.apple.retrofitdemoapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;

import dagger.android.support.AndroidSupportInjection;

public abstract class BaseFragment extends MvpAppCompatFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().resume();
    }

    protected abstract BasePresenter getPresenter();
}
