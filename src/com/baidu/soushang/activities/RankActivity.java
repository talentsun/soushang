package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.soushang.R;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.User;
import com.baidu.soushang.cloudapis.UserRankResponse;

public class RankActivity extends FragmentActivity {
  private ListView mListView;
  private RankAdapter mAdapter;
  
  private ApiResponseCallback<UserRankResponse> mUserRankCallback = new ApiResponseCallback<UserRankResponse>() {
    
    @Override
    public void onResults(UserRankResponse arg0) {
      if (arg0 != null && arg0.getRetCode() == 0 && arg0.getUsers() != null) {
        mAdapter.setData(arg0.getUsers());
      } else {
        mAdapter.clearData();
      }
    }
    
    @Override
    public void onError(Throwable arg0) {
      mAdapter.clearData();
    }
  };
  
  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.rank);
    
    mListView = (ListView) findViewById(R.id.list);
    mAdapter = new RankAdapter(this);
    mListView.setAdapter(mAdapter);
    
    Apis.getUserRank(this, mUserRankCallback);
    
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

  public class RankAdapter extends BaseAdapter {
    private List<User> mData;
    private LayoutInflater mInflater;
    
    public List<User> getData() {
      return mData;
    }
    
    public void setData(List<User> data) {
      mData.clear();
      
      if (data != null) {
        mData.addAll(data);
      }
      
      notifyDataSetChanged();
    }
    
    public void clearData() {
      mData.clear();
      notifyDataSetChanged();
    }
    
    public RankAdapter(Context context) {
      super();
      mData = new ArrayList<User>();
      mInflater = LayoutInflater.from(context);
    }
    
    @Override
    public int getCount() {
      return mData.size();
    }

    @Override
    public Object getItem(int position) {
      User user = null;
      try {
        user = mData.get(position);
      } catch (IndexOutOfBoundsException e) {
      }
      return user;
    }

    @Override
    public long getItemId(int position) {
      User user = (User) getItem(position);
      if (user == null) {
        return -1L;
      } else {
        return user.getUserId();
      }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder = null;
      if (convertView == null) {
        viewHolder = new ViewHolder();

        convertView = mInflater.inflate(R.layout.rank_item, parent, false);
        viewHolder.numberView = (TextView) convertView.findViewById(R.id.number);
        viewHolder.usernameView = (TextView) convertView.findViewById(R.id.username);
        viewHolder.creditView = (TextView) convertView.findViewById(R.id.credit);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      User user = (User) getItem(position);
      viewHolder.numberView.setText("" + (position+1));
      viewHolder.usernameView.setText(user.getUsername());
      viewHolder.creditView.setText("" + user.getIntegral());

      return convertView;
    }
    
    class ViewHolder {
      public TextView numberView;
      public TextView usernameView;
      public TextView creditView;
    }
    
  }
}
