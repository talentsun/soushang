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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ClipData.Item;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
  
  public class PeersUpdatedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      mAdapter.setData(mApplication.getPeers());
      showNoPeers();
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
    
    Typeface typeface = Typeface.createFromAsset(getAssets(), SouShangApplication.FONT);
    mNoPeers.setTypeface(typeface);
    
    mAdapter = new LBSAdapter(this);
    mListView.setEmptyView(findViewById(android.R.id.empty));
    mListView.setAdapter(mAdapter);
    
    mPeersUpdatedReceiver = new PeersUpdatedReceiver();

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
    super.onDestroy();
  }

  @Override
  protected void onStart() {
    IntentFilter filter = new IntentFilter(Intents.ACTION_PEERS_UPDATED);
    registerReceiver(mPeersUpdatedReceiver, filter);
    
    showLoading();
    
    Intent intent = new Intent(this, LBSService.class);
    intent.setAction(Intents.ACTION_UPDATE_PEERS);
    mUpdatePeers = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    mAlarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 10 * 1000, mUpdatePeers);
    
    super.onStart();
  }

  @Override
  protected void onStop() {
    unregisterReceiver(mPeersUpdatedReceiver);
    mAlarmManager.cancel(mUpdatePeers);
    
    super.onStop();
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
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), SouShangApplication.FONT);
        
        convertView = (View) mInflater.inflate(R.layout.peer, null);
        viewHolder.bg = (LinearLayout) convertView.findViewById(R.id.bg);
        viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
        viewHolder.username = (TextView) convertView.findViewById(R.id.username);
        viewHolder.network = (TextView) convertView.findViewById(R.id.network);
        viewHolder.eventCount = (TextView) convertView.findViewById(R.id.event_count);
        viewHolder.winRate = (TextView) convertView.findViewById(R.id.win_rate);
        viewHolder.fight = (Button) convertView.findViewById(R.id.fight);
        
        viewHolder.username.setTypeface(typeface);
        viewHolder.network.setTypeface(typeface);
        viewHolder.eventCount.setTypeface(typeface);
        viewHolder.winRate.setTypeface(typeface);
        
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      
      User peer = (User) getItem(position);
      
      DisplayImageOptions option = new DisplayImageOptions.Builder()
        .showImageOnFail(R.drawable.default_avatar)
        .showImageForEmptyUri(R.drawable.default_avatar)
        .showStubImage(R.drawable.default_avatar)
        .displayer(new RoundedBitmapDisplayer(mContext.getResources().getDimensionPixelSize(R.dimen.avatar_width) / 2))
        .build();
      ImageLoader.getInstance().displayImage(peer.getAvatar(), viewHolder.avatar, option);
      viewHolder.username.setText(peer.getName());
      viewHolder.network.setText(NetworkUtils.getNetworkStr(peer.getNetType()));
      viewHolder.eventCount.setText(String.format(mContext.getResources().getString(R.string.event_count), peer.getFightNum()));
      viewHolder.winRate.setText(String.format(mContext.getResources().getString(R.string.win_rate), (int) (peer.getWinNum() * 100.0 / (float) peer.getFightNum())));
      
      if (getCount() == 1) {
        viewHolder.bg.setBackgroundResource(R.drawable.lbs_event_item_bg_single);
      } else {
        if (position == 0) {
          viewHolder.bg.setBackgroundResource(R.drawable.lbs_event_item_bg_first);
        } else if (position == mData.size() - 1) {
          viewHolder.bg.setBackgroundResource(R.drawable.lbs_event_item_bg_last);
        } else {
          viewHolder.bg.setBackgroundResource(R.drawable.lbs_event_item_bg_other);
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