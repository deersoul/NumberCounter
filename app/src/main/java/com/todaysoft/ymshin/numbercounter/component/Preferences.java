package com.todaysoft.ymshin.numbercounter.component;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 파일 저장, 프리퍼런스 관련
 * @author ymshin
 * @Date 2019-02-04
 */
public class Preferences {

    public static String getString(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        return prefs.getString(key.toUpperCase(), "");
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        return prefs.getBoolean(key.toUpperCase(), false);
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences prefs = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        return prefs.getInt(key.toUpperCase(), defValue);
    }

    public static boolean setValue(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key.toUpperCase(), value);
        return editor.commit();
    }

    public static boolean setValue(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key.toUpperCase(), value);
        return editor.commit();
    }

    public static boolean setValue(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key.toUpperCase(), value);
        return editor.commit();
    }

    public static boolean setValue(Context context, String key, long value) {
        SharedPreferences prefs = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key.toUpperCase(), value);
        return editor.commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences prefs = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        return prefs.getLong(key.toUpperCase(), defValue);
    }

}
