package com.baidu.soushang.adapter;

import java.util.ArrayList;
import java.util.List;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.cloudapis.Gift;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileGiftAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private SouShangApplication mApplication;
	private List<Gift> list = new ArrayList<Gift>();
	private Gift gift;
	private List<String> urlList = null;
	private String url = null;
	private static final String BASEURL = "http://soushang.limijiaoyin.com";

	public ProfileGiftAdapter(SouShangApplication mApplication,
			Context context, List<Gift> list2) {
		this.mApplication = mApplication;
		this.context = context;
		this.list = list2;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		urlList = new ArrayList<String>();
		for (int i = 0; i < list2.size(); i++) {
			gift = new Gift();
			gift = list2.get(i);
			url = gift.getThumb();
			url.replace("\\", "");
			urlList.add(url);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyLayout mlayout;
		if (convertView == null || convertView.getTag() == null) {
			convertView = layoutInflater.inflate(R.layout.profile_gift_ada,
					null);
			mlayout = new MyLayout();
			mlayout.gift_imag = (ImageView) convertView
					.findViewById(R.id.gift_imag);
			mlayout.gift_name = (TextView) convertView
					.findViewById(R.id.gift_name);
			mlayout.gift_integral = (TextView) convertView
					.findViewById(R.id.gift_integral);

			convertView.setTag(mlayout);
		} else {
			mlayout = (MyLayout) convertView.getTag();
		}

		gift = list.get(position);

		String endUrl = BASEURL + urlList.get(position);
		mlayout.gift_imag.setBackgroundResource(R.drawable.self_gift_stroke);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(endUrl, mlayout.gift_imag,
				mApplication.getAvatarDisplayOption());

		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				SouShangApplication.FONT);
		mlayout.gift_name.setTypeface(tf);
		mlayout.gift_integral.setTypeface(tf);
		mlayout.gift_name.setText(gift.getTitle());
		mlayout.gift_integral.setText(gift.getNums());
		return convertView;
	}

	static class MyLayout {
		ImageView gift_imag;
		TextView gift_name;
		TextView gift_integral;
	}
}
