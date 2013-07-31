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

public class LBSFirstDialog extends Dialog {
	private Button cancel;
	private Button challenge;

	public LBSFirstDialog(Context context) {

		this(context, R.style.FeatureDialog);
	}

	public LBSFirstDialog(final Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.lbs_event_dialog);

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

		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
				SouShangApplication.FONT);
		challenge.setTypeface(typeface);
		cancel.setTypeface(typeface);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

	}

}
