package com.example.apple.retrofitdemoapp.profileData;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.apple.retrofitdemoapp.view.BaseActivity;
import com.example.apple.retrofitdemoapp.R;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.customViews.PSPickerDialog;
import com.example.apple.retrofitdemoapp.customViews.PSWeightBmiContainer;
import com.example.apple.retrofitdemoapp.view.MvpDelegateProvider;

import javax.inject.Inject;


public class ActivityProfileData extends BaseActivity implements ProfileDataView, MvpDelegateProvider {

    private TextView dataTv;
    private EditText dataEt;
    private Button buttonDialog;
    @InjectPresenter
    public ProfileDataPresenter presenter;
    @Inject
    public CredentialsStorage credentialsStorage;

    @Override
    public MvpDelegate getRootDelegate() {
        return getMvpDelegate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_data);
        dataTv = findViewById(R.id.data_tv);
        dataEt = findViewById(R.id.data_et);
        credentialsStorage.setup("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1laWQiOiIxMTQ3OSIsInVuaXF1ZV9uYW1lIjoiNzk1MTUzODA4MDkiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL2FjY2Vzc2NvbnRyb2xzZXJ2aWNlLzIwMTAvMDcvY2xhaW1zL2lkZW50aXR5cHJvdmlkZXIiOiJBU1AuTkVUIElkZW50aXR5IiwiQXNwTmV0LklkZW50aXR5LlNlY3VyaXR5U3RhbXAiOiI5MmViZTEzYy0xYTg1LTRhMTAtYTgzZC02Y2FlMjZhMGNjYTEiLCJyb2xlIjoiVXNlciIsIlByb2ZpbGVJZCI6IjEyNDgxIiwiQXBwVHlwZSI6InBhdGllbnRBcHAiLCJGaWxlVG9rZW4iOiJzQVZHVnFBTXo5OWtLUFhJbFNra2hCWGl0Y05SRXdveWVJditSSzJTYmVvPSIsImlzcyI6IkhlYWx0aENhcmVUb2tlbklzc3VlckNsaWVudCIsImF1ZCI6IjQxNGUxOTI3YTM4ODRmNjhhYmM3OWY3MjgzODM3ZmQxIiwiZXhwIjoxNTYyMTU0ODMxLCJuYmYiOjE1MzA2MTg4MzF9.m0NURDZoJWalWfPZUbZF-cOChSxFy1ZFdVG_mAVu2Mc",
                "c06ddc93b11f4c6390fe8af451d39c94");
        presenter.loadData(getIntent().getExtras());
        buttonDialog = findViewById(R.id.open_dialog_button);
        buttonDialog.setOnClickListener(v -> {

            openDialog();

        });

    }

    @Override
    public ProfileDataPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setData(ProfileDataEntity medicalCard) {
        dataTv.setText(medicalCard.toString());
    }

    public void openDialog() {
        final PSWeightBmiContainer weightBmiContainer = new PSWeightBmiContainer(ActivityProfileData.this);
        final PSPickerDialog dialog = new PSPickerDialog.Builder(ActivityProfileData.this)
                .withTitle("WEIGHT BMI")
                .addView(weightBmiContainer)
                .withOkButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .withCancelButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .build();
        weightBmiContainer.setBoundValues(50, 300);
        weightBmiContainer.setDialogContainer(dialog);
        dialog.setOkEnabled(false);
        weightBmiContainer.setParams(10, 100);
        weightBmiContainer.setParentDelegate(getRootDelegate());
        dialog.show();
    }
}
