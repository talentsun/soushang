package com.baidu.soushang.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.CommonResponse;
import com.baidu.soushang.cloudapis.UserInfoResponse;
import com.limijiaoyin.socialsdk.dialogs.CommonShareDialog;
import com.limijiaoyin.socialsdk.dialogs.ShareDialog;
import com.umeng.analytics.MobclickAgent;

public class EventCompletedActivity extends BaseActivity implements
        OnClickListener {
  private LinearLayout mNotLoggedArea;
  private Button mLogin;
  private TextView mEventScoreNoLogged;
  private TextView mEventScoreTextNoLogged;
  private LinearLayout mLoggedArea;
  private TextView mEventScoreLogged;
  private TextView mEventScoreTextLogged;
  private TextView mTotalScore;
  private TextView mTotalScoreText;

  private Button mShop;
  private Button mRank;
  private Button mHome;
  private Button mShare;

  private CommonShareDialog mShareDialog;
  private Baidu mBaidu;
  private Handler mMainHandler;
  private int mEventPoint;

  private ApiResponseCallback<UserInfoResponse> mUserInfoCallback =
      new ApiResponseCallback<UserInfoResponse>() {

        @Override
        public void onResults(UserInfoResponse arg0) {
          if (arg0 != null && arg0.getRetCode() == 0
                    && arg0.getUser() != null) {
            final int point = arg0.getUser().getPoint();

            mMainHandler.post(new Runnable() {

              @Override
              public void run() {
                updateTotalScore(point);
              }
            });
          }
        }

        @Override
        public void onError(Throwable arg0) {
          mMainHandler.post(new Runnable() {

            @Override
            public void run() {
              updateTotalScore(0);
            }
          });
        }
      };

  private ApiResponseCallback<CommonResponse> mLoginCallback =
      new ApiResponseCallback<CommonResponse>() {

        @Override
        public void onResults(CommonResponse arg0) {
          if (arg0 != null && arg0.getRetCode() == 0) {
            Config.setLogged(getApplicationContext(), true);

            SouShangApplication application = (SouShangApplication) getApplication();
            Apis.answer(EventCompletedActivity.this,
                        application.getAnswers(),
                        Config.getAccessToken(EventCompletedActivity.this),
                        null);
            initLoggedArea(null);
          } else {
            Config.setLogged(getApplicationContext(), false);
            Toast.makeText(EventCompletedActivity.this,
                        getResources().getString(R.string.login_failed),
                        Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void onError(Throwable arg0) {
          Config.setLogged(getApplicationContext(), false);
          Toast.makeText(EventCompletedActivity.this,
                    getResources().getString(R.string.login_failed),
                    Toast.LENGTH_SHORT).show();
        }
      };

  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.event_completed);

    mNotLoggedArea = (LinearLayout) findViewById(R.id.not_logged_area);
    mEventScoreNoLogged = (TextView) findViewById(R.id.event_score_no_logged);
    mEventScoreTextNoLogged = (TextView) findViewById(R.id.event_score_text_no_logged);
    mLogin = (Button) findViewById(R.id.login);
    mLoggedArea = (LinearLayout) findViewById(R.id.logged_area);
    mEventScoreLogged = (TextView) findViewById(R.id.event_score_logged);
    mEventScoreTextLogged = (TextView) findViewById(R.id.event_score_text_logged);
    mTotalScore = (TextView) findViewById(R.id.total_score);
    mTotalScoreText = (TextView) findViewById(R.id.total_score_text);
    mShop = (Button) findViewById(R.id.shop);
    mRank = (Button) findViewById(R.id.rank);
    mHome = (Button) findViewById(R.id.home);
    mShare = (Button) findViewById(R.id.share);

    if (!Config.isLogged(this)) {
      initNotLoggedArea(getIntent());
    } else {
      initLoggedArea(getIntent());
    }

    mLogin.setOnClickListener(this);
    mShop.setOnClickListener(this);
    mRank.setOnClickListener(this);
    mHome.setOnClickListener(this);
    mShare.setOnClickListener(this);

    Typeface typeface = Typeface.createFromAsset(getAssets(),
                SouShangApplication.FONT);
    mEventScoreNoLogged.setTypeface(typeface);
    mEventScoreTextNoLogged.setTypeface(typeface);
    mLogin.setTypeface(typeface);
    mEventScoreLogged.setTypeface(typeface);
    mEventScoreTextLogged.setTypeface(typeface);
    mTotalScore.setTypeface(typeface);
    mTotalScoreText.setTypeface(typeface);
    mShare.setTypeface(typeface);
    mShop.setTypeface(typeface);
    mRank.setTypeface(typeface);
    mHome.setTypeface(typeface);

    mMainHandler = new Handler();

    String shareEnabled = MobclickAgent.getConfigParams(this, "share_enabled");
    if ("true".equalsIgnoreCase(shareEnabled)) {
      mShare.setVisibility(View.VISIBLE);
    } else {
      mShare.setVisibility(View.GONE);
    }
    
    super.onCreate(arg0);
  }

  private void initNotLoggedArea(Intent intent) {
    mNotLoggedArea.setVisibility(View.VISIBLE);
    mLoggedArea.setVisibility(View.GONE);

    mEventPoint = intent.getIntExtra(Intents.EXTRA_POINT, 0);

    mEventScoreNoLogged.setText(String.format(
                getResources().getString(R.string.event_score_no_logged),
                mEventPoint));
  }

  private void initLoggedArea(Intent intent) {
    mNotLoggedArea.setVisibility(View.GONE);
    mLoggedArea.setVisibility(View.VISIBLE);

    if (intent != null) {
      mEventPoint = intent.getIntExtra(Intents.EXTRA_POINT, 0);

    }
    mEventScoreLogged.setText("" + mEventPoint);

    Apis.getUserInfo(this, Config.getAccessToken(this), mUserInfoCallback);
  }

  private void updateTotalScore(int point) {
    mTotalScore.setText("" + point);
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
      mBaidu = new Baidu(SouShangApplication.APP_KEY,
                    SouShangApplication.APP_SECRET, EventCompletedActivity.this);
      mBaidu.authorize(EventCompletedActivity.this,
                    new BaiduDialogListener() {

                      @Override
                      public void onError(BaiduDialogError arg0) {}

                      @Override
                      public void onComplete(Bundle arg0) {
                        Log.i("access_token", mBaidu.getAccessToken());
                        Config.setAccessToken(EventCompletedActivity.this,
                                    mBaidu.getAccessToken());
                        Apis.Login(EventCompletedActivity.this,
                                    mBaidu.getAccessToken(), mLoginCallback);
                      }

                      @Override
                      public void onCancel() {

                      }

                      @Override
                      public void onBaiduException(BaiduException arg0) {}
                    });
    } else if (v == mShop) {
      Intent intent = new Intent(EventCompletedActivity.this,
                    ShopActivity.class);
      startActivity(intent);

      finish();
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    } else if (v == mRank) {
      Intent intent = new Intent(EventCompletedActivity.this,
                    RankActivity.class);
      startActivity(intent);

      finish();
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    } else if (v == mHome) {
      Intent intent = new Intent(EventCompletedActivity.this,
                    HomeActivity.class);
      startActivity(intent);

      finish();
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    } else if (v == mShare) {
      mShareDialog = new CommonShareDialog(this, getResources().getString(R.string.share_content), null, getResources().getString(R.string.share_add_5_point));
      mShareDialog.setOnShareListener(new CommonShareDialog.OnShareListener() {

        @Override
        public void onShared() {
          Apis.share(EventCompletedActivity.this, Config.getAccessToken(EventCompletedActivity.this), new ApiResponseCallback<CommonResponse>() {
            
            @Override
            public void onResults(CommonResponse arg0) {
              Apis.getUserInfo(EventCompletedActivity.this, Config.getAccessToken(EventCompletedActivity.this), mUserInfoCallback);
            }
            
            @Override
            public void onError(Throwable arg0) {
            }
          });
        }

        @Override
        public void onFailed() {
        }
        
      });
      mShareDialog.create().show();
    }
    
  }

  @Override
  protected void onNewIntent(Intent intent) {
    if (!Config.isLogged(this)) {
      initNotLoggedArea(intent);
    } else {
      initLoggedArea(intent);
    }
    super.onNewIntent(intent);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (mShareDialog != null) {
      mShareDialog.onActivityResult(requestCode, resultCode, data);
    }
  }

}
