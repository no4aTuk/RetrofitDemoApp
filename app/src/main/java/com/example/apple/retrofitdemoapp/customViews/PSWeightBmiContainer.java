package com.example.apple.retrofitdemoapp.customViews;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.apple.retrofitdemoapp.R;
import com.example.apple.retrofitdemoapp.view.CustomMvpView;
import com.example.apple.retrofitdemoapp.view.WeightBmiPresenter;
import com.example.apple.retrofitdemoapp.view.WeightBmiView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lavrik on 13.07.2017.
 */

public class PSWeightBmiContainer extends LinearLayout implements WeightBmiView, CustomMvpView {

    //region Private fields
    private EditText mEtWeight;
    private TextView mBmiParamsTv;
    private TextView mNormalWeightTv;
    private TextView mRecommendationTv;
    private View mNormalWeightRecommendationContainer;
    private View mBmiParamsContainer;
    private View mRecommendationContainer;
    private TextView mTvError;
    private LinearLayout mLlError;

    private int mHeight;
    private float minValue = 2;
    private float maxValue = 300;
    private PSPickerDialog mDialogContainer;

    @InjectPresenter
    WeightBmiPresenter presenter;
    private MvpDelegate<PSWeightBmiContainer> mMvpDelegate;
    private MvpDelegate mParentDelegate;
    //endregion

    //region Public constructors
    public PSWeightBmiContainer(Context context) {
        this(context, null);
    }

    public PSWeightBmiContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PSWeightBmiContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //endregion

    //region Public methods

    public void setDialogContainer(final PSPickerDialog dialog) {
        mDialogContainer = dialog;
    }

    public void setParams(int weight, int height) {
        if (weight > 0) {
            String weightValue = String.valueOf(weight);
            int length = weightValue.length();
            StringBuilder builder = new StringBuilder();
            if (length > 3 && length < 7) {
                String weightKg = weightValue.substring(0, length - 3);
                String weightG = weightValue.substring(length - 3, length - 2);
                builder.append(weightKg);
                if (Integer.parseInt(weightG) > 0) {
                    builder.append(".").append(weightG);
                }
            } else if (length <= 3) {
                builder.append("0.");
                for (int i = length; i < 3; i++) {
                    builder.append("0");
                }
                builder.append(weightValue);
            }
            mEtWeight.setText(builder.toString());
        }
        mEtWeight.post(() -> {
            mEtWeight.requestFocus();
            showSoftInput(((Activity) getContext()), mEtWeight);
        });
        this.mHeight = height;
        updateText();
    }

    void showSoftInput(Activity activity, View inputView) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputView, InputMethodManager.SHOW_IMPLICIT);
    }

    public void addTextWatcher(TextWatcher textWatcher) {
        mEtWeight.addTextChangedListener(textWatcher);
    }

//    public void setDialogContainer(final PSPickerDialog dialog) {
//        mDialogContainer = dialog;
//    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        getMvpDelegate().onSaveInstanceState();
        getMvpDelegate().onDetach();
    }

    @Override
    public void setParentDelegate(MvpDelegate parentDelegate) {
        mParentDelegate = parentDelegate;

        getMvpDelegate().onCreate();
        getMvpDelegate().onAttach();
    }

    @Override
    public MvpDelegate getMvpDelegate() {
        if (mMvpDelegate != null) {
            return mMvpDelegate;
        }

        mMvpDelegate = new MvpDelegate<>(this);
        mMvpDelegate.setParentDelegate(mParentDelegate, String.valueOf(getId()));
        return mMvpDelegate;
    }

    public Integer getValue() {
        Integer result = null;
        String textValue = mEtWeight.getText().toString();
        if (!textValue.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            if (textValue.contains(".")) {
                int index = textValue.indexOf(".");
                builder.append(textValue.substring(0, index));
                String weightG = textValue.substring(index + 1);
                builder.append(weightG);
                for (int i = weightG.length(); i < 3; i++) {
                    builder.append("0");
                }
            } else {
                builder.append(textValue).append("000");
            }
            result = Integer.parseInt(builder.toString());
        }
        return result;
    }

    public void setBoundValues(int min, int max) {
        minValue = min;
        maxValue = max;
    }
    //endregion

    //region Private methods
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.weight_bmi_container_view, this, true);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setPadding(0, 0, 0, 12);
        setLayoutParams(params);
        setOrientation(VERTICAL);
        mEtWeight = findViewById(R.id.et_weight);
        mEtWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 1)});
        mTvError = findViewById(R.id.tvError);
        mLlError = findViewById(R.id.llError);
        mBmiParamsTv = findViewById(R.id.weight_bmi_tv);
        mNormalWeightTv = findViewById(R.id.weight_normal_tv);
        mRecommendationTv = findViewById(R.id.recommendation_tv);
        mRecommendationContainer = findViewById(R.id.recommendation_container);
        mNormalWeightRecommendationContainer = findViewById(R.id.normal_weight_recommendation_container);
        mBmiParamsContainer = findViewById(R.id.bmi_params_container);

        mEtWeight.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                updateText();
            }
            return false;
        });

        mEtWeight.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                updateText();
            }
            return false;
        });

        mEtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.toString().equals("")) {
//                    mEtWeight.setHintTextColor(getContext().getResources().getColor(R.color.color_edittext_light_gray_hint));
                    mDialogContainer.setOkEnabled(false);
                } else if (s.toString().equals(".")) {
                    mEtWeight.setText("0.");
                    mEtWeight.setSelection(2);
                } else {
                    String textValue = s.toString();
                    float value = Float.valueOf(textValue);
                    if (value < minValue) {
                        mTvError.setText("Good");
                        mLlError.setVisibility(VISIBLE);
                        mDialogContainer.setOkEnabled(false);
                    } else if (value > maxValue) {
                        mTvError.setText("Bad");
                        mLlError.setVisibility(VISIBLE);
                        mDialogContainer.setOkEnabled(false);
                    } else {
                        mLlError.setVisibility(GONE);
                        mDialogContainer.setOkEnabled(true);
                    }
                }
            }
        });
