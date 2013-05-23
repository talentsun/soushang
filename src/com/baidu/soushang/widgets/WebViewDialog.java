package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.utils.NetworkUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class WebViewDialog extends Dialog {
  private WebView mWebView;
  private TextView mNoNetwork;
  private Button mKnow;
  private TextView mTitle;
  
  public WebViewDialog(Context context) {
    this(context, R.style.WebViewDialog);
  }

  public WebViewDialog(Context context, int theme) {
    super(context, theme);
    
    setContentView(R.layout.webview_dialog);
    
    mWebView = (WebView) findViewById(R.id.webview);
    mNoNetwork = (TextView) findViewById(R.id.no_network);
    mKnow = (Button) findViewById(R.id.know);
    mTitle = (TextView) findViewById(R.id.dialog_title);
    
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
    
    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), SouShangApplication.FONT);
    mNoNetwork.setTypeface(typeface);
    mKnow.setTypeface(typeface);
    mTitle.setTypeface(typeface);
    
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
    mTitle.setText(title);
    mWebView.clearHistory();
    mWebView.loadUrl(url);
    super.show();
  }
}
