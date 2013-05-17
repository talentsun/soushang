package com.baidu.soushang.utils;

import com.baidu.soushang.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DialogUtils {
  public static void showSearchResultDialog(Context context, String query) {
    AlertDialog.Builder dialogBuilder = new Builder(context);

    dialogBuilder.setTitle(query);

    WebView webview = new WebView(context);
    webview.loadUrl("http://m.baidu.com/s?word=" + query);
    webview.setWebViewClient(new WebViewClient() {

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
      }

    });

    dialogBuilder.setView(webview);
    dialogBuilder.setNegativeButton(R.string.i_know, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        }
    });

    dialogBuilder.show();
  }

  public static void showSearchResultDialog(Context context, String title, String baseUrl,
      String content, DialogInterface.OnClickListener onClickListener) {
    AlertDialog.Builder dialogBuilder = new Builder(context);

    dialogBuilder.setTitle(title);

    WebView webview = new WebView(context);
    webview.loadDataWithBaseURL(baseUrl, content, "text/html", "UTF-8", null);
    webview.setWebViewClient(new WebViewClient() {

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
      }

    });

    dialogBuilder.setView(webview);
    dialogBuilder.setNegativeButton(R.string.i_know, onClickListener);

    dialogBuilder.show();
  }
}
