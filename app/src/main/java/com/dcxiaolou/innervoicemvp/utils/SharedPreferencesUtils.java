package com.dcxiaolou.innervoicemvp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences;

    public SharedPreferencesUtils() {
    }

    /*
    * 初始化SharePreference工具类
    * */
    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static void saveString(String key, String value){
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(String key){
        return sharedPreferences.getString(key, "");
    }

    public static void saveInt(String key, int value){
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key){
        return sharedPreferences.getInt(key, 0);
    }

}
