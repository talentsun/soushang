package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.activities.HomeActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LBSFirstDialog extends Dialog {

	private TextView dialogTitle;
	private TextView dialogWelcome;
	private TextView dialogIntroduce;
	private TextView ruleTitle;
	private TextView ruleContent;
	private TextView dialogInvite;

	private Button cancel;
	private Button challenge;

	public LBSFirstDialog(Context context) {

		this(context, R.style.FeatureDialog);
	}

	public LBSFirstDialog(final Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.lbs_event_dialog);

		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
				SouShangApplication.FONT);

		dialogTitle = (TextView) findViewById(R.id.lbs_tv_title);
		dialogTitle.setTypeface(typeface);

		dialogWelcome = (TextView) findViewById(R.id.lbs_tv_welcome);
		dialogWelcome.setTypeface(typeface);

		dialogIntroduce = (TextView) findViewById(R.id.lbs_tv_introduce);
		dialogIntroduce.setTypeface(typeface);

		ruleTitle = (TextView) findViewById(R.id.lbs_tv_rule_title);
		ruleTitle.setTypeface(typeface);

		ruleContent = (TextView) findViewById(R.id.lbs_tv_rule_content);
		ruleContent.setTypeface(typeface);

		dialogInvite = (TextView) findViewById(R.id.lbs_tv_invite);
		dialogInvite.setTypeface(typeface);

		cancel = (Button) findViewById(R.id.lbs_dialog_cancel);
		challenge = (Button) findViewById(R.id.lbs_dialog_challenge);

		challenge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				context.startActivity(new Intent(context, HomeActivity.class));
			}
		});

		challenge.setTypeface(typeface);
		cancel.setTypeface(typeface);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

	}

}
