package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import com.baidu.soushang.Config;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.Variables;
import com.baidu.soushang.SouShangApplication.UpdateUserInfoListener;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.ShopInfo;
import com.baidu.soushang.cloudapis.ShopInfoResponse;
import com.baidu.soushang.cloudapis.User;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.views.LoadingView;
import com.baidu.soushang.widgets.ShopDialog;
import com.baidu.soushang.widgets.TipsDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopActivity extends BaseActivity {

	private SouShangApplication mApplication;
	private Typeface mTypeface;
	private TextView mInterral;
	private TextView mDaliyPrize;
	private TextView mFeaturePrize;

	private GridView mGrid;

	private TextView mNoGifts;
	private LoadingView mLoading;

	private ShopInfokAdapter mAdapter;

	private ShopDialog sDialog;
	private TipsDialog mTipsDialog;
	private Handler mMainHandler;
	private ApiResponseCallback<ShopInfoResponse> mShopInfoCallback = new ApiResponseCallback<ShopInfoResponse>() {

		@Override
		public void onResults(ShopInfoResponse arg0) {
			showNoGifts();
			if (arg0 != null && arg0.getRetCode() == 0
					&& arg0.getGifts() != null) {

				mAdapter.setData(arg0.getGifts());
			} else {
				mAdapter.clearData();
			}
		}

		@Override
		public void onError(Throwable arg0) {
			showNoGifts();
			mAdapter.clearData();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.shop);

		mTipsDialog = new TipsDialog(this);

		mTypeface = Typeface.createFromAsset(getAssets(),
				SouShangApplication.FONT);
		mMainHandler = new Handler();

		mInterral = (TextView) findViewById(R.id.gift_interal_content);

		// String contet=mInterral.getText().toString();
		// String strs[]=contet.split("\n");

		mInterral.setTypeface(mTypeface);

		mApplication = (SouShangApplication) getApplication();
		mApplication.setUpdateUserInfoListener(new UpdateUserInfoListener() {

			@Override
			public void onUpdated(User user) {
				final int point = user.getPoint();
				// TODO Auto-generated method stub
				mMainHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mInterral.setText(point + "");
					}
				});

			}

			@Override
			public void onError() {
				// TODO Auto-generated method stub

			}
		});

		if (mApplication.getUser() != null) {
			mInterral.setText(mApplication.getUser().getPoint() + "");
		}
		mDaliyPrize = (TextView) findViewById(R.id.shop_daliy_prize);
		mDaliyPrize.setTypeface(mTypeface);

		mDaliyPrize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDaliyPrize.setBackgroundResource(R.drawable.gift_ch_selected);
				mDaliyPrize.setTextColor(getResources().getColor(
						R.color.light_green));
				mFeaturePrize.setBackgroundResource(R.drawable.gift_zt_normal);
				mFeaturePrize.setTextColor(getResources().getColor(
						R.color.dark_green));
				Variables.CATID = "1";
				showLoading();
				Apis.getShopInfo(ShopActivity.this, Variables.CATID,
						mShopInfoCallback);
				mAdapter = new ShopInfokAdapter(ShopActivity.this);
				mGrid.setAdapter(mAdapter);
			}
		});

		mFeaturePrize = (TextView) findViewById(R.id.shop_feature_prize);
		mFeaturePrize.setTypeface(mTypeface);
		mFeaturePrize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDaliyPrize.setBackgroundResource(R.drawable.gift_cg_normal);
				mDaliyPrize.setTextColor(getResources().getColor(
						R.color.dark_green));
				mFeaturePrize
						.setBackgroundResource(R.drawable.gift_zt_selected);
				mFeaturePrize.setTextColor(getResources().getColor(
						R.color.light_green));
				Variables.CATID = "2";
				showLoading();
				Apis.getShopInfo(ShopActivity.this, Variables.CATID,
						mShopInfoCallback);
				mAdapter = new ShopInfokAdapter(ShopActivity.this);
				mGrid.setAdapter(mAdapter);
			}
		});

		mGrid = (GridView) findViewById(R.id.shop_grid);
		mGrid.setEmptyView(findViewById(android.R.id.empty));

		mLoading = (LoadingView) findViewById(R.id.shop_loading);

		mNoGifts = (TextView) findViewById(R.id.shop_nogifts);
		mNoGifts.setTypeface(mTypeface);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Variables.CATID = "1";
		mAdapter = new ShopInfokAdapter(ShopActivity.this);
		mGrid.setAdapter(mAdapter);
		showLoading();
		System.out.println("at onCreate of ShopActivity Variables.CATID=="
				+ Variables.CATID);
		Apis.getShopInfo(ShopActivity.this, Variables.CATID, mShopInfoCallback);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("at onDestory of ShopActivity");
	}

	private void showLoading() {
		mNoGifts.setVisibility(View.GONE);
		mLoading.show();
	}

	private void showNoGifts() {
		mNoGifts.setVisibility(View.VISIBLE);
		mLoading.hide();

	}

	public class ShopInfokAdapter extends BaseAdapter {
		private List<ShopInfo> mData;
		private ShopInfo shopInfo;
		private LayoutInflater mInflater;
		private List<String> urlList = null;
		private String url = null;
		private static final String BASEURL = "http://soushang.limijiaoyin.com";

		public List<ShopInfo> getData() {
			return mData;
		}

		public void setData(List<ShopInfo> data) {
			mData.clear();

			if (data != null) {
				mData.addAll(data);
			}

			urlList = new ArrayList<String>();
			for (int i = 0; i < mData.size(); i++) {
				shopInfo = new ShopInfo();
				shopInfo = mData.get(i);
				url = shopInfo.getImage();
				url.replace("\\", "");
				urlList.add(url);
			}
			notifyDataSetChanged();
		}

		public void clearData() {
			mData.clear();
			notifyDataSetChanged();
		}

		public ShopInfokAdapter(Context context) {
			super();
			mData = new ArrayList<ShopInfo>();
			mInflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			ShopInfo shopInfo = null;
			try {
				shopInfo = mData.get(position);
			} catch (IndexOutOfBoundsException e) {
			}
			return shopInfo;
		}

		@Override
		public long getItemId(int position) {
			User user = (User) getItem(position);
			if (user == null) {
				return -1L;
			} else {
				return Long.parseLong(user.getUserId());
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.shop_item, parent,
						false);
				viewHolder.shop_item_imag = (ImageView) convertView
						.findViewById(R.id.shop_item_imag);
				viewHolder.shop_item_name = (TextView) convertView
						.findViewById(R.id.shop_item_name);
				viewHolder.shop_item_marknum = (TextView) convertView
						.findViewById(R.id.shop_item_marknum);
				viewHolder.shop_item_exchange = (Button) convertView
						.findViewById(R.id.shop_item_exchange);

				viewHolder.shop_item_name.setTypeface(mTypeface);
				viewHolder.shop_item_marknum.setTypeface(mTypeface);
				viewHolder.shop_item_exchange.setTypeface(mTypeface);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final ShopInfo shopInfo = (ShopInfo) getItem(position);

			String endUrl = BASEURL + urlList.get(position);
			// System.out.println("at getView endUrl of ShopInfokAdapter =="
			// + endUrl);
			ImageLoader imageLoader = ImageLoader.getInstance();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					ShopActivity.this).memoryCacheExtraOptions(304, 240)
					.discCacheExtraOptions(306, 240, CompressFormat.JPEG, 75)
					.build();
			imageLoader.init(config);
			imageLoader.displayImage(endUrl, viewHolder.shop_item_imag);

			viewHolder.shop_item_name.setText(shopInfo.getTitle());
			viewHolder.shop_item_marknum.setText("" + shopInfo.getIntegral());
			viewHolder.shop_item_exchange
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							// TODO Auto-generated method stub
							if (Config.isLogged(ShopActivity.this)) {

								int mPoint = mApplication.getUser().getPoint();

								Variables.shBean = shopInfo;
								int nPoint = Integer.parseInt(shopInfo
										.getIntegral());
								if (mPoint < nPoint) {
									mTipsDialog.show(getResources().getString(
											R.string.shop_dialog_tips));
								} else {

									sDialog = new ShopDialog(ShopActivity.this);
									sDialog.show();
								}

							} else {
								mTipsDialog.show(getResources().getString(
										R.string.shop_event_need_logged));
							}

						}
					});

			return convertView;
		}

		class ViewHolder {
			public ImageView shop_item_imag;
			public TextView shop_item_name;
			public TextView shop_item_marknum;
			public Button shop_item_exchange;
		}

	}
}
