package com.baidu.soushang.widgets;

import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.Variables;
import com.baidu.soushang.activities.QuestionActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FeatureDialog extends Dialog {
	private TextView title;
	private TextView introduce;
	private Button cancel;
	private Button start;

	public FeatureDialog(Context context) {

		this(context, R.style.FeatureDialog);
	}

	public FeatureDialog(final Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.feature_event_dialog);

		cancel = (Button) findViewById(R.id.cancel);
		title = (TextView) findViewById(R.id.title);
		introduce = (TextView) findViewById(R.id.introduce);
		start = (Button) findViewById(R.id.start);

		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!Variables.feBean.isFinished()) {
					
					Intent intent = null;
					intent = new Intent(context, QuestionActivity.class);
					intent.putExtra(Intents.EXTRA_EVENT_TYPE,
							Intents.EVENT_TYPE_FEATURE);
					
					context.startActivity(intent);
				} else {
					
					dismiss();
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.feature_join_tips),
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
				SouShangApplication.FONT);
		start.setTypeface(typeface);
		cancel.setTypeface(typeface);
		title.setTypeface(typeface);
		introduce.setTypeface(typeface);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();

		String mTitle = Variables.feBean.getTitle();
		String mIntroduce = Variables.feBean.getIntroduce();

		if (!TextUtils.isEmpty(mTitle)) {
			title.setText(mTitle);
		}

		if (!TextUtils.isEmpty(mIntroduce)) {
			introduce.setText(mIntroduce);
		}
		if (!Variables.feBean.isRunning()) {
			start.setEnabled(false);
			start.setBackgroundResource(R.drawable.zhuanti_intro_startbtdisable);
		} else {
			start.setEnabled(true);
			start.setBackgroundResource(R.drawable.feature_dialog_start);
		}

	}

}
