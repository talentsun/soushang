package com.baidu.soushang.activities;

import com.baidu.mobstat.StatService;
import com.baidu.soushang.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

  @Override
  protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
  }

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    StatService.onPause(this);
    super.onPause();
  }

  @Override
  protected void onResume() {
    StatService.onResume(this);
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    super.onResume();
  }

}
