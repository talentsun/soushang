package com.baidu.soushang.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
  public static boolean isNetworkConnected(Context context) {
    ConnectivityManager connManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
      return true;
    } else {
      return false;
    }
  }
  
  public static String getNetworkStr(int networkType) {
    if (networkType == 1) {
      return "2G/3G";
    } else if (networkType == 2) {
      return "Wi-Fi";
    } else {
      return "无网络";
    }
  }
  
  public static int getNetworkType(Context context) {
    ConnectivityManager connManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
      if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE
          || activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE_DUN
          || activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE_HIPRI
          || activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE_MMS
          || activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE_SUPL) {
        return 1;
      } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI
          || activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIMAX) {
        return 2;
      }
    }
    
    return 0;
  }
}
