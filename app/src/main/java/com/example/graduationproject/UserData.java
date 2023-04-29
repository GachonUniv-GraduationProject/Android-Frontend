package com.example.graduationproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data class containing the user info
 * */
public class UserData implements Parcelable {
    /**
     * ID number of the user
     * */
    @SerializedName("id")
    private int id;
    /**
     * Email of the user
     * */
    @SerializedName("email")
    private String email;
    /**
     * Username of the user
     * */
    @SerializedName("username")
    private String username;
    /**
     * Password of the user
     * */
    @SerializedName("password")
    private String password;
    /**
     * Phone number of the user
     * */
    @SerializedName("phone")
    private String phone;
    /**
     * Nickname of the user
     * */
    @SerializedName("display_name")
    private String displayName;

    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
        email = "";
    }

    public UserData(String username, String password, String email, String phone, String displayName) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = phone;
        this.displayName = displayName;
    }

    public UserData(Parcel in) {
        id = in.readInt();
        email = in.readString();
        username = in.readString();
        password = in.readString();
    }

    /**
     * Get ID number
     * */
    public int getId() {
        return id;
    }

    /**
     * Set ID number
     * */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get Email address
     * */
    public String getEmail() {
        return email;
    }

    /**
     * Set Email address
     * */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get username
     * */
    public String getUsername() {
        return username;
    }

    /**
     * Set username
     * */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get password
     * */
    public String getPassword() {
        return password;
    }

    /**
     * Set password
     * */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get phone number
     * */
    public String getPhone() {
        return phone;
    }

    /**
     * Get nickname
     * */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Parcel Creator
     * */
    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel parcel) {
            return new UserData(parcel);
        }

        @Override
        public UserData[] newArray(int i) {
            return new UserData[i];
        }
    };

    @Override
    public int describeContents() { return 0; }

    /**
     * Write member variables to parcel
     * */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
    }

}
