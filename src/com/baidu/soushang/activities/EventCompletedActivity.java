package com.baidu.soushang.activities;

import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.CommonResponse;
import com.baidu.soushang.cloudapis.UserInfoResponse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EventCompletedActivity extends FragmentActivity implements OnClickListener {
  private TextView mEventScore;
  private LinearLayout mNotLoggedArea;
  private Button mLogin;
  private LinearLayout mLoggedArea;
  private TextView mTotalScore;
  private Button mShop;
  private Button mRank;
  private Button mHome;
  
  private Baidu mBaidu;
  private Handler mMainHandler;
  
  private ApiResponseCallback<UserInfoResponse> mUserInfoCallback = new ApiResponseCallback<UserInfoResponse>() {
    
    @Override
    public void onResults(UserInfoResponse arg0) {
      if (arg0 != null && arg0.getRetCode() == 0 && arg0.getUser() != null) {
        final int credit = arg0.getUser().getIntegral();
        final int point = arg0.getUser().getPoint();
        
        mMainHandler.post(new Runnable() {
          
          @Override
          public void run() {
            updateTotalScore(credit, point);
          }
        });
      }
    }
    
    @Override
    public void onError(Throwable arg0) {
      mMainHandler.post(new Runnable() {
        
        @Override
        public void run() {
          updateTotalScore(0, 0);
        }
      });
    }
  };
  
  private ApiResponseCallback<CommonResponse> mLoginCallback = new ApiResponseCallback<CommonResponse>() {
    
    @Override
    public void onResults(CommonResponse arg0) {
      if (arg0 != null && arg0.getRetCode() == 0) {
        Config.setLogged(getApplicationContext(), true);
        
        SouShangApplication application = (SouShangApplication) getApplication();
        Apis.answer(EventCompletedActivity.this, application.getAnswers(), Config.getAccessToken(EventCompletedActivity.this), null);
        initLoggedArea();
      } else {
        Log.i("ret_code", "" + arg0.getRetCode());
        Log.i("ret_msg", arg0.getRetMsg());
        Config.setLogged(getApplicationContext(), false);
        Toast.makeText(EventCompletedActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
      }
    }
    
    @Override
    public void onError(Throwable arg0) {
      Config.setLogged(getApplicationContext(), false);
      Toast.makeText(EventCompletedActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
    }
  };
  
  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.event_completed);
    
    mEventScore = (TextView) findViewById(R.id.event_score);
    mNotLoggedArea = (LinearLayout) findViewById(R.id.not_logged_area);
    mLogin = (Button) findViewById(R.id.login);
    mLoggedArea = (LinearLayout) findViewById(R.id.logged_area);
    mTotalScore = (TextView) findViewById(R.id.total_score);
    mShop = (Button) findViewById(R.id.shop);
    mRank = (Button) findViewById(R.id.rank);
    mHome = (Button) findViewById(R.id.home);
    
    if (!Config.isLogged(this)) {
      initNotLoggedArea(getIntent());
    } else {
      initLoggedArea();
    }
    
    mLogin.setOnClickListener(this);
    mShop.setOnClickListener(this);
    mRank.setOnClickListener(this);
    mHome.setOnClickListener(this);
    
    mMainHandler = new Handler();
    
    super.onCreate(arg0);
  }
  
  private void initNotLoggedArea(Intent intent) {
    mNotLoggedArea.setVisibility(View.VISIBLE);
    mLoggedArea.setVisibility(View.GONE);
    
    int credit = intent.getIntExtra(Intents.EXTRA_CREDIT, 0);
    int point = intent.getIntExtra(Intents.EXTRA_POINT, 0);
    
    mEventScore.setText(String.format(getResources().getString(R.string.event_score), credit, point));
  }
  
  private void initLoggedArea() {
    mNotLoggedArea.setVisibility(View.GONE);
    mLoggedArea.setVisibility(View.VISIBLE);
    
    Apis.getUserInfo(this, Config.getAccessToken(this), mUserInfoCallback);
  }
  
  private void updateTotalScore(int credit, int point) {
    mTotalScore.setText(String.format(getResources().getString(R.string.total_score), credit, point));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  public void onClick(View v) {
    if (v == mLogin) {
      mBaidu = new Baidu(SouShangApplication.APP_KEY, SouShangApplication.APP_SECRET, EventCompletedActivity.this);
      mBaidu.authorize(EventCompletedActivity.this, new BaiduDialogListener() {

        @Override
        public void onError(BaiduDialogError arg0) {
          }

        @Override
        public void onComplete(Bundle arg0) {
          Log.i("access_token", mBaidu.getAccessToken());
          Config.setAccessToken(EventCompletedActivity.this, mBaidu.getAccessToken());
          Apis.Login(EventCompletedActivity.this, mBaidu.getAccessToken(), mLoginCallback);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onBaiduException(BaiduException arg0) {
        }
      });
    } else if (v == mShop) {
      Intent intent = new Intent(EventCompletedActivity.this, ShopActivity.class);
      startActivity(intent);
      
      finish();
    } else if (v == mRank) {
      Intent intent = new Intent(EventCompletedActivity.this, RankActivity.class);
      startActivity(intent);
      
      finish();
    } else if (v == mHome) {
      Intent intent = new Intent(EventCompletedActivity.this, HomeActivity.class);
      startActivity(intent);
      
      finish();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    if (!Config.isLogged(this)) {
      initNotLoggedArea(intent);
    } else {
      initLoggedArea();
    }
    super.onNewIntent(intent);
  }
}
