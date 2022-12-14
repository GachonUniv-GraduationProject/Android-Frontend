package com.example.graduationproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    static LoginData currentLoginData = null;

    @SerializedName("user")
    private UserData user;

    @SerializedName("token")
    private String token;

    @SerializedName("field")
    private String field;

    @SerializedName("is_individual")
    private boolean isIndividual;

    public LoginData(UserData user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isIndividual() {
        return isIndividual;
    }
}
