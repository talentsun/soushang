package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.Variables;
import com.baidu.soushang.activities.HomeActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class PausedDialog extends Dialog implements
		android.view.View.OnClickListener {
	public interface OnClickListener {
		public void onResume();

		public void onHome();
	}

	private OnClickListener mOnClickListener;
	private Button mResume;
	private Button mHome;

	public OnClickListener getOnClickListener() {
		return mOnClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.mOnClickListener = onClickListener;
	}

	public PausedDialog(Context context) {
		this(context, R.style.PausedDialog);
	}

	public PausedDialog(Context context, int theme) {
		super(context, theme);

		setContentView(R.layout.paused_dialog);

		mResume = (Button) findViewById(R.id.resume);
		mHome = (Button) findViewById(R.id.home);

		mResume.setOnClickListener(this);
		mHome.setOnClickListener(this);

		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onClick(View v) {
		if (v == mResume) {
			if (mOnClickListener != null) {
				mOnClickListener.onResume();
			}
		} else if (v == mHome) {
			Variables.homeFlag=0;
			home();
		}

		dismiss();
	}

	@Override
	public void onBackPressed() {
		home();
		dismiss();
	}

	private void home() {
		if (mOnClickListener != null) {
			mOnClickListener.onHome();
		}

		Intent intent = new Intent(getContext(), HomeActivity.class);
		getContext().startActivity(intent);
	}
}
