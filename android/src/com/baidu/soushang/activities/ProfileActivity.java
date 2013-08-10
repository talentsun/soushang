package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.soushang.Config;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.adapter.ProfileGiftAdapter;
import com.baidu.soushang.cloudapis.Gift;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
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
	private GridView mGift_grid;
	private TextView tipsMsg;
	private ProfileGiftAdapter pGiftAdapter;
	private int h;
	private Timer timer;
	private List<Gift> list = null;

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
		mGift_grid = (GridView) findViewById(R.id.gift_grid);
		tipsMsg = (TextView) findViewById(R.id.gift_tips_msg);

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
		tipsMsg.setTypeface(tf);

		mApplication = (SouShangApplication) getApplication();

		final Handler myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					if (mEventInfo.getHeight() != 0) {

						h = mEventInfo.getHeight();
						LayoutParams mLayoutParams = (LayoutParams) mGiftInfo
								.getLayoutParams();
						mLayoutParams.height = h;
						mGiftInfo.setLayoutParams(mLayoutParams);

						timer.cancel();

					}
				}
			}
		};

		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = 1;
				myHandler.sendMessage(message);
			}
		};
		timer.schedule(task, 10, 10);

		mEventInfoTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mEventInfo.setVisibility(View.VISIBLE);
				mEventInfoTab.setBackgroundResource(R.drawable.profile_gift);
				mGiftInfoTab
						.setBackgroundResource(R.drawable.profile_tab_unselected);
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

				if (list.size() != 0) {
					mGift_grid.setVisibility(View.VISIBLE);
					tipsMsg.setVisibility(View.GONE);
					pGiftAdapter = new ProfileGiftAdapter(mApplication,
							ProfileActivity.this, list);
					mGift_grid.setAdapter(pGiftAdapter);

				} else {

					mGift_grid.setVisibility(View.GONE);
					tipsMsg.setVisibility(View.VISIBLE);
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

			// 获取礼品信息
			list = new ArrayList<Gift>();
			list = mApplication.getUser().getGifts();

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
