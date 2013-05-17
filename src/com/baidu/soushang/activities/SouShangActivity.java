package com.baidu.soushang.activities;

import android.os.Bundle;

public class SouShangActivity extends WebViewActivity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mWebView.loadUrl("http://m.baidu.com");
  }
  
}