//        mEtWeight.setOnFocusChangeListener((v, hasFocus) -> mEtWeight.setHintTextColor(hasFocus ? getContext().getResources().getColor(R.color.color_edittext_light_gray_hint) : getContext().getResources().getColor(R.color.pickerDialogTextColor)));
    }

    private void updateText() {
        if (mHeight <= 0) {
            mRecommendationTv.setText("enter height");
            mBmiParamsContainer.setVisibility(GONE);
            mNormalWeightRecommendationContainer.setVisibility(GONE);
            return;
        }
        int weight = 0;
        if (!mEtWeight.getText().toString().isEmpty()) {
            weight = getValue();
        }

        if (weight == 0) {
            mRecommendationTv.setText("enter weight");
            mBmiParamsContainer.setVisibility(GONE);
            mNormalWeightRecommendationContainer.setVisibility(GONE);
            return;
        }
        mBmiParamsContainer.setVisibility(VISIBLE);
        mNormalWeightRecommendationContainer.setVisibility(VISIBLE);

        float bmiCount = 5;
        int stage = 1;
        if (stage != 1) {
            mRecommendationTv.setText("bad");
            mRecommendationContainer.setVisibility(VISIBLE);
            mEtWeight.setSelected(true);
        } else {
            mRecommendationContainer.setVisibility(GONE);
            mEtWeight.setSelected(false);
        }
//        String bmiDescr = PSHealthUtils.getBmiDescription(getContext(), bmiCount);
//        String bmi = getContext().getString(R.string.activity_profile_bmi_params, String.format("%.1f", bmiCount).replaceAll(",", "."), bmiDescr);
//        mBmiParamsTv.setText(Html.fromHtml(bmi), TextView.BufferType.SPANNABLE);

//        int minWeight = PSHealthUtils.getMinWeight(mHeight);
//        int maxWeight = PSHealthUtils.getMaxWeight(mHeight);

//        mNormalWeightTv.setText(getContext().getString(R.string.activity_profile_normal_weight, minWeight, maxWeight));
    }

    @Override
    public void setState(int state) {

    }
    //endregion

    public class DecimalDigitsInputFilter implements InputFilter {
        private int mDigitsBeforeZero;
        private int mDigitsAfterZero;
        private Pattern mPattern;

        private static final int DIGITS_BEFORE_ZERO_DEFAULT = 1;
        private static final int DIGITS_AFTER_ZERO_DEFAULT = 1;

        public DecimalDigitsInputFilter(Integer digitsBeforeZero, Integer digitsAfterZero) {
            mDigitsBeforeZero = (digitsBeforeZero != null ? digitsBeforeZero : DIGITS_BEFORE_ZERO_DEFAULT);
            mDigitsAfterZero = (digitsAfterZero != null ? digitsAfterZero : DIGITS_AFTER_ZERO_DEFAULT);
            mPattern = Pattern.compile("-?[0-9]{0," + (mDigitsBeforeZero) + "}+((\\.[0-9]{0," + (mDigitsAfterZero)
                    + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.subSequence(0, dstart).toString() + replacement
                    + dest.subSequence(dend, dest.length()).toString();
            Matcher matcher = mPattern.matcher(newVal);
            if (matcher.matches())
                return null;

            if (TextUtils.isEmpty(source))
                return dest.subSequence(dstart, dend);
            else
                return "";
        }
    }
}
