package com.baidu.soushang;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Config {
  public static final String PREFS_NAME = "com.baidu.soushang";
  public static final String KEY_ACCESS_TOKEN = "access_token";
  public static final String KEY_LOGGED = "logged";
  
  public static SharedPreferences getConfigs(Context context) {
    return context.getSharedPreferences(PREFS_NAME, 0);
  }
  
  public static void setAccessToken(Context context, String accessToken) {
    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.putString(KEY_ACCESS_TOKEN, accessToken);
    editor.commit();
  }
  
  public static String getAccessToken(Context context) {
    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    return settings.getString(KEY_ACCESS_TOKEN, null);
  }
  
  public static void setLogged(Context context, boolean logged) {
    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.putBoolean(KEY_LOGGED, logged);
    editor.commit();
  }
  
  public static boolean isLogged(Context context) {
    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    return settings.getBoolean(KEY_LOGGED, false);
  }
}
