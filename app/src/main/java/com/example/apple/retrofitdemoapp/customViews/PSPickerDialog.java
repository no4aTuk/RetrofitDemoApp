package com.example.apple.retrofitdemoapp.customViews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.arellomobile.mvp.MvpDelegate;
import com.example.apple.retrofitdemoapp.R;
import com.example.apple.retrofitdemoapp.view.CustomMvpView;
import com.example.apple.retrofitdemoapp.view.MvpDelegateProvider;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by lavrik on 05.07.2017.
 */

public class PSPickerDialog {

    //region Private fields
    private Context mContext;
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
    //endregion

    //region Private constructors
    private PSPickerDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    }
    //endregion

    //region Public methods
    public void show() {
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        if (mContext instanceof MvpDelegateProvider) {
//            MvpDelegate rootDelegate = ((MvpDelegateProvider)mContext).getRootDelegate();
//            View mvpView;
//            for (int i = 0; i < mContainer.getChildCount(); i++) {
//                if ((mvpView = mContainer.getChildAt(i)) instanceof CustomMvpView) {
//                    ((CustomMvpView) mvpView).setParentDelegate(rootDelegate);
//                }
//            }
//        }
    }

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    public void hideButtons() {
        mOkButton.setVisibility(View.GONE);
        mCancelButton.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title.toUpperCase());
    }

    public void setOkEnabled(boolean enabled) {
        mOkButton.setEnabled(enabled);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
    }

    public void setDismissDelay(long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mDialog.dismiss();
        }, delayMillis);
    }

    public void detachView(View view) {
        mContainer.removeView(view);
    }

    public void dismiss() {
        mDialog.dismiss();
    }
    //endregion

    //region Public classes
    public static class Builder {

        private PSPickerDialog mPickerDialog;

        public Builder(Context context) {
            mPickerDialog = new PSPickerDialog(context);
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            mPickerDialog.mDialog.setCanceledOnTouchOutside(cancel);
            return this;
        }

        public Builder withTitle(String title) {
            mPickerDialog.mTitle.setVisibility(View.VISIBLE);
            mPickerDialog.mTitle.setText(title.toUpperCase());
            return this;
        }

        public Builder withText(int textId) {
            mPickerDialog.mText.setVisibility(View.VISIBLE);
            mPickerDialog.mText.setText(textId);
            return this;
        }

        public Builder withText(String text) {
            mPickerDialog.mText.setVisibility(View.VISIBLE);
            mPickerDialog.mText.setText(text);
            return this;
        }

        public Builder withText(String text, boolean wideText) {
            if (wideText) {
//                mPickerDialog.mText.setMinWidth(PSCommonUtils.convertDpToPixels(500));
//                mPickerDialog.mText.setMaxWidth(PSCommonUtils.convertDpToPixels(500));
            }
            return withText(text);
        }

        public Builder textGravity(int gravity) {
            mPickerDialog.mText.setGravity(gravity);
            return this;
        }

        /**
         * @param text текст
         * @param size текст в пикселях
         * @return объект билдера
         */
        public Builder withTextSize(String text, float size) {
            mPickerDialog.mText.setVisibility(View.VISIBLE);
            mPickerDialog.mText.setText(text);
            mPickerDialog.mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            return this;
        }

        public Builder withOkButton(View.OnClickListener listener) {
            mPickerDialog.mOkListener = listener;
            return this;
        }

        public Builder withOkButton(String text, View.OnClickListener listener) {
            mPickerDialog.mOkButton.setText(text);
            mPickerDialog.mOkListener = listener;
            return this;
        }

        public Builder setButtonsGravity(int gravity) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPickerDialog.mButtonsContainer.getLayoutParams();
            params.gravity = gravity;
            return this;
        }

        public Builder withoutCancelButton() {
            mPickerDialog.mCancelButton.setVisibility(View.GONE);
            return this;
        }

        public Builder setAlertMode(long delayMillis) {
            withoutCancelButton();
            textGravity(Gravity.CENTER);
            mPickerDialog.mOkButton.setVisibility(View.GONE);
            mPickerDialog.setDismissDelay(delayMillis);
            int bottomMargin = 24;
            mPickerDialog.mMainContainer.setPadding(0, 0, 0, bottomMargin);
            mPickerDialog.mText.setLineSpacing(0, 1.3f);
            return this;
        }

        public Builder withCancelButton(final View.OnClickListener listener) {
            mPickerDialog.mCancelListener = listener;
            mPickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    listener.onClick(mPickerDialog.mContainer);
                }
            });
            return this;
        }

        public Builder withIcon(@DrawableRes int iconId) {
            mPickerDialog.mIconContainer.setVisibility(View.VISIBLE);
            mPickerDialog.mIconIv.setImageDrawable(ContextCompat.getDrawable(mPickerDialog.mContext, iconId));
//            ((ViewGroup.MarginLayoutParams)mPickerDialog.mMainContainer.getLayoutParams()).topMargin =
//                    (int) mPickerDialog.mContext.getResources().getDimension(R.dimen.dialog_picker_margin_top);
//            int padding = (int) mPickerDialog.mContext.getResources().getDimension(R.dimen.dialog_picker_padding);
//            mPickerDialog.mTitle.setPadding(padding, 0, padding, 0);
            return this;
        }

