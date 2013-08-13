package com.baidu.soushang.activities;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.cloudapis.FeatureEvent;
import com.baidu.soushang.utils.JsonTool;
import com.baidu.soushang.views.LoadingView;
import com.baidu.soushang.widgets.FeatureDialog;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FeatureEventActivity extends BaseActivity {

  private ListView mListView;
  private TextView mNoEvent;
  private LoadingView mLoading;
  private Typeface mTypeface;

  private EventAdapter mAdapter;
  private List<FeatureEvent> list = new ArrayList<FeatureEvent>();
  private List<FeatureEvent> li = new ArrayList<FeatureEvent>();
  private static final String FEATURE_EVENT_URL =
      "http://soushang.limijiaoyin.com/index.php/Devent/getRooms.html?access_token=%s";

  private FeatureDialog mFeatureDialog;

  @Override
  protected void onCreate(Bundle arg0) {
    // TODO Auto-generated method stub
    setContentView(R.layout.feature_event);

    mListView = (ListView) findViewById(R.id.list);
    mTypeface = Typeface.createFromAsset(getAssets(),
        SouShangApplication.FONT);
    mListView.setEmptyView(findViewById(android.R.id.empty));
    mLoading = (LoadingView) findViewById(R.id.loading);
    mNoEvent = (TextView) findViewById(R.id.no_event);
    mNoEvent.setTypeface(mTypeface);

    mFeatureDialog = new FeatureDialog(this);
    mFeatureDialog.setOnClickListener(new FeatureDialog.OnClickListener() {

      @Override
      public void onResume() {
      }

      @Override
      public void onHome() {
        finish();
      }
    });
    showLoading();

    ThreadForFeature tFeature = new ThreadForFeature();
    tFeature.execute("");

    mListView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view,
          int position, long id) {
        SouShangApplication.CurrentFeatureEvent = li.get(position);
        mFeatureDialog.show();
      }
    });

    super.onCreate(arg0);

  }

  class ThreadForFeature extends
      AsyncTask<String, String, List<FeatureEvent>> {

    public ThreadForFeature() {
    }

    @Override
    protected List<FeatureEvent> doInBackground(String... params) {
      list = JsonTool.getFeatureData(FEATURE_EVENT_URL,
          FeatureEventActivity.this);

      if (list != null) {

        for (int i = 0; i < list.size(); i++) {

          if (list.get(i).isRunning()) {
            li.add(list.get(i));
            list.remove(i);
            i--;
          }

        }
        li.addAll(list);
      }

      return li;
    }

    @Override
    protected void onPostExecute(List<FeatureEvent> result) {
      showNoEvent();
      mAdapter = new EventAdapter(FeatureEventActivity.this);
      if (result != null) {
        mAdapter.setData(result);
      }

      mListView.setAdapter(mAdapter);
      super.onPostExecute(result);
    }
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
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  private void showLoading() {
    mNoEvent.setVisibility(View.GONE);
    mLoading.show();
  }

  private void showNoEvent() {
    mNoEvent.setVisibility(View.VISIBLE);
    mLoading.hide();
  }

  public class EventAdapter extends BaseAdapter {
    private List<FeatureEvent> mData;
    private LayoutInflater mInflater;

    public List<FeatureEvent> getData() {
      return mData;
    }

    public void setData(List<FeatureEvent> data) {
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

    public EventAdapter(Context context) {
      super();
      mData = new ArrayList<FeatureEvent>();
      mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
      return mData.size();
    }

    @Override
    public Object getItem(int position) {
      FeatureEvent featureEvent = null;
      try {
        featureEvent = mData.get(position);
      } catch (IndexOutOfBoundsException e) {}
      return featureEvent;
    }

    @Override
    public long getItemId(int position) {
      FeatureEvent featureEvent = mData.get(position);
      if (featureEvent == null) {
        return -1L;
      } else {
        return Long.parseLong(featureEvent.getId() + "");
      }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder = null;
      if (convertView == null) {
        viewHolder = new ViewHolder();

        convertView = mInflater.inflate(R.layout.feature_event_item,
            parent, false);

        viewHolder.backView = (LinearLayout) convertView
            .findViewById(R.id.feature_back);
        viewHolder.nameView = (TextView) convertView
            .findViewById(R.id.name);
        viewHolder.countView = (TextView) convertView
            .findViewById(R.id.competitor_count);
        viewHolder.dateView = (TextView) convertView
            .findViewById(R.id.date);
        viewHolder.nameView.setTypeface(mTypeface);
        viewHolder.countView.setTypeface(mTypeface);
        viewHolder.dateView.setTypeface(mTypeface);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      FeatureEvent featureEvent = mData.get(position);
      viewHolder.nameView.setText(featureEvent.getTitle());
      boolean isruning = featureEvent.isRunning();

      if (isruning) {
        viewHolder.backView
            .setBackgroundResource(R.drawable.feature_run);
      } else {
        viewHolder.backView
            .setBackgroundResource(R.drawable.feature_start);
      }

      viewHolder.nameView.setText(featureEvent.getTitle());
      viewHolder.countView.setText(getResources().getString(
          R.string.feature_count)
          + featureEvent.getPnum());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      Date startDate = new Date(Long.parseLong((long) featureEvent.getStartTime() * 1000 + ""));
      Date endDate = new Date(Long.parseLong((long) featureEvent.getEndTime() * 1000 + ""));
      String startTime = sdf.format(startDate);
      String end_Time = sdf.format(endDate);
      viewHolder.dateView.setText(startTime + "¡ª" + end_Time);

      return convertView;
    }

    class ViewHolder {
      LinearLayout backView;
      public TextView nameView;
      public TextView countView;
      public TextView dateView;
    }

  }

}
