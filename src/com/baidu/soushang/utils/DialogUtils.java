package com.baidu.soushang.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.text.TextUtils;

public class DialogUtils {
	public static void showSearchResultDialog(Context context, String title, String resultUrl) {
		AlertDialog.Builder dialogBuilder = new Builder(context);
		
		if (!TextUtils.isEmpty(title)) {
			dialogBuilder.setTitle(title);
		}
		
		if (!TextUtils.isEmpty(resultUrl)) {
			
		}
	}
}
