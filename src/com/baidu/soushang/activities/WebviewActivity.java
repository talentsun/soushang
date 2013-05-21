package com.baidu.soushang.activities;

import com.baidu.soushang.R;
import com.baidu.soushang.utils.NetworkUtils;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends BaseActivity {
  protected WebView mWebView;
  protected TextView mNoNetwork;

  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.webview);
    
    mWebView = (WebView) findViewById(R.id.webview);
    mNoNetwork = (TextView) findViewById(R.id.no_network);
    
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    mWebView.setWebViewClient(new WebViewClient(){

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        return super.shouldOverrideUrlLoading(view, url);
      }
      
    });
    
    super.onCreate(arg0);
  }

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
  }

  @Override
  protected void onStart() {
    // TODO Auto-generated method stub
    super.onStart();
  }

  @Override
  protected void onStop() {
    // TODO Auto-generated method stub
    super.onStop();
  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
  }

  @Override
  protected void onResume() {
    if (!NetworkUtils.isNetworkConnected(this)) {
      mNoNetwork.setVisibility(View.VISIBLE);
      mWebView.setVisibility(View.GONE);
    }
    super.onResume();
  }

}
