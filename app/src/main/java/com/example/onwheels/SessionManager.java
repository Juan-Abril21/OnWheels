package com.example.onwheels;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private static SessionManager instance;

    private SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
