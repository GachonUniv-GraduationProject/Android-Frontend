package com.example.graduationproject;

import com.google.gson.annotations.SerializedName;

public class ProfileData {
    @SerializedName("username")
    private String username;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("is_individual")
    private boolean isIndividual;

    public ProfileData(String username, String displayName, boolean isIndividual) {
        this.username = username;
        this.displayName = displayName;
        this.isIndividual = isIndividual;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isIndividual() {
        return isIndividual;
    }

    public void setIndividual(boolean individual) {
        isIndividual = individual;
    }
}
