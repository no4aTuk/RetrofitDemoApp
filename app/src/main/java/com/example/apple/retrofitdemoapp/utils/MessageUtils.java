package com.example.apple.retrofitdemoapp.utils;

import android.content.Context;
import android.widget.Toast;

public class MessageUtils {

    public static void showToast(Context context, String error) {

        if (error != null && context != null && error.length() > 0 ) {
            if (error.length() < 15) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        }

    }
}
