package com.baidu.soushang.activities;

import com.baidu.soushang.R;
import com.baidu.soushang.utils.NetworkUtils;
import com.baidu.soushang.views.LoadingView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends BaseActivity {
  protected WebView mWebView;
  protected TextView mNoNetwork;
  protected LoadingView mLoading;

  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.webview);
    
    mWebView = (WebView) findViewById(R.id.webview);
    mNoNetwork = (TextView) findViewById(R.id.no_network);
    mLoading = (LoadingView) findViewById(R.id.loading);
    
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    mWebView.setWebViewClient(new WebViewClient(){

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        return super.shouldOverrideUrlLoading(view, url);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        mLoading.setVisibility(View.GONE);
        super.onPageFinished(view, url);
      }

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mLoading.setVisibility(View.VISIBLE);
        super.onPageStarted(view, url, favicon);
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
