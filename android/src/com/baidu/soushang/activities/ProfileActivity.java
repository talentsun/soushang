package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.soushang.Config;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.cloudapis.Gift;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ProfileActivity extends BaseActivity {
  private SouShangApplication mApplication;
  private ImageView mAvatar;
  private TextView mUserName;
  private TextView mEventInfoTab;
  private TextView mGiftInfoTab;
  private LinearLayout mEventInfo;
  private TextView mDailyEvent;
  private TextView mLBSEvent;
  private TextView mIntegral;
  private TextView mPoint;
  private TextView mRank;
  private TextView mFightCount;
  private TextView mGameCount;
  private TextView mWinRate;
  private LinearLayout mGiftInfo;
  private GridView mGiftGrid;
  private TextView mTipsMsg;
  private ProfileGiftAdapter mGiftAdapter;
  private int mHeight;
  private Timer mTimer;
  private List<Gift> mList = null;
  private Button mLoginOut;

  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.profile);
    mAvatar = (ImageView) findViewById(R.id.avatar);
    mUserName = (TextView) findViewById(R.id.username);
    mEventInfoTab = (TextView) findViewById(R.id.event_info_tab);
    mGiftInfoTab = (TextView) findViewById(R.id.gift_info_tab);
    mEventInfo = (LinearLayout) findViewById(R.id.event_info);
    mDailyEvent = (TextView) findViewById(R.id.daily_event);
    mIntegral = (TextView) findViewById(R.id.integral);
    mPoint = (TextView) findViewById(R.id.point);
    mRank = (TextView) findViewById(R.id.rank);
    mLBSEvent = (TextView) findViewById(R.id.lbs_event);
    mFightCount = (TextView) findViewById(R.id.fight_count);
    mGameCount = (TextView) findViewById(R.id.game_count);
    mWinRate = (TextView) findViewById(R.id.win_rate);
    mGiftInfo = (LinearLayout) findViewById(R.id.gift_info);
    mGiftGrid = (GridView) findViewById(R.id.gift_grid);
    mTipsMsg = (TextView) findViewById(R.id.gift_tips_msg);
    Typeface tf = Typeface.createFromAsset(getAssets(),
        SouShangApplication.FONT);
    mUserName.setTypeface(tf);
    mEventInfoTab.setTypeface(tf);
    mGiftInfoTab.setTypeface(tf);
    mDailyEvent.setTypeface(tf);
    mIntegral.setTypeface(tf);
    mPoint.setTypeface(tf);
    mRank.setTypeface(tf);
    mLBSEvent.setTypeface(tf);
    mFightCount.setTypeface(tf);
    mGameCount.setTypeface(tf);
    mWinRate.setTypeface(tf);
    mTipsMsg.setTypeface(tf);
    mApplication = (SouShangApplication) getApplication();
    final Handler mHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        if (msg.what == 1) {
          if (mEventInfo.getHeight() != 0) {
            mHeight = mEventInfo.getHeight();
            LayoutParams mLayoutParams = (LayoutParams) mGiftInfo
                .getLayoutParams();
            mLayoutParams.height = mHeight;
            mGiftInfo.setLayoutParams(mLayoutParams);
            mTimer.cancel();
          }
        }
      }
    };

    mTimer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        // TODO Auto-generated method stub
        Message message = new Message();
        message.what = 1;
        mHandler.sendMessage(message);
      }
    };
    mTimer.schedule(task, 10, 10);
    mEventInfoTab.setTextColor(getResources().getColor(
        R.color.profile_tab_selected));
    mEventInfoTab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mEventInfo.setVisibility(View.VISIBLE);
        mEventInfoTab.setBackgroundResource(R.drawable.profile_gift);
        mGiftInfoTab
            .setBackgroundResource(R.drawable.profile_tab_unselected);
        mEventInfoTab.setTextColor(getResources().getColor(
            R.color.profile_tab_selected));
        mGiftInfoTab.setTextColor(getResources().getColor(
            R.color.profile_tab_nomal));
        mGiftInfo.setVisibility(View.GONE);
      }
    });

    mGiftInfoTab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mEventInfo.setVisibility(View.GONE);
        mGiftInfo.setVisibility(View.VISIBLE);
        mEventInfoTab
            .setBackgroundResource(R.drawable.profile_tab_unselected);
        mGiftInfoTab.setBackgroundResource(R.drawable.profile_gift);
        mEventInfoTab.setTextColor(getResources().getColor(
            R.color.profile_tab_nomal));
        mGiftInfoTab.setTextColor(getResources().getColor(
            R.color.profile_tab_selected));
        if (mList != null && mList.size() != 0) {
          mGiftGrid.setVisibility(View.VISIBLE);
          mTipsMsg.setVisibility(View.GONE);
          mGiftAdapter = new ProfileGiftAdapter(mApplication,
              ProfileActivity.this, mList);
          mGiftGrid.setAdapter(mGiftAdapter);
        } else {
          mGiftGrid.setVisibility(View.GONE);
          mTipsMsg.setVisibility(View.VISIBLE);
        }
      }
    });
    mEventInfoTab.setBackgroundResource(R.drawable.profile_gift);
    if (mApplication.getUser() != null) {
      ImageLoader.getInstance().displayImage(Config.getAvatar(this),
          mAvatar, mApplication.getAvatarDisplayOption());
      mUserName.setText(Config.getUserName(this));
      mIntegral.setText(String.format(getString(R.string.integral),
          mApplication.getUser().getIntegral()));
      mPoint.setText(String.format(getString(R.string.point),
          mApplication.getUser().getPoint()));
      mRank.setText(String.format(getString(R.string.rank), mApplication
          .getUser().getUserRank()));
      mFightCount.setText(String.format(getString(R.string.fight_count),
          mApplication.getUser().getFightNum()));
      mGameCount.setText(String.format(getString(R.string.game_count),
          mApplication.getUser().getWinNum()));
      mWinRate.setText(String.format(getString(R.string.win_rate),
          mApplication.getUser().getWinRatio()));
      mList = new ArrayList<Gift>();
      mList = mApplication.getUser().getGifts();
    }
    mLoginOut = (Button) findViewById(R.id.login_out);
    mLoginOut.setTypeface(tf);
    mLoginOut.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (Config.isLogged(ProfileActivity.this)) {
          mApplication.logout(ProfileActivity.this);
          Config.setAccessToken(ProfileActivity.this, null);
          Config.setAvatar(ProfileActivity.this, null);
          Config.setLogged(ProfileActivity.this, false);
          Config.setUserName(ProfileActivity.this, null);
          Config.setLatestNewsDate(ProfileActivity.this, null);
        }
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
      }
    });
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

  class ProfileGiftAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private SouShangApplication mApplication;
    private List<Gift> mList = new ArrayList<Gift>();
    private Gift mGift;
    private List<String> mUrlList = null;
    private String mUrl = null;
    private static final String BASEURL = "http://sou.baidu.com";

    public ProfileGiftAdapter(SouShangApplication mApplication,
        Context context, List<Gift> list) {
      this.mApplication = mApplication;
      this.mContext = context;
      this.mList = list;
      mLayoutInflater = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      mUrlList = new ArrayList<String>();
      for (int i = 0; i < list.size(); i++) {
        mGift = new Gift();
        mGift = list.get(i);
        mUrl = mGift.getThumb();
        mUrl.replace("\\", "");
        mUrlList.add(mUrl);
      }
    }

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return mList.size();
    }

    @Override
    public Object getItem(int position) {
      // TODO Auto-generated method stub
      return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      MyLayout mlayout;
      if (convertView == null || convertView.getTag() == null) {
        convertView = mLayoutInflater.inflate(R.layout.profile_gift_ada,
            null);
        mlayout = new MyLayout();
        mlayout.mGiftImag = (ImageView) convertView
            .findViewById(R.id.gift_imag);
        mlayout.mGiftName = (TextView) convertView
            .findViewById(R.id.gift_name);
        mlayout.mGiftIntegral = (TextView) convertView
            .findViewById(R.id.gift_integral);
        convertView.setTag(mlayout);
      } else {
        mlayout = (MyLayout) convertView.getTag();
      }
      mGift = mList.get(position);
      String endUrl = BASEURL + mUrlList.get(position);
      mlayout.mGiftImag.setBackgroundResource(R.drawable.self_gift_stroke);
      ImageLoader imageLoader = ImageLoader.getInstance();
      imageLoader.displayImage(endUrl, mlayout.mGiftImag,
          mApplication.getAvatarDisplayOption());
      Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
          SouShangApplication.FONT);
      mlayout.mGiftName.setTypeface(tf);
      mlayout.mGiftIntegral.setTypeface(tf);
      mlayout.mGiftName.setText(mGift.getTitle());
      mlayout.mGiftIntegral.setText(mGift.getNums());
      return convertView;
    }

    class MyLayout {
      ImageView mGiftImag;
      TextView mGiftName;
      TextView mGiftIntegral;
    }
  }
  
  
  
}
