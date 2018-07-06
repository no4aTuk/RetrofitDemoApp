package com.example.apple.retrofitdemoapp.profileData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.example.apple.retrofitdemoapp.BaseApplication;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.ResultHandlers.ResponseException;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;

import javax.inject.Inject;

@InjectViewState
public class ProfileDataPresenter extends BasePresenter<ProfileDataView, ProfileDataEntity> {

    @Inject
    ProfileDataInteractor interactor;
    @Inject
    Context context;

    public ProfileDataPresenter() {
        BaseApplication.getAppComponent().inject(this);
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
    protected UserProfileSubscriber getInitSubscriber() {
        return getProfileSubscriber();
    }

    @Override
    protected ProfileDataInteractor getInitInteractor() {
        return interactor;
    }

    @NonNull
    private UserProfileSubscriber getProfileSubscriber() {
        return new UserProfileSubscriber();
    }

    private final class UserProfileSubscriber extends DefaultSubscriber<ProfileDataEntity> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            if (e instanceof ResponseException) {
                ErrorResult result = ((ResponseException) e).getErrorResult();
                Toast.makeText(context, "Error code: " + result.getStatusCode()
                        + "; \n Message: " + result.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                e.printStackTrace();
            }
        }

        @Override
        public void onNext(ProfileDataEntity entity) {
            if (entity.isSuccess()) {
                getViewState().setData(entity);
            } else {
                ErrorResult result = entity.getErrorResult();
                Toast.makeText(context, "Error code: " + result.getStatusCode()
                        + "; \n Message: " + result, Toast.LENGTH_SHORT).show();
                System.out.println(entity.getErrorResult().getMessage());
            }
        }
    }
}
