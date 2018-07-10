package com.example.apple.retrofitdemoapp.profileData.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;

import javax.inject.Inject;

import static com.example.apple.retrofitdemoapp.profileData.activity.AuthInteractor.PASSWORD;
import static com.example.apple.retrofitdemoapp.profileData.activity.AuthInteractor.USER_NAME;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView, Void> {

    private final Context mContext;
    private final AuthInteractor mInteractor;

    @Inject
    public MainPresenter(Context context, AuthInteractor interactor) {
        this.mContext = context;
        this.mInteractor = interactor;
    }

    public void auth(String login, String password) {
        Bundle credentials = new Bundle();
        credentials.putString(USER_NAME, login);
        credentials.putString(PASSWORD, password);
        mInteractor.setData(credentials);
        mInteractor.execute(
                new DefaultSubscriber<>(new OnRequestComplete<Token>() {

                    @Override
                    public void onSuccess(Token result) {
                        getViewState().gotToken();
                        getViewState().hideInputFields();
                    }

                    @Override
                    public void onFail(ErrorResult error) {
                        getViewState().showError(error.getMessage());
                    }
                }));
    }

    @Nullable
    @Override
    protected Interactor<Void> getInitInteractor() {
        return null;
    }

    @Nullable
    @Override
    protected DefaultSubscriber<Void> getInitSubscriber() {
        return null;
    }
}
