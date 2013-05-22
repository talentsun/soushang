package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.R.id;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopActivity extends BaseActivity implements OnClickListener {
  public class Gift {
    private int mImage;
    private String mName;
    private int mPointNeeded;
    
    public int getImage() {
      return mImage;
    }
    
    public void setImage(int image) {
      mImage = image;
    }
    
    public String getName() {
      return mName;
    }
    
    public void setName(String name) {
      this.mName = name;
    }
    
    public int getPointNeeded() {
      return mPointNeeded;
    }
    
    public void setPointNeeded(int pointNeeded) {
      this.mPointNeeded = pointNeeded;
    }
    
    public Gift(int image, String name, int pointNeeded) {
      mImage = image;
      mName = name;
      mPointNeeded = pointNeeded;
    }
  }
  public class GiftAdapter extends PagerAdapter {
    private List<Gift> mGifts;
    private View[] mGiftViews;
    private LayoutInflater mInflater;

    public GiftAdapter(Context context) {
      super();
      
      mInflater = LayoutInflater.from(context);
      
      mGiftViews = new View[4];
      
      mGifts = new ArrayList<ShopActivity.Gift>();
      mGifts.add(new Gift(R.drawable.gift_phone, getResources().getString(R.string.gift_name_1), 40000));
      mGifts.add(new Gift(R.drawable.gift_phone, getResources().getString(R.string.gift_name_2), 50000));
      mGifts.add(new Gift(R.drawable.gift_phone, getResources().getString(R.string.gift_name_3), 60000));
      mGifts.add(new Gift(R.drawable.gift_phone, getResources().getString(R.string.gift_name_4), 70000));
    }

    @Override
    public int getCount() {
      return mGifts.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == (arg1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      Gift gift = mGifts.get(position);
      
      View view = mInflater.inflate(R.layout.gift, null);
      ImageView giftImage = (ImageView) view.findViewById(R.id.gift_image);
      TextView giftName = (TextView) view.findViewById(R.id.gift_name);
      TextView point = (TextView) view.findViewById(R.id.point);
      
      giftName.setTypeface(mTypeface);
      point.setTypeface(mTypeface);
      
      giftImage.setImageResource(gift.getImage());
      giftName.setText(gift.getName());
      point.setText("" + gift.getPointNeeded());
      
      mGiftViews[position] = view;
      container.addView(view, 0);
      return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      Log.i("destory", "" + position);
      container.removeView(mGiftViews[position]);
      mGiftViews[position] = null;
    }
    
  }
  
  private TextView mGoShopWeb;
  private TextView mShopUrl;
  private Button mLeft;
  private Button mRight;
  private ViewPager mGifts;
  private GiftAdapter mAdapter;

  private Typeface mTypeface;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.shop);
    
    mGoShopWeb = (TextView) findViewById(R.id.go_shop);
    mShopUrl = (TextView) findViewById(R.id.shop_url);
    mLeft = (Button) findViewById(R.id.left);
    mRight = (Button) findViewById(R.id.right);
    mGifts = (ViewPager) findViewById(R.id.gifts);
    
    mTypeface = Typeface.createFromAsset(getAssets(), SouShangApplication.FONT);
    mGoShopWeb.setTypeface(mTypeface);
    mShopUrl.setTypeface(mTypeface);
    
    mLeft.setOnClickListener(this);
    mRight.setOnClickListener(this);
    
    mAdapter = new GiftAdapter(this);
    mGifts.setAdapter(mAdapter);
    mGifts.setCurrentItem(0);
  }

  @Override
  public void onClick(View v) {
    if (v == mLeft) {
      if (mGifts.getCurrentItem() > 0) {
        mGifts.setCurrentItem(mGifts.getCurrentItem()-1);
      }
    } else if (v == mRight) {
      if (mGifts.getCurrentItem() < mAdapter.getCount()-1) {
        mGifts.setCurrentItem(mGifts.getCurrentItem()+1);
      }
    }
  }
  
}
