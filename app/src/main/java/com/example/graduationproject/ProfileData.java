package com.example.graduationproject;

import com.google.gson.annotations.SerializedName;

/**
 * Profile data of this user
 * */
public class ProfileData {
    /**
     * ID of this user
     * */
    @SerializedName("username")
    private String username;
    /**
     * Nickname of this user
     * */
    @SerializedName("display_name")
    private String displayName;
    /**
     * Whether if this user is individual (not business)
     * */
    @SerializedName("is_individual")
    private boolean isIndividual;

    public ProfileData(String username, String displayName, boolean isIndividual) {
        this.username = username;
        this.displayName = displayName;
        this.isIndividual = isIndividual;
    }

    /**
     * Get the user ID
     * */
    public String getUsername() {
        return username;
    }

    /**
     * Set the user ID
     * */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the nickname
     * */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set the nickname
     * */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Check if this user is individual
     * */
    public boolean isIndividual() {
        return isIndividual;
    }
}
