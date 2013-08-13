package com.baidu.soushang.activities;

import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LBSEventCompletedActivity extends BaseActivity implements
    OnClickListener {
  private RelativeLayout mWaiting;
  private TextView mLBSEventCompleted;
  private TextView mWaitSomebody;
  private TextView mCurrentEventScore;
  private TextView mCurrentEventScoreValue;

  private LinearLayout mResult;
  private ImageView mResultImage;
  private TextView mResultTitle1;
  private TextView mResultTitle2;
  private TextView mMe;
  private TextView mMyCurrentEventPoint;
  private TextView mMyCurrentEventTime;
  private TextView mOther;
  private TextView mOtherCurrentEventPoint;
  private TextView mOtherCurrentEventTime;
  private TextView mEventPoint;
  private TextView mWinRate;
  private LinearLayout mEventScore;
  private Button mContinueEvent;
  private Button mBackHome;

  private SouShangApplication mApplication;

  public class FightEndReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent != null) {
        String action = intent.getAction();
        if (Intents.ACTION_FIGHT_END.equalsIgnoreCase(action)) {
          showResult(
              intent.getBooleanExtra(Intents.EXTRA_WIN, false),
              intent.getIntExtra(Intents.EXTRA_MY_POINT, 0),
              intent.getIntExtra(Intents.EXTRA_MY_TIME, 0),
              intent.getIntExtra(Intents.EXTRA_OTHER_POINT, 0),
              intent.getIntExtra(Intents.EXTRA_OTHER_TIME, 0),
              intent.getIntExtra(Intents.EXTRA_MY_POINT_DELTA, 0),
              intent.getIntExtra(Intents.EXTRA_MY_WIN_RATE, 0));
        }
      }
    }

  }

  private FightEndReceiver mReceiver;

  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.lbs_event_completed);

    mWaiting = (RelativeLayout) findViewById(R.id.waiting);
    mLBSEventCompleted = (TextView) findViewById(R.id.lbs_event_completed);
    mWaitSomebody = (TextView) findViewById(R.id.wait_somebody);
    mCurrentEventScore = (TextView) findViewById(R.id.current_event_score);
    mCurrentEventScoreValue = (TextView) findViewById(R.id.current_event_score_value);

    mResult = (LinearLayout) findViewById(R.id.result);
    mResultImage = (ImageView) findViewById(R.id.result_image);
    mResultTitle1 = (TextView) findViewById(R.id.result_title1);
    mResultTitle2 = (TextView) findViewById(R.id.result_title2);
    mMe = (TextView) findViewById(R.id.me);
    mMyCurrentEventPoint = (TextView) findViewById(R.id.my_current_event_point);
    mMyCurrentEventTime = (TextView) findViewById(R.id.my_current_event_time);
    mOther = (TextView) findViewById(R.id.other);
    mOtherCurrentEventPoint = (TextView) findViewById(R.id.other_current_event_point);
    mOtherCurrentEventTime = (TextView) findViewById(R.id.other_current_event_time);
    mEventPoint = (TextView) findViewById(R.id.event_point);
    mWinRate = (TextView) findViewById(R.id.win_rate);
    mEventScore = (LinearLayout) findViewById(R.id.event_score);
    mContinueEvent = (Button) findViewById(R.id.continue_event);
    mBackHome = (Button) findViewById(R.id.back_home);

    Typeface typeface = Typeface.createFromAsset(getAssets(),
        SouShangApplication.FONT);
    mLBSEventCompleted.setTypeface(typeface);
    mWaitSomebody.setTypeface(typeface);
    mCurrentEventScore.setTypeface(typeface);
    mCurrentEventScoreValue.setTypeface(typeface);
    mResultTitle1.setTypeface(typeface);
    mResultTitle2.setTypeface(typeface);
    mMe.setTypeface(typeface);
    mMyCurrentEventPoint.setTypeface(typeface);
    mMyCurrentEventTime.setTypeface(typeface);
    mOther.setTypeface(typeface);
    mOtherCurrentEventPoint.setTypeface(typeface);
    mOtherCurrentEventTime.setTypeface(typeface);
    mEventPoint.setTypeface(typeface);
    mWinRate.setTypeface(typeface);
    mContinueEvent.setTypeface(typeface);
    mBackHome.setTypeface(typeface);

    mContinueEvent.setOnClickListener(this);
    mBackHome.setOnClickListener(this);

    mApplication = (SouShangApplication) getApplication();

    Intent intent = getIntent();
    if (intent != null) {
      String action = intent.getAction();
      if (Intents.ACTION_LBS_WAIT.equalsIgnoreCase(action)) {
        showWaiting(intent.getIntExtra(Intents.EXTRA_MY_POINT, 0),
            intent.getIntExtra(Intents.EXTRA_MY_TIME, 0));
      } else if (Intents.ACTION_LBS_RESULT.equalsIgnoreCase(action)) {
        showResult(intent.getBooleanExtra(Intents.EXTRA_WIN, true),
            intent.getIntExtra(Intents.EXTRA_MY_POINT, 0),
            intent.getIntExtra(Intents.EXTRA_MY_TIME, 0),
            intent.getIntExtra(Intents.EXTRA_OTHER_POINT, 0),
            intent.getIntExtra(Intents.EXTRA_OTHER_TIME, 0),
            intent.getIntExtra(Intents.EXTRA_MY_POINT_DELTA, 0),
            intent.getIntExtra(Intents.EXTRA_MY_WIN_RATE, 0));
      } else {
        showWaiting(0, 0);
      }
    } else {
      showWaiting(0, 0);
    }

    IntentFilter filter = new IntentFilter(Intents.ACTION_FIGHT_END);
    mReceiver = new FightEndReceiver();
    registerReceiver(mReceiver, filter);

    super.onCreate(arg0);
  }

  private void showWaiting(int point, int time) {
    mResult.setVisibility(View.GONE);
    mWaiting.setVisibility(View.VISIBLE);

    mWaitSomebody.setText(String.format(getString(R.string.wait_somebody),
        mApplication.getCurrentPeer().getName()));
    mCurrentEventScoreValue.setText(String.format(
        getString(R.string.current_event_score_value), point, time));
  }

  private void showResult(boolean win, int myPoint, int myTime,
      int otherPoint, int otherTime, int winPoint, int winRate) {
    mResult.setVisibility(View.VISIBLE);
    mWaiting.setVisibility(View.GONE);

    if (win) {
      mResultImage.setImageResource(R.drawable.lbs_event_win);
      mEventScore.setBackgroundResource(R.drawable.lbs_event_win_score);
      mResultTitle1.setText(getString(R.string.congratulation));
      mResultTitle2.setText(getString(R.string.win_current_event));
      mEventPoint.setText(String.format(getString(R.string.get_point),
          Math.abs(winPoint)));
    } else {
      mResultImage.setImageResource(R.drawable.lbs_event_lose);
      mEventScore.setBackgroundResource(R.drawable.lbs_event_lose_score);
      mResultTitle1.setText(getString(R.string.regret));
      mResultTitle2.setText(getString(R.string.lose_current_event));
      mEventPoint.setText(String.format(getString(R.string.drop_point),
          winPoint));
    }

    mMyCurrentEventPoint.setText(String.format(
        getString(R.string.current_event_point), myPoint));
    mMyCurrentEventTime.setText(String.format(
        getString(R.string.current_event_time), myTime));
    mOtherCurrentEventPoint.setText(String.format(
        getString(R.string.current_event_point), otherPoint));
    mOtherCurrentEventTime.setText(String.format(
        getString(R.string.current_event_time), otherTime));
    mWinRate.setText(String.format(getString(R.string.win_rate),
        Math.abs(winRate)));

    mApplication.setCurrentPeer(null);
    mApplication.updateUserExtraInfo();
  }

  @Override
  protected void onDestroy() {
    unregisterReceiver(mReceiver);
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
    if (v == mBackHome) {
      Intent intent = new Intent(LBSEventCompletedActivity.this,
          HomeActivity.class);
      startActivity(intent);
    } else {
      Intent intent = new Intent(LBSEventCompletedActivity.this,
          LBSEventActivity.class);
      startActivity(intent);
    }

    finish();
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }

}
