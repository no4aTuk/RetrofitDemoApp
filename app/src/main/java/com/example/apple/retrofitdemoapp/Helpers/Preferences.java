package com.example.apple.retrofitdemoapp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static final String PREFERENCE_TOKEN = "PREFERENCE_TOKEN";
    private static final String PREFERENCE_REFRESH_TOKEN = "PREFERENCE_REFRESH_TOKEN";

    public static String getToken(Context context) {
        return getValue(context, PREFERENCE_TOKEN, "");
    }

    public static void setToken(Context context, String token) {
        setValue(context, PREFERENCE_TOKEN, token);
    }

    public static String getRefreshToken(Context context) {
        return getValue(context, PREFERENCE_REFRESH_TOKEN, "");
    }

    public static void setRefreshToken(Context context, String refreshToken) {
        setValue(context, PREFERENCE_REFRESH_TOKEN, refreshToken);
    }

    private static String getValue(Context context, String key, String defValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, defValue);
    }

    private static void setValue(Context context, String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(key, value);
        ed.apply();
    }
}
