package com.example.apple.retrofitdemoapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class BackendError {
    @SerializedName("Message")
    private String message;
    @SerializedName("ModelState")
    private Map<String, String[]> modelState;

    public String getErrorMessage() {
        String resultMessage = message;
        StringBuilder stringBuilder = new StringBuilder();
        if (modelState != null) {
            Collection<String[]> stateCollections = modelState.values();
            for (String[] collection: stateCollections) {
                for(int i = 0; i < collection.length; i++) {
                    boolean isLastValue = i == collection.length - 1;
                    String value = collection[i];
                    if (isLastValue) {
                        stringBuilder.append(value);
                        continue;
                    }
                    stringBuilder.append(value).append("/n");
                }
            }
            resultMessage = stringBuilder.toString();
            return resultMessage;
        }
        return resultMessage;
    }
}
