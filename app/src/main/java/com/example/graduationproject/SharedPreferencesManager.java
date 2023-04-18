package com.example.graduationproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared Preference Management Class for storing/recalling auto-login data
 * */
public class SharedPreferencesManager {
    /**
     * Preference file name
     * */
    private static final String PREFERENCE_NAME = "auto_login_pref";

    /**
     * Get Preference instance
     * */
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Remove preference data
     * */
    public static void clearPreferences(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Save login information
     * */
    public static void setLoginInfo(Context context, String id, String password) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", id);
        editor.putString("password", password);

        editor.apply();
    }

    /**
     * Get login information from preference data
     * */
    public static Map<String, String> getLoginInfo(Context context) {
        SharedPreferences prefs = getPreferences(context);
        Map<String, String> loginInfo = new HashMap<>();
        // Get id, password
        String id = prefs.getString("id", "");
        String password = prefs.getString("password", "");

        // Add to Map instance and return it
        if(!id.equals("")) {
            loginInfo.put("id", id);
            loginInfo.put("password", password);
            return loginInfo;
        }
        else
            return null;
    }
}
