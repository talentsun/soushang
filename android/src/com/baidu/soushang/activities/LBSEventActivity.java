package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.baidu.soushang.R;
import com.baidu.soushang.lbs.Models.User;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LBSEventActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.lbs_event);
    
    super.onCreate(arg0);
  }

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
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
  
  public class LBSAdapter extends BaseAdapter {
    private List<User> mData;
    private LayoutInflater mInflater;
    
    public LBSAdapter(Context context) {
      super();
      mInflater = LayoutInflater.from(context);
      mData = new ArrayList<User>();
    }

    public void setData(List<User> data) {
      mData.clear();
      if (data != null) {
        mData.addAll(data);
      }
    }
    
    public void clear() {
      mData.clear();
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
        
        convertView = (View) mInflater.inflate(R.layout.peer, null);
        viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
        viewHolder.username = (TextView) convertView.findViewById(R.id.username);
        viewHolder.network = (TextView) convertView.findViewById(R.id.network);
        viewHolder.eventCount = (TextView) convertView.findViewById(R.id.event_count);
        viewHolder.winRate = (TextView) convertView.findViewById(R.id.win_rate);
        viewHolder.fight = (Button) convertView.findViewById(R.id.fight);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      
      User peer = (User) getItem(position);
      
      return convertView;
    }
    
    public class ViewHolder {
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
