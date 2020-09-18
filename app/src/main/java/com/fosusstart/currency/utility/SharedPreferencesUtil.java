package com.fosusstart.currency.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Вспомогательный класс для использования Shared Preferences
 * (в данном случае хранится json с курсом валют)
 * */
public class SharedPreferencesUtil {

    private final String jsonKey = "json";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences("currencies", Context.MODE_PRIVATE);
    }

    public String getJson() {
        return sharedPreferences.getString(jsonKey, "");
    }

    public void saveJson(String json) {
        sharedPreferences.edit().putString(jsonKey, json).apply();
    }

}
