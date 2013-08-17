package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import com.baidu.soushang.Config;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
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

import android.content.Context;
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
  private TextView mTitle;
  private TextView mInterral;
  private TextView mDaliyPrize;
  private TextView mFeaturePrize;
  private GridView mGrid;
  private TextView mNoGifts;
  private LoadingView mLoading;
  private ShopInfokAdapter mAdapter;
  private ShopDialog mShopInfoDialog;
  private TipsDialog mTipsDialog;
  private Handler mMainHandler;
  private ApiResponseCallback<ShopInfoResponse> mShopInfoCallback =
      new ApiResponseCallback<ShopInfoResponse>() {
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
    mTitle = (TextView) findViewById(R.id.gift_interal_title);
    mInterral = (TextView) findViewById(R.id.gift_interal_content);
    mTitle.setTypeface(mTypeface);
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
      mTitle.setVisibility(View.VISIBLE);
      mInterral.setVisibility(View.VISIBLE);
      mInterral.setText(mApplication.getUser().getPoint() + "");
    } else {
      mTitle.setVisibility(View.GONE);
      mInterral.setVisibility(View.GONE);
    }

    mDaliyPrize = (TextView) findViewById(R.id.shop_daliy_prize);
    mDaliyPrize.setTypeface(mTypeface);
    mDaliyPrize.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mDaliyPrize.setBackgroundResource(R.drawable.gift_cg_pressed);
        mDaliyPrize.setTextColor(getResources().getColor(
            R.color.light_green));
        mFeaturePrize.setBackgroundResource(R.drawable.gift_zt_normal);
        mFeaturePrize.setTextColor(getResources().getColor(
            R.color.dark_green));
        SouShangApplication.CATID = "1";
        showLoading();
        Apis.getShopInfo(ShopActivity.this, SouShangApplication.CATID,
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
        mFeaturePrize.setBackgroundResource(R.drawable.gift_zt_pressed);
        mFeaturePrize.setTextColor(getResources().getColor(
            R.color.light_green));
        SouShangApplication.CATID = "2";
        showLoading();
        Apis.getShopInfo(ShopActivity.this, SouShangApplication.CATID,
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
    SouShangApplication.CATID = "1";
    mAdapter = new ShopInfokAdapter(ShopActivity.this);
    mGrid.setAdapter(mAdapter);
    showLoading();
    Apis.getShopInfo(ShopActivity.this, SouShangApplication.CATID, mShopInfoCallback);
  }

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
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
    private ShopInfo mShopInfo;
    private LayoutInflater mInflater;
    private List<String> mUrlList = null;
    private String mUrl = null;
    private static final String BASEURL = "http://sou.baidu.com";

    public List<ShopInfo> getData() {
      return mData;
    }

    public void setData(List<ShopInfo> data) {
      mData.clear();
      if (data != null) {
        mData.addAll(data);
      }
      mUrlList = new ArrayList<String>();
      for (int i = 0; i < mData.size(); i++) {
        mShopInfo = new ShopInfo();
        mShopInfo = mData.get(i);
        mUrl = mShopInfo.getImage();
        mUrl.replace("\\", "");
        mUrlList.add(mUrl);
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
      } catch (IndexOutOfBoundsException e) {}
      return shopInfo;
    }

    @Override
    public long getItemId(int position) {
      return Long.parseLong(mData.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder = null;
      if (convertView == null) {
        viewHolder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.shop_item, parent,
            false);
        viewHolder.mShopImag = (ImageView) convertView
            .findViewById(R.id.shop_item_imag);
        viewHolder.mShopName = (TextView) convertView
            .findViewById(R.id.shop_item_name);
        viewHolder.mShopMarkTitle = (TextView) convertView
            .findViewById(R.id.shop_item_marktitle);
        viewHolder.mShopMarkNum = (TextView) convertView
            .findViewById(R.id.shop_item_marknum);
        viewHolder.mShopExchange = (Button) convertView
            .findViewById(R.id.shop_item_exchange);
        viewHolder.mShopName.setTypeface(mTypeface);
        viewHolder.mShopMarkTitle.setTypeface(mTypeface);
        viewHolder.mShopMarkNum.setTypeface(mTypeface);
        viewHolder.mShopExchange.setTypeface(mTypeface);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      final ShopInfo shopInfo = (ShopInfo) getItem(position);
      String endUrl = BASEURL + mUrlList.get(position);
      ImageLoader imageLoader = ImageLoader.getInstance();
      imageLoader.displayImage(endUrl, viewHolder.mShopImag);
      viewHolder.mShopName.setText(shopInfo.getTitle());
      viewHolder.mShopMarkNum.setText("" + shopInfo.getIntegral());
      viewHolder.mShopExchange
          .setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              // TODO Auto-generated method stub
              if (Config.isLogged(ShopActivity.this)) {
                int mPoint = mApplication.getUser().getPoint();
                SouShangApplication.CurrentShopInfo = shopInfo;
                int nPoint = Integer.parseInt(shopInfo
                    .getIntegral());
                if (mPoint < nPoint) {
                  mTipsDialog.show(getResources().getString(
                      R.string.shop_dialog_tips));
                } else {
                  mShopInfoDialog = new ShopDialog(ShopActivity.this);
                  mShopInfoDialog.show();
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
      public ImageView mShopImag;
      public TextView mShopName;
      public TextView mShopMarkTitle;
      public TextView mShopMarkNum;
      public Button mShopExchange;
    }
  }
}
