package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.utils.NetworkUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class WebViewDialog extends Dialog {
  private WebView mWebView;
  private TextView mNoNetwork;
  private Button mKnow;
  
  public WebViewDialog(Context context) {
    this(context, 0);
  }

  public WebViewDialog(Context context, int theme) {
    super(context, theme);
    
    setContentView(R.layout.webview_dialog);
    
    mWebView = (WebView) findViewById(R.id.webview);
    mNoNetwork = (TextView) findViewById(R.id.no_network);
    mKnow = (Button) findViewById(R.id.know);
    
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    mWebView.setWebViewClient(new WebViewClient(){

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        return super.shouldOverrideUrlLoading(view, url);
      }
      
    });
    
    mKnow.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
    
    setCanceledOnTouchOutside(false);
  }

  @Override
  public void onBackPressed() {
    if (mWebView.canGoBack()) {
      mWebView.goBack();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onStart() {
    if (!NetworkUtils.isNetworkConnected(getContext())) {
      mNoNetwork.setVisibility(View.VISIBLE);
      mWebView.setVisibility(View.GONE);
    }
    
    super.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  public void show(String title, String url) {
    setTitle(title);
    mWebView.loadUrl(url);
    super.show();
  }
  
  public void show(String title, String baseUrl, String content) {
    setTitle(title);
    mWebView.loadDataWithBaseURL(baseUrl, content, "text/html", "UTF-8", null);
    super.show();
  }
}
