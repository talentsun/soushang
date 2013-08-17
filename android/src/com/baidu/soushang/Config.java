package com.baidu.soushang;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class Config {
  public static final String PREFS_NAME = "com.baidu.soushang";
  public static final String KEY_ACCESS_TOKEN = "access_token";
  public static final String KEY_LOGGED = "logged_v2";
  public static final String KEY_LATEST_NEWS_DATE = "latest_news_date";
  public static final String KEY_UDID = "udid";
  public static final String KEY_USERNAME = "user_name";
  public static final String KEY_AVATAR = "avatar";
  public static final String KEY_USERID = "user_id";
  
  public static SharedPreferences getConfigs(Context context) {
    return context.getSharedPreferences(PREFS_NAME, 0);
  }
  
  public static void setExchangeUsrInfo(Context context,String key,String msg){
    SharedPreferences  sp = context.getSharedPreferences(Intents.EXTRA_SHOP_SEND_ADDRESS,
      Activity.MODE_PRIVATE);
    SharedPreferences.Editor  et = sp.edit();
    et.putString(key, msg);
    et.commit();
  }
  public static String getExchangeUserInfo(Context context,String key){
    SharedPreferences  sp = context.getSharedPreferences(Intents.EXTRA_SHOP_SEND_ADDRESS,
      Activity.MODE_PRIVATE);
    String shop = sp.getString(key, "first");
    return shop;
  }
  public static void setAccessToken(Context context, String accessToken) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.putString(KEY_ACCESS_TOKEN, accessToken);
    editor.commit();
  }

  public static void removeAccessToken(Context context) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.remove(KEY_ACCESS_TOKEN);
    editor.commit();
  }

  public static String getAccessToken(Context context) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    return settings.getString(KEY_ACCESS_TOKEN, null);
  }

  public static void setLogged(Context context, boolean logged) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.putBoolean(KEY_LOGGED, logged);
    editor.commit();
  }

  public static boolean isLogged(Context context) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    return settings.getBoolean(KEY_LOGGED, false);
  }

  public static void setLatestNewsDate(Context context, String date) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.putString(KEY_LATEST_NEWS_DATE, date);
    editor.commit();
  }

  public static String getLatestNewsDate(Context context) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    return settings.getString(KEY_LATEST_NEWS_DATE, null);
  }

  public static void setUserName(Context context, String userName) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.putString(KEY_USERNAME, userName);
    editor.commit();
  }

  public static String getUserName(Context context) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    return settings.getString(KEY_USERNAME, null);
  }

  public static void setAvatar(Context context, String avatar) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.putString(KEY_AVATAR, avatar);
    editor.commit();
  }

  public static String getAvatar(Context context) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    return settings.getString(KEY_AVATAR, null);
  }

  public static void setUserId(Context context, long userId) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    Editor editor = settings.edit();
    editor.putLong(KEY_USERID, userId);
    editor.commit();
  }

  public static long getUserId(Context context) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    return settings.getLong(KEY_USERID, 0);
  }

  public static String getUDID(Context context) {
    SharedPreferences settings = context
        .getSharedPreferences(PREFS_NAME, 0);
    String udid = settings.getString(KEY_UDID, null);

    if (TextUtils.isEmpty(udid)) {
      udid = generateUDID();
      Editor editor = settings.edit();
      editor.putString(KEY_UDID, udid);
      editor.commit();
    }

    return udid;
  }

  private static String generateUDID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }
}
