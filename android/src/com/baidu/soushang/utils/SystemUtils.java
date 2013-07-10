package com.baidu.soushang.utils;

import android.content.Context;

public class SystemUtils {
  public static String getDeviceName(Context context) {
    return android.os.Build.MODEL;
  }
}
