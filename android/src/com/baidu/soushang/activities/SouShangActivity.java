package com.baidu.soushang.activities;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class SouShangActivity extends BaseActivity {
  private TextView mSoushang;
  private TextView mSoushangTitle;
  private TextView mSoushangContent;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.soushang);
    
    mSoushang = (TextView) findViewById(R.id.soushang);
    mSoushangTitle = (TextView) findViewById(R.id.soushang_title);
    mSoushangContent = (TextView) findViewById(R.id.soushang_content);
    
    Typeface typeface = Typeface.createFromAsset(getAssets(), SouShangApplication.FONT);
    mSoushang.setTypeface(typeface);
    mSoushangTitle.setTypeface(typeface);
    mSoushangContent.setTypeface(typeface);
  }
  
}
