package com.edu.tutorapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class SharedPreferencesUtils {
    private static final String SHARED_PREF_NAME = "MyAppPreferences"; // Name of the preference file
    private static final String KEY_USERNAME = "username"; // Key for storing username
    private static final String KEY_ROLE = "role"; // Key for storing role
    private static final String KEY_ACCESS_TOKEN = "access_token"; // Key for storing access token
    private static final String KEY_REFRESH_TOKEN = "refresh_token"; // Key for storing refresh token
    private static final String KEY_TIMEOUT = "timeout"; // Key for storing timeout
    private static final String KEY_FACULTY= "faculty";
    private static final String KEY_EMAIL= "email";
    private static final String KEY_MAJOR = "major";
    private static final String KEY_RATE = "rate";
    private static final String KEY_RATING = "rating";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    // Method to save a string value
    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Method to get a string value
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Method to save an integer value
    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // Method to get an integer value
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    // Method to save a boolean value
    public void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Method to get a boolean value
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Method to delete a value
    public void deleteValue(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    // Method to clear all preferences
    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Specific methods for your keys
    public void saveUsername(String username) {
        saveString(KEY_USERNAME, username);
    }

    public String getUsername() {
        return getString(KEY_USERNAME, "none");
    }

    public void saveRole(String role) {
        saveString(KEY_ROLE, role);
    }

    public String getRole() {
        return getString(KEY_ROLE, "none");
    }

    public void saveAccessToken(String accessToken) {
        saveString(KEY_ACCESS_TOKEN, accessToken);
    }

    public String getAccessToken() {
        return getString(KEY_ACCESS_TOKEN, "none");
    }

    public void saveRefreshToken(String refreshToken) {
        saveString(KEY_REFRESH_TOKEN, refreshToken);
    }

    public void createProfile(String username, String email, String faculty, String major, String rate, String rating){
        saveString(KEY_EMAIL, email);
        saveString(KEY_FACULTY, faculty);
        saveString(KEY_MAJOR, major);
        saveString(KEY_RATING, rating);
        saveString(KEY_RATE, rate);
    }
    public JSONObject getProfile() throws JSONException {
        String username = getString(KEY_USERNAME, "None");
        String email = getString(KEY_EMAIL, "None");
        String faculty = getString(KEY_FACULTY, "None");
        String major = getString(KEY_MAJOR, "None");
        String rate = getString(KEY_RATE, "None");
        String rating = getString(KEY_RATING, "None");

        JSONObject profile = new JSONObject();
        JSONObject data = new JSONObject();
        profile.put("data", data);
        data.put("username", username);
        data.put("email", email);
        data.put("faculty", faculty);
        data.put("major", major);
        data.put("rate", rate);
        data.put("rating", rating);

        return profile;
    }
    public String getRefreshToken() {
        return getString(KEY_REFRESH_TOKEN, "none");
    }

    public void saveTimeout(int timeout) {
        saveInt(KEY_TIMEOUT, timeout);
    }

    public int getTimeout() {
        return getInt(KEY_TIMEOUT, 0);
    }
}