package com.example.apple.retrofitdemoapp.Models;

import com.google.gson.annotations.SerializedName;

public class Token {
    public String access_token;
    public String token_type;
    public int expires_in;
    public String refresh_token;
    @SerializedName("as:client_id")
    public String as_client_id;
    public String userName;
    public boolean isSavedProfile;
    @SerializedName(".issued")
    public String issued;
    @SerializedName(".expires")
    public String expires;
}
