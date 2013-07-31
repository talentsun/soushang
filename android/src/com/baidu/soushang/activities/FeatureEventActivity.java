package com.baidu.soushang.activities;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.Variables;
import com.baidu.soushang.bean.FeatureEventBean;
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
	private List<FeatureEventBean> list = new ArrayList<FeatureEventBean>();
	private static final String FEATURE_EVENT_URL = "http://soushang.limijiaoyin.com/index.php/Devent/getRooms.html";

	private FeatureDialog fDialog;

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

		fDialog = new FeatureDialog(this);

		showLoading();

		ThreadForFeature tFeature = new ThreadForFeature();
		tFeature.execute("");

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Variables.feBean = list.get(position);
				fDialog.show();
			}
		});
		super.onCreate(arg0);

	}

	class ThreadForFeature extends
			AsyncTask<String, String, List<FeatureEventBean>> {

		public ThreadForFeature() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected List<FeatureEventBean> doInBackground(String... params) {
			// TODO Auto-generated method stub
			list = JsonTool.getFeatureData(FEATURE_EVENT_URL);
			return list;
		}

		@Override
		protected void onPostExecute(List<FeatureEventBean> result) {
			// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		if (Variables.homeFlag == 0) {
			finish();
		}
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (Variables.homeFlag == 0) {
			finish();
		}
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

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
		private List<FeatureEventBean> mData;
		private LayoutInflater mInflater;

		public List<FeatureEventBean> getData() {
			return mData;
		}

		public void setData(List<FeatureEventBean> data) {
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
			mData = new ArrayList<FeatureEventBean>();
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			FeatureEventBean sBean = null;
			try {
				sBean = mData.get(position);
			} catch (IndexOutOfBoundsException e) {
			}
			return sBean;
		}

		@Override
		public long getItemId(int position) {
			FeatureEventBean sBean = mData.get(position);
			if (sBean == null) {
				return -1L;
			} else {
				return Long.parseLong(sBean.getId() + "");
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

			FeatureEventBean sBean = mData.get(position);
			viewHolder.nameView.setText(sBean.getTitle());
			boolean isruning = sBean.isRunning();

			if (isruning) {
				viewHolder.backView
						.setBackgroundResource(R.drawable.feature_run);
			} else {
				viewHolder.backView
						.setBackgroundResource(R.drawable.feature_start);
			}

			viewHolder.nameView.setText(sBean.getTitle());
			viewHolder.countView.setText(getResources().getString(
					R.string.feature_count)
					+ sBean.getPnum());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date startDate = new Date(Long.parseLong(sBean.getStartTime() + ""));
			Date endDate = new Date(Long.parseLong(sBean.getEndTime() + ""));
			String startTime = sdf.format(startDate);
			String end_Time = sdf.format(endDate);
			viewHolder.dateView.setText(startTime + "¡ª¡ª" + end_Time);

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
