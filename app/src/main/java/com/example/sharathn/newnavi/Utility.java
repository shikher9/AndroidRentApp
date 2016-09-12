package com.example.sharathn.newnavi;

import android.content.Context;
import android.content.SharedPreferences;

public class Utility {

    public static final String MY_PREFS_NAME = "rent_app_pref";

    public static void saveValueToSP(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValueFromSP(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);

        if (prefs.contains(key)) {
            return prefs.getString(key, "null");
        } else {
            return "null";
        }
    }


    public static boolean deleteValueFromSP(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (prefs.contains(key)) {
            editor.remove(key);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }
}