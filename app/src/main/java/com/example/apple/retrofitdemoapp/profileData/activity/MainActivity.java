package com.example.apple.retrofitdemoapp.profileData.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.apple.retrofitdemoapp.R;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.profileData.fragment.ProfileDataFragment;
import com.example.apple.retrofitdemoapp.utils.CommonUtils;
import com.example.apple.retrofitdemoapp.view.BaseActivity;
import com.example.apple.retrofitdemoapp.view.MvpDelegateProvider;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.example.apple.retrofitdemoapp.profileData.activity.AuthInteractor.PASSWORD;
import static com.example.apple.retrofitdemoapp.profileData.activity.AuthInteractor.USER_NAME;
import static com.example.apple.retrofitdemoapp.utils.MessageUtils.showToast;


public class MainActivity extends BaseActivity implements MvpDelegateProvider, HasSupportFragmentInjector, MainView {

    private EditText mLoginEt;
    private EditText mPasswordnEt;
    private Button mEnterButton;
    @Inject
    @InjectPresenter
    public MainPresenter mPresenter;
    @ProvidePresenter
    public MainPresenter providePresenter() {
        return mPresenter;
    }
    @Inject
    public CredentialsStorage mCredentialsStorage;
    @Inject
    public DispatchingAndroidInjector<Fragment> mInjector;

    @Override
    public MvpDelegate getRootDelegate() {
        return getMvpDelegate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_data);
        mLoginEt = findViewById(R.id.login_et);
        mPasswordnEt = findViewById(R.id.password_et);
        mEnterButton = findViewById(R.id.enter_button);
        mEnterButton.setOnClickListener(v -> enter());
    }

    private void enter() {
        mPresenter.auth(mLoginEt.getText().toString(), mPasswordnEt.getText().toString());
    }

    @Override
    public MainPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void showError(String error) {
        showToast(this, error);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mInjector;
    }

    @Override
    public void gotToken() {
        CommonUtils.hideSoftKeyboard(mLoginEt);
        CommonUtils.hideSoftKeyboard(mPasswordnEt);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileDataFragment())
                .commit();
    }

    @Override
    public void hideInputFields() {
        mLoginEt.setVisibility(View.GONE);
        mPasswordnEt.setVisibility(View.GONE);
        mEnterButton.setVisibility(View.GONE);
    }
}
