package com.baidu.soushang.activities;

import com.baidu.soushang.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends FragmentActivity implements OnClickListener {
  private Button mSouShang;
  private Button mDailyEvent;
  private Button mRank;
  private Button mShop;
  
  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.home);
    
    mSouShang = (Button) findViewById(R.id.soushang);
    mDailyEvent = (Button) findViewById(R.id.daily_event);
    mRank = (Button) findViewById(R.id.rank);
    mShop = (Button) findViewById(R.id.shop);
    
    mSouShang.setOnClickListener(this);
    mDailyEvent.setOnClickListener(this);
    mRank.setOnClickListener(this);
    mShop.setOnClickListener(this);
    
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
  public void onClick(View v) {
    if (v == mSouShang) {
      Intent intent = new Intent(this, SouShangActivity.class);
      startActivity(intent);
    } else if (v == mDailyEvent) {
      Intent intent = new Intent(this, QuestionActivity.class);
      startActivity(intent);
    } else if (v == mRank) {
      Intent intent = new Intent(this, RankActivity.class);
      startActivity(intent);
    } else if (v == mShop) {
      Intent intent = new Intent(this, ShopActivity.class);
      startActivity(intent);
    }
  }

}
