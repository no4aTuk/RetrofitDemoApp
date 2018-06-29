package com.example.apple.retrofitdemoapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {
    @Expose(deserialize = true, serialize = false)
    public String access_token;
    @Expose
    public String token_type;
    @Expose
    public int expires_in;
    @Expose
    public String refresh_token;
    @SerializedName("as:client_id")
    @Expose
    public String as_client_id;
    @Expose
    public String userName;
    @Expose
    public boolean isSavedProfile;
    @SerializedName(".issued")
    @Expose
    public String issued;
    @Expose
    @SerializedName(".expires")
    public String expires;
}
