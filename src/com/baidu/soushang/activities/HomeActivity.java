package com.baidu.soushang.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.CommonResponse;
import com.baidu.soushang.cloudapis.DayEventResponse;
import com.baidu.soushang.cloudapis.QuestionResponse;
import com.baidu.soushang.widgets.LoadingDialog;
import com.baidu.soushang.widgets.TipsDialog;
import com.baidu.soushang.widgets.WebViewDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends BaseActivity implements OnClickListener {
  private Button mSouShang;
  private Button mDailyEvent;
  private Button mRank;
  private Button mShop;
  
  private WebViewDialog mNewsDialog;
  private TipsDialog mTipsDialog;
  
  private ApiResponseCallback<DayEventResponse> mDayEventCallback = new ApiResponseCallback<DayEventResponse>() {
    
    @Override
    public void onResults(DayEventResponse arg0) {
      //FIXME no event 0:00
      if (arg0 != null && arg0.getRetCode() == 0) {
        if (arg0.getEventFinished() == 0) {
          Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);
          intent.putExtra(Intents.EXTRA_QUESTION_ID, arg0.getStartId());
          HomeActivity.this.startActivity(intent);
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
          mTipsDialog.show(getResources().getString(R.string.day_event_finished));
        }
        
      } else {
        mTipsDialog.show(getResources().getString(R.string.no_day_event));
      }
    }
    
    @Override
    public void onError(Throwable arg0) {
      Toast.makeText(HomeActivity.this, HomeActivity.this.getResources().getString(R.string.get_dayevent_failed), Toast.LENGTH_SHORT).show();
    }
  };
  
  private ApiResponseCallback<QuestionResponse> mNextQuestionCallback = new ApiResponseCallback<QuestionResponse>() {
    
    @Override
    public void onResults(QuestionResponse arg0) {
      if (arg0 != null && arg0.getRetCode() == 0 && arg0.getQuestion() != null) {
        Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);
        HomeActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
      } else {
        mTipsDialog.show(getResources().getString(R.string.no_day_event));
      }
    }
    
    @Override
    public void onError(Throwable arg0) {
      Toast.makeText(HomeActivity.this, HomeActivity.this.getResources().getString(R.string.get_dayevent_failed), Toast.LENGTH_SHORT).show();
    }
  };
  
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
    
    mNewsDialog = new WebViewDialog(this);
    mTipsDialog = new TipsDialog(this);
    
    String currentDate = getCurrentDate();
    if (!currentDate.equalsIgnoreCase(Config.getLatestNewsDate(this))) {
      Config.setLatestNewsDate(this, currentDate);
      mNewsDialog.show(getResources().getString(R.string.news), "http://sou.baidu.com/news/news.html");
    }
    
    if (Config.isLogged(this)) {
      Apis.Login(this, Config.getAccessToken(this), new ApiResponseCallback<CommonResponse>() {
        
        @Override
        public void onResults(CommonResponse arg0) {
          if (arg0 == null || arg0.getRetCode() != 0) {
            Config.removeAccessToken(HomeActivity.this);
            Config.setLogged(HomeActivity.this, false);
          }
        }
        
        @Override
        public void onError(Throwable arg0) {
        }
      });
    }
    
    super.onCreate(arg0);
    MobclickAgent.onError(this);
    MobclickAgent.updateOnlineConfig(this);
    UmengUpdateAgent.update(this);
  }

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
  }

  @Override
  public void onClick(View v) {
    if (v == mSouShang) {
      Intent intent = new Intent(this, SouShangActivity.class);
      startActivity(intent);
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    } else if (v == mDailyEvent) {
      if (Config.isLogged(this)) {
        Apis.getDayEvent(this, Config.getAccessToken(this), mDayEventCallback);
      } else {
        Apis.getNextQuestion(this, 0, null, mNextQuestionCallback);
      }
    } else if (v == mRank) {
      Intent intent = new Intent(this, RankActivity.class);
      startActivity(intent);
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    } else if (v == mShop) {
      Intent intent = new Intent(this, ShopActivity.class);
      startActivity(intent);
      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
  }
  
  private String getCurrentDate() {
    Date date = new Date();
    return new SimpleDateFormat("yyyyMMdd").format(date);
  }

}
