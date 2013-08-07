package com.baidu.soushang.views;

import com.baidu.soushang.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class LoadingView extends ImageView {
	private Animation mAnim;

	public LoadingView(Context context) {
		super(context);
		initView();
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		setBackgroundResource(R.drawable.loading);

		mAnim = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
		LinearInterpolator lin = new LinearInterpolator();
		mAnim.setInterpolator(lin);
	}

	public void show() {
		clearAnimation();
		startAnimation(mAnim);
		setVisibility(View.VISIBLE);
	}

	public void hide() {
		clearAnimation();
		setVisibility(View.GONE);
	}

}
