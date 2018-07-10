package com.example.apple.retrofitdemoapp.profileData.fragment;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.example.apple.retrofitdemoapp.BaseApplication;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;

import javax.inject.Inject;

@InjectViewState
public class ProfileDataPresenter extends BasePresenter<ProfileDataView, ProfileDataEntity> {

    protected ProfileDataInteractor interactor;
    protected Context context;

    @Inject
    public ProfileDataPresenter(Context context, ProfileDataInteractor interactor) {
        this.context = context;
        this.interactor = interactor;
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected DefaultSubscriber<ProfileDataEntity> getInitSubscriber() {
        return new DefaultSubscriber<>(new UserProfileCallback());
    }

    @Override
    protected ProfileDataInteractor getInitInteractor() {
        return interactor;
    }

    private final class UserProfileCallback implements OnRequestComplete<ProfileDataEntity> {

        @Override
        public void onSuccess(ProfileDataEntity result) {
            getViewState().setData(result);
        }

        @Override
        public void onFail(ErrorResult error) {
            getViewState().showError(error.getMessage());
        }
    }
}
