package com.baidu.soushang.activities;

import com.baidu.soushang.Config;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ProfileActivity extends BaseActivity {
  private SouShangApplication mApplication;

  private ImageView mAvatar;
  private TextView mUserName;
  private RadioButton mEventInfoTab;
  private RadioButton mGiftInfoTab;
  
  private LinearLayout mEventInfo;
  private TextView mDailyEvent;
  private TextView mLBSEvent;
  private TextView mIntegral;
  private TextView mPoint;
  private TextView mRank;
  private TextView mFightCount;
  private TextView mBeFightCount;
  private TextView mWinRate;
  
  private LinearLayout mGiftInfo;
  
  @Override
  protected void onCreate(Bundle arg0) {
    
    setContentView(R.layout.profile);
    
    mAvatar = (ImageView) findViewById(R.id.avatar);
    mUserName = (TextView) findViewById(R.id.username);
    mEventInfoTab = (RadioButton) findViewById(R.id.event_info_tab);
    mGiftInfoTab = (RadioButton) findViewById(R.id.gift_info_tab);

    mEventInfo = (LinearLayout) findViewById(R.id.event_info);
    mDailyEvent = (TextView) findViewById(R.id.daily_event);
    mIntegral = (TextView) findViewById(R.id.integral);
    mPoint = (TextView) findViewById(R.id.point);
    mRank = (TextView) findViewById(R.id.rank);
    mLBSEvent = (TextView) findViewById(R.id.lbs_event);
    mFightCount = (TextView) findViewById(R.id.fight_count);
    mBeFightCount = (TextView) findViewById(R.id.be_fight_count);
    mWinRate = (TextView) findViewById(R.id.win_rate);
    
    mGiftInfo = (LinearLayout) findViewById(R.id.gift_info);
    
    Typeface tf = Typeface.createFromAsset(getAssets(), SouShangApplication.FONT);
    mUserName.setTypeface(tf);
    mEventInfoTab.setTypeface(tf);
    mGiftInfoTab.setTypeface(tf);
    mDailyEvent.setTypeface(tf);
    mIntegral.setTypeface(tf);
    mPoint.setTypeface(tf);
    mRank.setTypeface(tf);
    mLBSEvent.setTypeface(tf);
    mFightCount.setTypeface(tf);
    mBeFightCount.setTypeface(tf);
    mWinRate.setTypeface(tf);
    
    mApplication = (SouShangApplication) getApplication();
    
    mEventInfoTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          mEventInfo.setVisibility(View.VISIBLE);
          mGiftInfo.setVisibility(View.GONE);
        }
      }
    });
    
    mGiftInfoTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          mEventInfo.setVisibility(View.GONE);
          mGiftInfo.setVisibility(View.VISIBLE);
        }
      }
    });
    
    mEventInfoTab.setChecked(true);
    
    if (mApplication.getUser() != null) {
      ImageLoader.getInstance().displayImage(Config.getAvatar(this), mAvatar, mApplication.getAvatarDisplayOption());
      mUserName.setText(Config.getUserName(this));
      
      mIntegral.setText(String.format(getString(R.string.integral), mApplication.getUser().getIntegral()));
      mPoint.setText(String.format(getString(R.string.point), mApplication.getUser().getPoint()));
    }
    
    super.onCreate(arg0);
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

}