//        public Builder withDictionary(PSTableDictionary[] dictionary, PSTableDictionary chosenItem, OnDictionaryItemSelectListener listener) {
//            DictionaryView dictionaryView = new DictionaryView(mPickerDialog.mContext);
//            dictionaryView.setListener(listener);
//            dictionaryView.setItems(dictionary, chosenItem);
//            return addView(dictionaryView);
//        }

        public Builder withManualOkDismiss() {
            mPickerDialog.mWithManualOkDismiss = true;
            return this;
        }

//        public Builder withAdditionalPaddings() {
//            mPickerDialog.mMainContainer.setPadding(0, PSCommonUtils.convertDpToPixels(8), 0, 0);
//            mPickerDialog.mButtonsContainer.setPadding(0, 0, PSCommonUtils.convertDpToPixels(12), 0);
//            return this;
//        }

        public Builder addView(View view) {
            mPickerDialog.mContainer.addView(view);
            return this;
        }

        public Builder addPicker(NumberPicker picker) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 12, 0);
            picker.setLayoutParams(layoutParams);
//            picker.setDividerDrawable(new ColorDrawable(ContextCompat.getColor(mPickerDialog.mContext, R.color.colorBlue)));
            picker.setWrapSelectorWheel(false);
            return addView(picker);
        }

        public Builder addPicker(Calendar initialDate, DatePicker.OnDateChangedListener listener) {
//            ContextThemeWrapper themeWrapper = new ContextThemeWrapper(mPickerDialog.mContext, R.style.DatePickerTheme);
            DatePicker datePicker = new DatePicker(mPickerDialog.mContext);
            datePicker.init(initialDate.get(Calendar.YEAR), initialDate.get(Calendar.MONTH),
                    initialDate.get(Calendar.DAY_OF_MONTH), listener);
            return addView(datePicker);
        }

        public Builder addPicker(Calendar initialDate, boolean fixedTextSize, Long min, Long max, DatePicker.OnDateChangedListener listener) {
            ContextThemeWrapper themeWrapper;
//            if (fixedTextSize) {
//                themeWrapper = new ContextThemeWrapper(mPickerDialog.mContext, R.style.DatePickerFixedSize);
//            } else {
//                themeWrapper = new ContextThemeWrapper(mPickerDialog.mContext, R.style.DatePickerTheme);
//            }

            DatePicker datePicker = new DatePicker(mPickerDialog.mContext);

            Resources system = Resources.getSystem();
            int dayId = system.getIdentifier("day", "id", "android");
            int monthId = system.getIdentifier("month", "id", "android");
            int yearId = system.getIdentifier("year", "id", "android");
            NumberPicker dayPicker = (NumberPicker) datePicker.findViewById(dayId);
            NumberPicker monthPicker = (NumberPicker) datePicker.findViewById(monthId);
            NumberPicker yearPicker = (NumberPicker) datePicker.findViewById(yearId);
            setCustomDividerColor(dayPicker, mPickerDialog.mContext);
            setCustomDividerColor(monthPicker, mPickerDialog.mContext);
            setCustomDividerColor(yearPicker, mPickerDialog.mContext);
            if (min != null) {
                datePicker.setMinDate(min);
            }
            if (max != null) {
                datePicker.setMaxDate(max);
            }
            datePicker.init(initialDate.get(Calendar.YEAR), initialDate.get(Calendar.MONTH),
                    initialDate.get(Calendar.DAY_OF_MONTH), listener);
            return addView(datePicker);
        }

        public Builder addTimePicker(int hourMax, int minuteMax, Calendar calendar, NumberPicker.OnValueChangeListener listener) {
//            ContextThemeWrapper themeWrapper = new ContextThemeWrapper(mPickerDialog.mContext, R.style.DatePickerFixedSize);

            NumberPicker hourPicker = new NumberPicker(mPickerDialog.mContext);
            NumberPicker minutePicker = new NumberPicker(mPickerDialog.mContext);

            setCustomDividerColor(hourPicker, mPickerDialog.mContext);
            setCustomDividerColor(minutePicker, mPickerDialog.mContext);

            hourPicker.setMaxValue(hourMax);
            hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
            hourPicker.setFormatter(i -> String.format("%02d", i));

            minutePicker.setMaxValue(minuteMax);
            minutePicker.setValue(calendar.get(Calendar.MINUTE));
            minutePicker.setFormatter(i -> String.format("%02d", i));

            if (listener != null) {
                hourPicker.setOnValueChangedListener(listener);
                minutePicker.setOnValueChangedListener(listener);
            }
            hourPicker.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            hourPicker.setFocusableInTouchMode(true);
            minutePicker.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            minutePicker.setFocusableInTouchMode(true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 20;
            hourPicker.setLayoutParams(params);

            mPickerDialog.mContainer.addView(hourPicker);
            mPickerDialog.mContainer.addView(minutePicker);

            return this;
        }

        public Builder addPicker(int from, int to, NumberPicker.OnValueChangeListener listener) {
//            ContextThemeWrapper themeWrapper = new ContextThemeWrapper(mPickerDialog.mContext, R.style.DatePickerTheme);
            NumberPicker picker = new NumberPicker(mPickerDialog.mContext);
            picker.setMinValue(from);
            picker.setMaxValue(to);
            picker.setOnValueChangedListener(listener);
            return addPicker(picker);
        }

        public Builder addPicker(String[] values, boolean fixedTextSize, int initValue, NumberPicker.OnValueChangeListener listener) {
            ContextThemeWrapper themeWrapper;
//            if (fixedTextSize) {
//                themeWrapper = new ContextThemeWrapper(mPickerDialog.mContext, R.style.DatePickerFixedSize);
//            } else {
//                themeWrapper = new ContextThemeWrapper(mPickerDialog.mContext, R.style.DatePickerTheme);
//            }
            NumberPicker picker = new NumberPicker(mPickerDialog.mContext);
            picker.setMinValue(0);
            picker.setMaxValue(values.length - 1);
            picker.setDisplayedValues(values);
            picker.setValue(initValue);
            picker.setOnValueChangedListener(listener);
            setCustomDividerColor(picker, mPickerDialog.mContext);
            return addPicker(picker);
        }


        public PSPickerDialog build() {
            return mPickerDialog;
        }

        private void setCustomDividerColor(NumberPicker picker, Context context) {
            if (picker == null)
                return;

            final int count = picker.getChildCount();
            for (int i = 0; i < count; i++) {
                try {
                    Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                    dividerField.setAccessible(true);
                    ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, android.R.color.holo_blue_bright));
                    dividerField.set(picker, colorDrawable);
                    picker.invalidate();
                } catch (Exception e) {
                    Log.w("setDividerColor", e);
                }
            }
        }
    }

