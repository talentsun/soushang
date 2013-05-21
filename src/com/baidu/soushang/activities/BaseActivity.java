package com.baidu.soushang.activities;

import com.baidu.mobstat.StatService;

import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

  @Override
  protected void onPause() {
    StatService.onPause(this);
    super.onPause();
  }

  @Override
  protected void onResume() {
    StatService.onResume(this);
    super.onResume();
  }

}
