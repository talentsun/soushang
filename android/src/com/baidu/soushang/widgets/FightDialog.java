package com.baidu.soushang.widgets;

import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.lbs.LBSService;
import com.baidu.soushang.lbs.Models.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FightDialog extends Dialog implements View.OnClickListener {
  private RelativeLayout mFightReq;
  private TextView mFight;
  private TextView mSomebody;
  private TextView mRaise;
  private EditText mRaiseValue;
  private Button mReqFight;
  private Button mCancel;

  private RelativeLayout mFightResp;
  private TextView mReceiveFight;
  private TextView mSomebodyReceive;
  private ImageView mOtherAvatarResp;
  private TextView mOtherUserNameResp;
  private TextView mOtherEventCountResp;
  private TextView mOtherWinRateResp;
  private Button mAccept;
  private Button mReject;

  private RelativeLayout mFightWaiting;
  private TextView mWaiting;
  private TextView mSomebodyResp;
  private ImageView mMyAvatar;
  private TextView mMyUserName;
  private ImageView mOtherAvatar;
  private TextView mOtherUserName;
  private Button mCancelFight;
  private ImageView mConnecting;
  private ImageView mCountdown;
  private DisplayImageOptions mOption;

  private SouShangApplication mApplication;
  private int mMaxBet = 0;

  public class FightRespReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent != null) {
        String action = intent.getAction();
        if (Intents.ACTION_FIGHT_RESP.equalsIgnoreCase(action)) {
          int result = intent.getIntExtra(Intents.EXTRA_FIGHT_RESULT, 1);
          if (result > 0) {
            if (result == 2) {
              Toast.makeText(
                getContext(),
                String.format(getContext().getString(R.string.bet_more_than_five), mApplication
                    .getCurrentPeer().getName()), Toast.LENGTH_LONG).show();
            } else {
              Toast.makeText(
                getContext(),
                String.format(getContext().getString(R.string.be_rejected), mApplication
                    .getCurrentPeer().getName()), Toast.LENGTH_LONG).show();
            }
            
            mApplication.setCurrentPeer(null);
            cancel();
          }
        } else if (Intents.ACTION_FIGHT_CANCEL.equalsIgnoreCase(action)) {
          Toast.makeText(
              getContext(),
              String.format(getContext().getString(R.string.be_cancelled), mApplication
                  .getCurrentPeer().getName()), Toast.LENGTH_LONG).show();
          mApplication.setCurrentPeer(null);
          cancel();
        } else if (Intents.ACTION_FIGHT_BEGIN.equalsIgnoreCase(action)) {
          mCancelFight.setEnabled(false);
          
          mCountdown.setVisibility(View.VISIBLE);
          AnimationDrawable countdown = (AnimationDrawable) mCountdown.getBackground();
          if (countdown.isRunning()) {
            countdown.stop();
          }
          countdown.start();

          final String fightKey =  intent.getStringExtra(Intents.EXTRA_FIGHT_KEY);
          mCountdown.postDelayed(new Runnable() {
            
            @Override
            public void run() {
              dismiss();
              if (mListener != null) {
                mListener.onFight(fightKey);
              }
            }
          }, 4000);
        }
      }
    }

  }

  private FightRespReceiver mFightRespReceiver;

  public interface Listener {
    public void onFight(String fightKey);
  }
  private Listener mListener;
  public void setListener(Listener listener) {
    mListener = listener;
  }
  
  public FightDialog(Context context) {
    this(context, R.style.PopupDialog);
  }

  public FightDialog(Context context, int theme) {
    super(context, theme);

    setContentView(R.layout.fight_dialog);

    mFightReq = (RelativeLayout) findViewById(R.id.fight_req);
    mFight = (TextView) findViewById(R.id.fight);
    mSomebody = (TextView) findViewById(R.id.somebody);
    mRaise = (TextView) findViewById(R.id.raise);
    mRaiseValue = (EditText) findViewById(R.id.raise_value);
    mReqFight = (Button) findViewById(R.id.req_fight);
    mCancel = (Button) findViewById(R.id.cancel);

    mFightResp = (RelativeLayout) findViewById(R.id.fight_resp);
    mReceiveFight = (TextView) findViewById(R.id.receive_fight);
    mSomebodyReceive = (TextView) findViewById(R.id.somebody_receive);
    mOtherAvatarResp = (ImageView) findViewById(R.id.other_avatar_resp);
    mOtherUserNameResp = (TextView) findViewById(R.id.other_resp);
    mOtherEventCountResp = (TextView) findViewById(R.id.other_event_count_resp);
    mOtherWinRateResp = (TextView) findViewById(R.id.other_win_rate_resp);
    mAccept = (Button) findViewById(R.id.accept);
    mReject = (Button) findViewById(R.id.reject);

    mFightWaiting = (RelativeLayout) findViewById(R.id.fight_waiting);
    mWaiting = (TextView) findViewById(R.id.waiting);
    mSomebodyResp = (TextView) findViewById(R.id.somebody_resp);
    mMyAvatar = (ImageView) findViewById(R.id.my_avatar);
    mMyUserName = (TextView) findViewById(R.id.me);
    mOtherAvatar = (ImageView) findViewById(R.id.other_avatar);
    mOtherUserName = (TextView) findViewById(R.id.other);
    mCancelFight = (Button) findViewById(R.id.cancel_fight);
    mConnecting = (ImageView) findViewById(R.id.connecting);
    mCountdown = (ImageView) findViewById(R.id.countdown);

    Typeface typeface = Typeface.createFromAsset(context.getAssets(), SouShangApplication.FONT);
    mFight.setTypeface(typeface);
    mSomebody.setTypeface(typeface);
    mRaise.setTypeface(typeface);
    mRaiseValue.setTypeface(typeface);
    mReqFight.setTypeface(typeface);
    mCancel.setTypeface(typeface);
    mWaiting.setTypeface(typeface);
    mSomebodyResp.setTypeface(typeface);
    mMyUserName.setTypeface(typeface);
    mOtherUserName.setTypeface(typeface);
    mCancelFight.setTypeface(typeface);
    mReceiveFight.setTypeface(typeface);
    mSomebodyReceive.setTypeface(typeface);
    mOtherUserNameResp.setTypeface(typeface);
    mOtherEventCountResp.setTypeface(typeface);
    mOtherWinRateResp.setTypeface(typeface);
    mAccept.setTypeface(typeface);
    mReject.setTypeface(typeface);

    mReqFight.setOnClickListener(this);
    mCancel.setOnClickListener(this);
    mCancelFight.setOnClickListener(this);
    mAccept.setOnClickListener(this);
    mReject.setOnClickListener(this);

    mApplication = (SouShangApplication) ((Activity) context).getApplication();

    mOption = new DisplayImageOptions.Builder()
          .showImageOnFail(R.drawable.default_avatar)
          .showImageForEmptyUri(R.drawable.default_avatar)
          .showStubImage(R.drawable.default_avatar)
          .displayer(
              new RoundedBitmapDisplayer(getContext().getResources().getDimensionPixelSize(
                  R.dimen.avatar_width) / 2))
          .build();
    mFightRespReceiver = new FightRespReceiver();
    
    setCanceledOnTouchOutside(false);
  }

  @Override
  protected void onStart() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intents.ACTION_FIGHT_RESP);
    filter.addAction(Intents.ACTION_FIGHT_CANCEL);
    filter.addAction(Intents.ACTION_FIGHT_BEGIN);
    getContext().registerReceiver(mFightRespReceiver, filter);

    super.onStart();
  }

  @Override
  protected void onStop() {
    getContext().unregisterReceiver(mFightRespReceiver);
    super.onStop();
  }

  public void show(boolean req, User peer, int bet) {
    mApplication.setCurrentPeer(peer);

    if (req) {
      mSomebody.setText(String.format(getContext().getResources().getString(R.string.somebody),
          mApplication.getCurrentPeer().getName()));

      if (mApplication.getUser() != null) {
        mMaxBet = mApplication.getUser().getPoint() / 10;
      } 
      
      mRaiseValue.setHint(String.format(getContext().getString(R.string.max_bet), mMaxBet));

      mFightReq.setVisibility(View.VISIBLE);
      mFightResp.setVisibility(View.GONE);
      mFightWaiting.setVisibility(View.GONE);
    } else {
      mSomebodyReceive.setText(String.format(
          getContext().getResources().getString(R.string.somebody_receive), mApplication
              .getCurrentPeer().getName(), bet));

      ImageLoader.getInstance().displayImage(mApplication.getCurrentPeer().getAvatar(),
          mOtherAvatarResp, mOption);
      mOtherUserNameResp.setText(mApplication.getCurrentPeer().getName());
      mOtherEventCountResp.setText(String.format(
          getContext().getResources().getString(R.string.event_count), mApplication
              .getCurrentPeer().getFightNum()));
      mOtherWinRateResp.setText(String.format(
          getContext().getResources().getString(R.string.win_rate), (int) (mApplication
              .getCurrentPeer().getWinNum() * 100.0 / (float) mApplication.getCurrentPeer()
              .getFightNum())));

      mFightReq.setVisibility(View.GONE);
      mFightResp.setVisibility(View.VISIBLE);
      mFightWaiting.setVisibility(View.GONE);
    }

    show();
  }

  @Override
  public void onClick(View v) {
    if (v == mReqFight) {
      int bet = 0;
      try {
        bet = Integer.parseInt(mRaiseValue.getText().toString());
      } catch (Exception e) {}
      
      if (bet > mMaxBet) {
        Toast.makeText(
          getContext(), getContext().getString(R.string.more_than_max_bet), Toast.LENGTH_LONG).show();
      } else {
        Intent intent = new Intent(getContext(), LBSService.class);
        intent.setAction(Intents.ACTION_FIGHT_REQ);
        intent.putExtra(Intents.EXTRA_BET, bet);
        getContext().startService(intent);

        showWaiting();
      }
    } else if (v == mCancel) {
      mApplication.setCurrentPeer(null);
      cancel();
    } else if (v == mCancelFight) {
      mApplication.setCurrentPeer(null);

      Intent intent = new Intent(getContext(), LBSService.class);
      intent.setAction(Intents.ACTION_FIGHT_CANCEL);
      getContext().startService(intent);

      cancel();
    } else if (v == mAccept) {
      Intent intent = new Intent(getContext(), LBSService.class);
      intent.setAction(Intents.ACTION_FIGHT_RESP);
      intent.putExtra(Intents.EXTRA_FIGHT_RESULT, 0);
      getContext().startService(intent);

      showWaiting();
    } else if (v == mReject) {
      mApplication.setCurrentPeer(null);

      Intent intent = new Intent(getContext(), LBSService.class);
      intent.setAction(Intents.ACTION_FIGHT_RESP);
      intent.putExtra(Intents.EXTRA_FIGHT_RESULT, 1);
      getContext().startService(intent);

      cancel();
    }
  }

  private void showWaiting() {
    mFightReq.setVisibility(View.GONE);
    mFightResp.setVisibility(View.GONE);
    mFightWaiting.setVisibility(View.VISIBLE);
    
    mCountdown.setVisibility(View.GONE);

    mSomebodyResp.setText(String.format(
        getContext().getResources().getString(R.string.somebody_resp), mApplication
            .getCurrentPeer().getName()));
    mOtherUserName.setText(mApplication.getCurrentPeer().getName());

    AnimationDrawable connecting = (AnimationDrawable) mConnecting.getBackground();
    if (!connecting.isRunning()) {
      connecting.start();
    }

    ImageLoader.getInstance().displayImage(Config.getAvatar(getContext()), mMyAvatar, mOption);
    ImageLoader.getInstance().displayImage(mApplication.getCurrentPeer().getAvatar(), mOtherAvatar,
        mOption);
  }

  @Override
  public void onBackPressed() {
  }

}