//    public interface OnDictionaryItemSelectListener {
//        void OnItemSelected(PSTableDictionary item);
//    }

//    private static class DictionaryView extends PSCustomView {
//
//        private RadioGroup mItemsContainer;
//        private PSTableDictionary[] mDictionaryItems;
//
//        public DictionaryView(@NonNull Context context) {
//            super(context);
//        }
//
//        public DictionaryView(@NonNull Context context, @Nullable AttributeSet attrs) {
//            super(context, attrs);
//        }
//
//        public DictionaryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//        }
//
//        public void setItems(PSTableDictionary[] items, PSTableDictionary chosenItem) {
//            mDictionaryItems = items;
//            int colorActive = ContextCompat.getColor(getContext(), R.color.buttonRoundColor);
//            for (PSTableDictionary item : items) {
//                AppCompatRadioButton button = new AppCompatRadioButton(getContext(), null, R.attr.radioButtonStyle);
//                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.dialog_checkbox_margin));
//                button.setLayoutParams(params);
//                button.setText(item.fc_title);
//                button.setPadding((int) getResources().getDimension(R.dimen.dialog_checkbox_padding_bottom),
//                        0, 0, 0);
//                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.pickerDictionaryTextSize));
//                button.setTextColor(ContextCompat.getColor(getContext(), R.color.pickerDictionaryTextColor));
//                mItemsContainer.addView(button);
//                if (item.fk_id == chosenItem.fk_id) {
//                    mItemsContainer.check(button.getId());
//                }
//            }
//        }
//
//        public void setListener(OnDictionaryItemSelectListener listener) {
//            mItemsContainer.setOnCheckedChangeListener((group, checkedId) -> {
//                PSTableDictionary item = null;
//                for (PSTableDictionary dictionaryItem : mDictionaryItems) {
//                    String selectedItemTitle = ((RadioButton) group.findViewById(checkedId)).getText().toString();
//                    if (selectedItemTitle.equalsIgnoreCase(dictionaryItem.fc_title)) {
//                        item = dictionaryItem;
//                        break;
//                    }
//                }
//                listener.OnItemSelected(item);
//            });
//        }
//
//        @Override
//        protected void initViewVariables(View view) {
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(PSCommonUtils.convertDpToPixels(300), ViewGroup.LayoutParams.WRAP_CONTENT);
//            setLayoutParams(params);
//            mItemsContainer = view.findViewById(R.id.items_container);
//        }
//
//        @Override
//        protected int getCustomViewLayoutId() {
//            return R.layout.picker_dictionary_view;
//        }
//
//    }
    //endregion
}
