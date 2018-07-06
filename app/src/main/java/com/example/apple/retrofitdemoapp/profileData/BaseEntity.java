package com.example.apple.retrofitdemoapp.profileData;

import com.example.apple.retrofitdemoapp.Models.ErrorResult;


public class BaseEntity {

    private ErrorResult errorResult;

    public ErrorResult getErrorResult() {
        return errorResult;
    }

    public boolean isSuccess() {
        return errorResult == null;
    }

    public void setErrorResult(ErrorResult errorResult) {
        this.errorResult = errorResult;
    }

    public static <T extends BaseEntity> T create(ErrorResult errorResult, Class<T> tClass) {
        T baseEntity = null;
        try {
            baseEntity = tClass.newInstance();
            baseEntity.setErrorResult(errorResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseEntity;
    }
}
