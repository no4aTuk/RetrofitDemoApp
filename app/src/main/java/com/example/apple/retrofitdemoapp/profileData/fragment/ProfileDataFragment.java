package com.example.apple.retrofitdemoapp.profileData.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.apple.retrofitdemoapp.R;
import com.example.apple.retrofitdemoapp.presenter.BasePresenter;
import com.example.apple.retrofitdemoapp.profileData.dialog.WeightDialog;
import com.example.apple.retrofitdemoapp.view.BaseFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class ProfileDataFragment extends BaseFragment implements HasSupportFragmentInjector, ProfileDataView {

    private Button mButtonDialog;
    private TextView mDataTv;
    @Inject
    @InjectPresenter
    public ProfileDataPresenter mPresenter;

    @ProvidePresenter
    ProfileDataPresenter providePresenter() {
        return mPresenter;
    }

    @Inject
    DispatchingAndroidInjector<Fragment> mInjector;

    public ProfileDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonDialog = view.findViewById(R.id.open_dialog_button);
        mButtonDialog.setOnClickListener(v -> openDialog());
        mDataTv = view.findViewById(R.id.data_tv);
        mPresenter.setInitialData(getArguments());
    }

    public void openDialog() {
        WeightDialog dialog = new WeightDialog();
        dialog.show(getChildFragmentManager(), "");
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mInjector;
    }


    @Override
    public void setData(ProfileDataEntity medicalCard) {
        mDataTv.setText(medicalCard.toString());
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }
}
