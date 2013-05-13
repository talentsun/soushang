package com.baidu.soushang.activities;

import com.baidu.soushang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionActivity extends FragmentActivity {
	private TextView mQuestionTitle;
	private ImageView mQuestionImage;

	@Override
	protected void onCreate(Bundle arg0) {
		
		setContentView(R.layout.question);
		
		mQuestionTitle = (TextView) findViewById(R.id.title);
		
		final String imageUrl = "http://c.hiphotos.baidu.com/album/w%3D2048/sign=094761a737d12f2ece05a9607bfad462/d009b3de9c82d158be1459c1810a19d8bc3e422f.jpg";
		mQuestionImage = (ImageView) findViewById(R.id.image);
		mQuestionImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(imageUrl), "image/*");
				startActivity(intent);
			}
		});
		ImageLoader.getInstance().displayImage(imageUrl, mQuestionImage);
		
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

}
