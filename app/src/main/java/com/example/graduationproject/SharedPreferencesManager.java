package com.example.graduationproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedPreferencesManager {
    private static final String PREFERENCE_NAME = "auto_login_pref";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void clearPreferences(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public static void setLoginInfo(Context context, String id, String password) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", id);
        editor.putString("password", password);

        editor.apply();
    }

    public static Map<String, String> getLoginInfo(Context context) {
        SharedPreferences prefs = getPreferences(context);
        Map<String, String> loginInfo = new HashMap<>();
        String id = prefs.getString("id", "");
        String password = prefs.getString("password", "");

        if(!id.equals("")) {
            loginInfo.put("id", id);
            loginInfo.put("password", password);
            return loginInfo;
        }
        else
            return null;
    }
}
