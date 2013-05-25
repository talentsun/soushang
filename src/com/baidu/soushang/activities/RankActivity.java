package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.User;
import com.baidu.soushang.cloudapis.UserRankResponse;
import com.baidu.soushang.views.LoadingView;

public class RankActivity extends BaseActivity {
  private ListView mListView;
  private RankAdapter mAdapter;
  private TextView mNoRank;
  private LoadingView mLoading;
  private Typeface mTypeface;
  
  private ApiResponseCallback<UserRankResponse> mUserRankCallback = new ApiResponseCallback<UserRankResponse>() {
    
    @Override
    public void onResults(UserRankResponse arg0) {
      showNoRank();
      if (arg0 != null && arg0.getRetCode() == 0 && arg0.getUsers() != null) {
        mAdapter.setData(arg0.getUsers());
      } else {
        mAdapter.clearData();
      }
    }
    
    @Override
    public void onError(Throwable arg0) {
      showNoRank();
      mAdapter.clearData();
    }
  };
  
  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.rank);
    
    mListView = (ListView) findViewById(R.id.list);
    mTypeface = Typeface.createFromAsset(getAssets(), SouShangApplication.FONT);
    View headerView = LayoutInflater.from(this).inflate(R.layout.rank_header, null);
    ((TextView) headerView.findViewById(R.id.ranking)).setTypeface(mTypeface);
    ((TextView) headerView.findViewById(R.id.user)).setTypeface(mTypeface);
    ((TextView) headerView.findViewById(R.id.credit)).setTypeface(mTypeface);
    mListView.addHeaderView(headerView);
    mListView.setEmptyView(findViewById(android.R.id.empty));
    mAdapter = new RankAdapter(this);
    mListView.setAdapter(mAdapter);
    mLoading = (LoadingView) findViewById(R.id.loading);
    
    mNoRank = (TextView) findViewById(R.id.norank);
    mNoRank.setTypeface(mTypeface);
    
    showLoading();
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
  
  private void showLoading() {
    mNoRank.setVisibility(View.GONE);
    mLoading.show();
  }
  
  private void showNoRank() {
    mNoRank.setVisibility(View.VISIBLE);
    mLoading.hide();
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
        viewHolder.numberView.setTypeface(mTypeface);
        viewHolder.usernameView.setTypeface(mTypeface);
        viewHolder.creditView.setTypeface(mTypeface);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      User user = (User) getItem(position);
      if (position == 0) {
        viewHolder.numberView.setText("");
        viewHolder.numberView.setBackgroundResource(R.drawable.rank_1);
      } else if (position == 1) {
        viewHolder.numberView.setText("");
        viewHolder.numberView.setBackgroundResource(R.drawable.rank_2);
      } else if (position == 2) {
        viewHolder.numberView.setText("");
        viewHolder.numberView.setBackgroundResource(R.drawable.rank_3);
      } else {
        viewHolder.numberView.setText("" + (position+1));
        viewHolder.numberView.setBackgroundResource(R.drawable.rank_other);
      }

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
