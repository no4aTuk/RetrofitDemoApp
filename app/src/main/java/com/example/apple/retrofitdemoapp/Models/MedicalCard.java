package com.example.apple.retrofitdemoapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicalCard {

    @SerializedName("Id")
    @Expose
    public long Id;
    @SerializedName("BloodType")
    @Expose
    public int BloodType;
    @SerializedName("RhFactor")
    @Expose
    public int RhFactor;
    @SerializedName("Comment")
    @Expose
    public String Comment;
    @SerializedName("HasAllergicReactions")
    @Expose
    public boolean HasAllergicReactions;
    @SerializedName("HasConsultations")
    @Expose
    public Boolean HasConsultations;
    @SerializedName("FileToken")
    @Expose
    public String FileToken;
    @SerializedName("FileFolder")
    @Expose
    public String FileFolder;
    @SerializedName("Weight")
    @Expose
    public int Weight;
    @SerializedName("Height")
    @Expose
    public int Height;


    @Override
    public String toString() {
        return "MedicalCard{" +
                "Id=" + Id +
                ", BloodType=" + BloodType +
                ", RhFactor=" + RhFactor +
                ", Comment='" + Comment + '\'' +
                ", HasAllergicReactions=" + HasAllergicReactions +
                ", HasConsultations=" + HasConsultations +
                ", FileToken='" + FileToken + '\'' +
                ", FileFolder='" + FileFolder + '\'' +
                ", Weight=" + Weight +
                ", Height=" + Height +
                '}';
    }
}
