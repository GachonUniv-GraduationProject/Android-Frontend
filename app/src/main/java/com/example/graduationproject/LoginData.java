package com.example.graduationproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data class for login
 * */
public class LoginData {
    /**
     * Current user's information
     * */
    static LoginData currentLoginData = null;

    /**
     * User data
     * */
    @SerializedName("user")
    private UserData user;

    /**
     * Authentication token
     * */
    @SerializedName("token")
    private String token;

    /**
     * Field of the user
     * */
    @SerializedName("field")
    private String field;

    /**
     * Whether this user is individual (not business)
     * */
    @SerializedName("is_individual")
    private boolean isIndividual;

    public LoginData(UserData user, String token) {
        this.user = user;
        this.token = token;
    }

    /**
     * Get user data
     * */
    public UserData getUser() {
        return user;
    }

    /**
     * Set user data
     * */
    public void setUser(UserData user) {
        this.user = user;
    }

    /**
     * Get authentication token
     * */
    public String getToken() {
        return token;
    }

    /**
     * Set authentication token
     * */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get field name of this user
     * */
    public String getField() {
        return field;
    }

    /**
     * Set the field name of this user
     * */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Check if this user is individual
     * */
    public boolean isIndividual() {
        return isIndividual;
    }
}
