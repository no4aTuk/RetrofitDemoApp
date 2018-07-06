package com.example.apple.retrofitdemoapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainScreenData {

    @SerializedName("Weight")
    @Expose
    public int Weight;

    @SerializedName("Height")
    @Expose
    public int Height;

    @Override
    public String toString() {
        return "MainScreenData{" +
                "Weight=" + Weight +
                ", Height=" + Height +
                '}';
    }


}
