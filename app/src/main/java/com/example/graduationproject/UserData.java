package com.example.graduationproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserData implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
        email = "";
    }

    public UserData(String username, String id, String password, String email) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserData(Parcel in) {
        id = in.readString();
        email = in.readString();
        username = in.readString();
        password = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
    }

}
