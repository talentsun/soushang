package com.baidu.soushang.activities;

import java.io.IOException;

import org.json.JSONObject;

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

import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.AsyncBaiduRunner.RequestListener;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.SouShangApplication.LoginListener;
import com.baidu.soushang.SouShangApplication.UpdateUserInfoListener;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.CommonResponse;
import com.baidu.soushang.cloudapis.User;
import com.baidu.soushang.cloudapis.UserInfoResponse;
import com.baidu.soushang.lbs.LBSService;
import com.baidu.soushang.utils.SystemUtils;
import com.limijiaoyin.socialsdk.dialogs.CommonShareDialog;
import com.umeng.analytics.MobclickAgent;

public class DailyEventCompletedActivity extends BaseActivity implements
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
  private Handler mMainHandler;
  private int mEventPoint;
  private SouShangApplication mApplication;

  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.daily_event_completed);

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

    mApplication = (SouShangApplication) getApplication();
    mApplication.setUpdateUserInfoListener(new UpdateUserInfoListener() {
      
      @Override
      public void onUpdated(User user) {
        final int point = user.getPoint();

        mMainHandler.post(new Runnable() {

          @Override
          public void run() {
            updateTotalScore(point);
          }
        });
      }
      
      @Override
      public void onError() {
        mMainHandler.post(new Runnable() {

          @Override
          public void run() {
            updateTotalScore(0);
          }
        });
      }
    });
    
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
    if (mApplication.getUser() != null) {
      updateTotalScore(mApplication.getUser().getPoint());
    } else {
      updateTotalScore(0);
    }
    
    mApplication.updateUserExtraInfo();
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
    mApplication.setLoginListener(new LoginListener() {
      
      @Override
      public void onSuccess() {
        mLogin.post(new Runnable() {
          
          @Override
          public void run() {
            Apis.answer(DailyEventCompletedActivity.this,
              mApplication.getAnswers(),
              Config.getAccessToken(DailyEventCompletedActivity.this),
              null);
            initLoggedArea(null);
          }
        });
      }
      
      @Override
      public void onFail() {
        mLogin.post(new Runnable() {
          
          @Override
          public void run() {
            Toast.makeText(DailyEventCompletedActivity.this,
              getResources().getString(R.string.login_failed),
              Toast.LENGTH_SHORT).show();
          }
        });
      }
      
      @Override
      public void onError() {
        mLogin.post(new Runnable() {
          
          @Override
          public void run() {
            Toast.makeText(DailyEventCompletedActivity.this,
              getResources().getString(R.string.login_failed),
              Toast.LENGTH_SHORT).show();
          }
        });
      }
    });

    super.onStart();
  }

  @Override
  protected void onStop() {
    mApplication.setLoginListener(null);
    super.onStop();
  }

  @Override
  public void onClick(View v) {
    if (v == mLogin) {
      mApplication.login(DailyEventCompletedActivity.this);
    } else if (v == mShop) {
      Intent intent = new Intent(DailyEventCompletedActivity.this,
                    ShopActivity.class);
      startActivity(intent);

      finish();
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    } else if (v == mRank) {
      Intent intent = new Intent(DailyEventCompletedActivity.this,
                    RankActivity.class);
      startActivity(intent);

      finish();
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    } else if (v == mHome) {
      Intent intent = new Intent(DailyEventCompletedActivity.this,
                    HomeActivity.class);
      startActivity(intent);

      finish();
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    } else if (v == mShare) {
      mShareDialog = new CommonShareDialog(this, getResources().getString(R.string.share_content), null, getResources().getString(R.string.share_add_5_point));
      mShareDialog.setOnShareListener(new CommonShareDialog.OnShareListener() {

        @Override
        public void onShared() {
          Apis.share(DailyEventCompletedActivity.this, Config.getAccessToken(DailyEventCompletedActivity.this), new ApiResponseCallback<CommonResponse>() {
            
            @Override
            public void onResults(CommonResponse arg0) {
              mApplication.updateUserExtraInfo();
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
