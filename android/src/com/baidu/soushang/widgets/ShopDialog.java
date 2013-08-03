package com.baidu.soushang.widgets;

import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.Variables;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.ShopExchangeInfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShopDialog extends Dialog {

	private SouShangApplication mApplication;
	private TextView title;
	private TextView yMark;
	private TextView nMark;
	private TextView receMsg,receName,receAddr,recePhone;
	private EditText editName, editAddr, editPhone;
	private Button exchange;
	private Button cancel;
	private SharedPreferences sp;
	private SharedPreferences.Editor et;

	public ShopDialog(Context context) {
		this(context, R.style.FeatureDialog);
	}

	public ShopDialog(final Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.shop_dialog);

		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
				SouShangApplication.FONT);
		
		mApplication = (SouShangApplication) ((Activity) context).getApplication();
		
		title = (TextView) findViewById(R.id.shop_dialog_title_exchange);
		title.setTypeface(typeface);
		title.setText(Variables.shBean.getTitle());

		yMark = (TextView) findViewById(R.id.shop_dialog_ymark);
		yMark.setTypeface(typeface);
		yMark.setText(context.getResources().getString(
				R.string.shop_dialog_ymark)
				+ mApplication.getUser().getPoint());

		nMark = (TextView) findViewById(R.id.shop_dialog_nmark);
		nMark.setTypeface(typeface);
		nMark.setText(context.getResources().getString(
				R.string.shop_dialog_nmark)
				+ Variables.shBean.getIntegral());

		receMsg=(TextView)findViewById(R.id.shop_dialog_rece_msg);
		receMsg.setTypeface(typeface);
		
		receName=(TextView)findViewById(R.id.shop_dialog_rece_name);
		receName.setTypeface(typeface);
		editName = (EditText) findViewById(R.id.shop_dialog_edit_name);
		editName.setTypeface(typeface);

		receAddr=(TextView)findViewById(R.id.shop_dialog_rece_address);
		receAddr.setTypeface(typeface);
		editAddr = (EditText) findViewById(R.id.shop_dialog_edit_address);
		editAddr.setTypeface(typeface);

		recePhone=(TextView)findViewById(R.id.shop_dialog_rece_phone);
		recePhone.setTypeface(typeface);
		editPhone = (EditText) findViewById(R.id.shop_dialog_edit_phone);
		editPhone.setTypeface(typeface);

		sp = context.getSharedPreferences(Intents.EXTRA_SHOP_SEND_ADDRESS,
				Activity.MODE_PRIVATE);
		et = sp.edit();
		String key = mApplication.getUser().getUsername();
		String shop = sp.getString(key, "first");
		if (!shop.equals("first")) {
			String msg[] = shop.split("#");
			editName.setText(msg[0]);
			editAddr.setText(msg[1]);
			editPhone.setText(msg[2]);
		}

		exchange = (Button) findViewById(R.id.shop_dialog_btn_exchange);
		exchange.setTypeface(typeface);
		exchange.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = editName.getText().toString();
				if (TextUtils.isEmpty(name)) {
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.shop_dialog_rec_name_erro),
							Toast.LENGTH_SHORT).show();
					return;
				}

				String addr = editAddr.getText().toString();
				if (TextUtils.isEmpty(addr)) {
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.shop_dialog_rec_addr_erro),
							Toast.LENGTH_SHORT).show();
					return;
				}

				String phone = editPhone.getText().toString();
				if (TextUtils.isEmpty(phone)) {
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.shop_dialog_rec_phone_erro),
							Toast.LENGTH_SHORT).show();
					return;
				}

				String msg = name + "#" + addr + "#" + phone;
				et.putString(mApplication.getUser().getUsername(), msg);
				et.commit();

				ShopExchangeInfo sExchangeInfo = new ShopExchangeInfo();
				sExchangeInfo.setAccess_token(Config.getAccessToken(context));
				sExchangeInfo.setRealname(name);
				sExchangeInfo.setDelivery(addr);
				sExchangeInfo.setPhone(phone);
				sExchangeInfo.setGid(Variables.shBean.getId());
				// System.out.println("at ShopDialog sExchangeInfo.toJSON()=="
				// + sExchangeInfo.toJSON());
				Apis.exchange(context, sExchangeInfo, null);
				
				System.out
						.println("at onclick of ShopDialog mApplication.getUser().getPoint()=="
								+ mApplication.getUser().getPoint());
				yMark.setText(context.getResources().getString(
						R.string.shop_dialog_ymark)
						+ mApplication.getUser().getPoint());
				nMark.setText(context.getResources().getString(
						R.string.shop_dialog_nmark)
						+ Variables.shBean.getIntegral());
				dismiss();
				
				mApplication.updateUserExtraInfo();
			}
		});

		cancel = (Button) findViewById(R.id.shop_dialog_btn_cancel);
		cancel.setTypeface(typeface);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();

	}

}
