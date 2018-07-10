package com.example.apple.retrofitdemoapp.profileData.dialog;

import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.interactor.Interactor;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.rx.DefaultSubscriber;

import javax.inject.Inject;

import static com.example.apple.retrofitdemoapp.profileData.dialog.SaveWeightInteractor.WEIGHT_PARAM;

@InjectViewState
public class WeightDialogPresenter extends BasePresenter<WeightDialogView, Void> {

    private SaveWeightInteractor mSaveWeightInteractor;

    @Inject
    public WeightDialogPresenter(SaveWeightInteractor saveWeightInteractor) {
        this.mSaveWeightInteractor = saveWeightInteractor;
    }

    public void setWeight(int weight) {
        Bundle data = new Bundle();
        data.putInt(WEIGHT_PARAM, weight);
        mSaveWeightInteractor.setData(data);
        mSaveWeightInteractor.execute(new DefaultSubscriber<>(
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
    protected Interactor<Void> getInitInteractor() {
        return null;
    }

    @Override
    protected DefaultSubscriber<Void> getInitSubscriber() {
        return null;
    }
}
