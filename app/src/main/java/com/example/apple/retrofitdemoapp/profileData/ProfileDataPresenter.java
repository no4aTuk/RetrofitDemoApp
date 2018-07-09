package com.example.apple.retrofitdemoapp.profileData;

import android.content.Context;
import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.example.apple.retrofitdemoapp.BaseApplication;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;

import javax.inject.Inject;

import static com.example.apple.retrofitdemoapp.profileData.SaveWeightInteractor.WEIGHT_PARAM;

@InjectViewState
public class ProfileDataPresenter extends BasePresenter<ProfileDataView, ProfileDataEntity> {

    @Inject
    protected ProfileDataInteractor interactor;
    @Inject
    protected SaveWeightInteractor saveWeightInteractor;
    @Inject
    protected Context context;

    public ProfileDataPresenter() {
        BaseApplication.getAppComponent().inject(this);
    }

    public void setWeight(int weight) {
        Bundle data = new Bundle();
        data.putInt(WEIGHT_PARAM, weight);
        saveWeightInteractor.setData(data);
        saveWeightInteractor.execute(new DefaultSubscriber<>(
                new OnRequestComplete<Void>() {
                    @Override
                    public void onComplete() {
                        getViewState().weightSaved();
                    }

                    @Override
                    public void onFail(ErrorResult error) {
                        getViewState().showError(error.getMessage());
                    }
                }));
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
