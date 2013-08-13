package com.baidu.soushang.activities;

import com.baidu.soushang.R;
import com.umeng.analytics.MobclickAgent;

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
    super.onPause();
    MobclickAgent.onPause(this);
  }

  @Override
  protected void onResume() {
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    super.onResume();
    MobclickAgent.onResume(this);
  }

}
