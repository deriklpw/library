package com.fih.idx.deriklibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by derik on 18-1-23.
 */

public class ComplexPreferences {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();

    public ComplexPreferences(Context ctx, String name, int mode) {
        if (name == null || name.equals("")) {
            name = "complex_preferences";
        }
        sharedPreferences = ctx.getSharedPreferences(name, mode);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void putObject(String key, Object object) {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("key is null or empty");
        }

        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }
        editor.putString(key, gson.toJson(object));
    }

    public void commit() {
        editor.commit();
    }

    public void clearObject() {
        editor.clear();
    }

    public void remove(String key) {
        editor.remove(key);
    }

    public <T> T getObject(String key, Class<T> a, T defaultValue) {
        try {
            String json = sharedPreferences.getString(key, null);
            if (json == null) {
                return defaultValue;
            } else {
                return gson.fromJson(json, a);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Object stored with key " + key + " is instanceof other class");
        }
    }

}
