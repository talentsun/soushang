package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.lbs.LBSService;
import com.baidu.soushang.lbs.Models.User;
import com.baidu.soushang.utils.NetworkUtils;
import com.baidu.soushang.views.LoadingView;
import com.baidu.soushang.widgets.FightDialog;
import com.baidu.soushang.widgets.FightDialog.Listener;
import com.baidu.soushang.widgets.LBSFirstDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class LBSEventActivity extends BaseActivity {
	private ListView mListView;
	private LBSAdapter mAdapter;
	private LoadingView mLoading;
	private TextView mNoPeers;

	private SouShangApplication mApplication;
	private PendingIntent mUpdatePeers;
	private AlarmManager mAlarmManager;
	private FightDialog mFightDialog;

	private LBSFirstDialog lDialog;
	private SharedPreferences sp;
	private SharedPreferences.Editor et;

	public class PeersUpdatedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String action = intent.getAction();
				if (Intents.ACTION_PEERS_UPDATED.equalsIgnoreCase(action)) {
					mAdapter.setData(mApplication.getPeers());
					showNoPeers();
				} else if (Intents.ACTION_FIGHT_REQ.equalsIgnoreCase(action)) {
					mFightDialog.show(false, mApplication.getCurrentPeer(),
							intent.getIntExtra(Intents.EXTRA_BET, 0));
				}
			}
		}

	}

	private PeersUpdatedReceiver mPeersUpdatedReceiver;

	@Override
	protected void onCreate(Bundle arg0) {

		setContentView(R.layout.lbs_event);

		mApplication = (SouShangApplication) getApplication();

		mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		mListView = (ListView) findViewById(R.id.peers);
		mLoading = (LoadingView) findViewById(R.id.loading);
		mNoPeers = (TextView) findViewById(R.id.no_peers);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				SouShangApplication.FONT);
		mNoPeers.setTypeface(typeface);

		mAdapter = new LBSAdapter(this);
		mListView.setEmptyView(findViewById(android.R.id.empty));
		mListView.setAdapter(mAdapter);

		mPeersUpdatedReceiver = new PeersUpdatedReceiver();

		mFightDialog = new FightDialog(this);
		mFightDialog.setListener(new Listener() {

			@Override
			public void onFight(String fightKey) {
				Intent questionIntent = new Intent(LBSEventActivity.this,
						QuestionActivity.class);
				questionIntent.putExtra(Intents.EXTRA_EVENT_TYPE,
						Intents.EVENT_TYPE_LBS);
				questionIntent.putExtra(Intents.EXTRA_FIGHT_KEY, fightKey);
				LBSEventActivity.this.startActivity(questionIntent);

				finish();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			}
		});

		lDialog = new LBSFirstDialog(this);
		sp = getSharedPreferences(Intents.EXTRA_LBS_DIALOG_JUDGE,
				Activity.MODE_PRIVATE);
		et = sp.edit();
		int lbs = sp.getInt("lbs", -1);
		if (lbs == -1) {
			lDialog.show();
		}
		super.onCreate(arg0);
	}

	private void showLoading() {
		mLoading.show();
		mNoPeers.setVisibility(View.GONE);
	}

	private void showNoPeers() {
		mLoading.hide();
		mNoPeers.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		et.putInt("lbs", 0);
		et.commit();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intents.ACTION_PEERS_UPDATED);
		filter.addAction(Intents.ACTION_FIGHT_REQ);
		registerReceiver(mPeersUpdatedReceiver, filter);

		showLoading();

		Intent intent = new Intent(this, LBSService.class);
		intent.setAction(Intents.ACTION_UPDATE_PEERS);
		mUpdatePeers = PendingIntent.getService(this, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mAlarmManager.setRepeating(AlarmManager.RTC,
				System.currentTimeMillis(), 10 * 1000, mUpdatePeers);

		super.onStart();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(mPeersUpdatedReceiver);
		mAlarmManager.cancel(mUpdatePeers);

		super.onStop();
	}

	@Override
	protected void onPause() {
		Intent intent = new Intent(this, LBSService.class);
		intent.setAction(Intents.ACTION_LBS_OFFLINE);
		startService(intent);
		super.onPause();
	}

	@Override
	protected void onResume() {
		Intent intent = new Intent(this, LBSService.class);
		intent.setAction(Intents.ACTION_LBS_ONLINE);
		startService(intent);
		super.onResume();
	}

	public class LBSAdapter extends BaseAdapter {
		private List<User> mData;
		private LayoutInflater mInflater;
		private Context mContext;

		public LBSAdapter(Context context) {
			super();
			mContext = context;
			mInflater = LayoutInflater.from(context);
			mData = new ArrayList<User>();
		}

		public void setData(List<User> data) {
			mData.clear();
			if (data != null) {
				mData.addAll(data);
			}
			notifyDataSetChanged();
		}

		public void clear() {
			mData.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mData.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				Typeface typeface = Typeface.createFromAsset(
						mContext.getAssets(), SouShangApplication.FONT);

				convertView = (View) mInflater.inflate(R.layout.peer, null);
				viewHolder.bg = (LinearLayout) convertView
						.findViewById(R.id.bg);
				viewHolder.avatar = (ImageView) convertView
						.findViewById(R.id.avatar);
				viewHolder.username = (TextView) convertView
						.findViewById(R.id.username);
				viewHolder.network = (TextView) convertView
						.findViewById(R.id.network);
				viewHolder.eventCount = (TextView) convertView
						.findViewById(R.id.event_count);
				viewHolder.winRate = (TextView) convertView
						.findViewById(R.id.win_rate);
				viewHolder.fight = (Button) convertView
						.findViewById(R.id.fight);

				viewHolder.username.setTypeface(typeface);
				viewHolder.network.setTypeface(typeface);
				viewHolder.eventCount.setTypeface(typeface);
				viewHolder.winRate.setTypeface(typeface);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final User peer = (User) getItem(position);

			DisplayImageOptions option = new DisplayImageOptions.Builder()
					.showImageOnFail(R.drawable.default_avatar)
					.showImageForEmptyUri(R.drawable.default_avatar)
					.showStubImage(R.drawable.default_avatar)
					.displayer(
							new RoundedBitmapDisplayer(
									mContext.getResources()
											.getDimensionPixelSize(
													R.dimen.avatar_width) / 2))
					.build();
			ImageLoader.getInstance().displayImage(peer.getAvatar(),
					viewHolder.avatar, option);
			viewHolder.username.setText(peer.getName());
			viewHolder.network.setText(NetworkUtils.getNetworkStr(peer
					.getNetType()));
			viewHolder.eventCount.setText(String.format(mContext.getResources()
					.getString(R.string.event_count), peer.getFightNum()));
			viewHolder.winRate.setText(String.format(mContext.getResources()
					.getString(R.string.win_rate),
					(int) (peer.getWinNum() * 100.0 / (float) peer
							.getFightNum())));
			viewHolder.fight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mFightDialog.show(true, peer, 0);
				}
			});

			if (getCount() == 1) {
				viewHolder.bg
						.setBackgroundResource(R.drawable.lbs_event_item_bg_single);
			} else {
				if (position == 0) {
					viewHolder.bg
							.setBackgroundResource(R.drawable.lbs_event_item_bg_first);
				} else if (position == mData.size() - 1) {
					viewHolder.bg
							.setBackgroundResource(R.drawable.lbs_event_item_bg_last);
				} else {
					viewHolder.bg
							.setBackgroundResource(R.drawable.lbs_event_item_bg_other);
				}
			}

			return convertView;
		}

		public class ViewHolder {
			public LinearLayout bg;
			public ImageView avatar;
			public TextView username;
			public TextView network;
			public TextView eventCount;
			public TextView winRate;
			public Button fight;

			public ViewHolder() {

			}
		}

	}
}
