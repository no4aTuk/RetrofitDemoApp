package com.example.apple.retrofitdemoapp.profileData.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.apple.retrofitdemoapp.R;
import com.example.apple.retrofitdemoapp.customViews.PSWeightBmiContainer;
import com.example.apple.retrofitdemoapp.view.MvpDelegateProvider;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.apple.retrofitdemoapp.utils.MessageUtils.showToast;

public class WeightDialog extends MvpAppCompatDialogFragment implements WeightDialogView, MvpDelegateProvider {

    private LinearLayout mMainContainer;
    private LinearLayout mButtonsContainer;
    private Button mOkButton;
    private View.OnClickListener mOkListener;
    private Button mCancelButton;
    private View.OnClickListener mCancelListener;
    private Dialog mDialog;
    private LinearLayout mContainer;
    private ImageView mIconIv;
    private View mIconContainer;
    private TextView mTitle;
    private TextView mText;
    private boolean mWithManualOkDismiss;
    private PSWeightBmiContainer mWeightBmiContainer;
    @Inject
    @InjectPresenter
    WeightDialogPresenter presenter;

    @ProvidePresenter
    WeightDialogPresenter providePresenter() {
        return presenter;
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void weightSaved() {
        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
        dismissDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_picker, null);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mMainContainer = view.findViewById(R.id.picker_main_container);
        mButtonsContainer = (LinearLayout) view.findViewById(R.id.buttons_container);
        mOkButton = (Button) view.findViewById(R.id.pickerDialogOkButton);
        mCancelButton = (Button) view.findViewById(R.id.pickerDialogCancelButton);
        mContainer = (LinearLayout) view.findViewById(R.id.picker_dialog_container);
        mIconContainer = view.findViewById(R.id.picker_dialog_icon_container);
        mIconIv = (ImageView) view.findViewById(R.id.picker_dialog_icon);
        mTitle = (TextView) view.findViewById(R.id.picker_dialog_title);
        mText = (TextView) view.findViewById(R.id.picker_dialog_text);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mWithManualOkDismiss) {
                    mDialog.dismiss();
                }
                if (mOkListener != null) {
                    mOkListener.onClick(v);
                }
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (mCancelListener != null) {
                    mCancelListener.onClick(v);
                }
            }
        });
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText("TITLE");
        mWeightBmiContainer = new PSWeightBmiContainer(getContext());
        mContainer.addView(mWeightBmiContainer);
        mWithManualOkDismiss = true;
        mOkListener = v -> {
            Integer weight = mWeightBmiContainer.getValue();
            if (weight != null) {
                presenter.setWeight(weight);
            }
        };
        mWeightBmiContainer.setBoundValues(50, 300);
        mWeightBmiContainer.setDialogContainer(this);
        setOkEnabled(false);
        mWeightBmiContainer.setParams(10, 100);
        mWeightBmiContainer.setParentDelegate(getRootDelegate());
        return mDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setOkEnabled(boolean enabled) {
        mOkButton.setEnabled(enabled);
    }

    @Override
    public MvpDelegate getRootDelegate() {
        return getMvpDelegate();
    }

    @Override
    public void showError(String error) {
        showToast(getContext(), error);
    }
}
