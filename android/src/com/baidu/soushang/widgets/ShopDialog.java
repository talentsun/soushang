package com.baidu.soushang.widgets;

import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
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
  private TextView mTitle;
  private TextView mYourMark;
  private TextView mNeedMark;
  private TextView mReceMsg, mReceName, mReceAddr, mRecePhone;
  private EditText mEditName, mEditAddr, mEditPhone;
  private Button mExchange;
  private Button mCancel; 
  private SharedPreferences sp;
  private SharedPreferences.Editor et;

  public ShopDialog(Context context) {
    this(context, R.style.ShareDialog);
  }

  public ShopDialog(final Context context, int theme) {
    super(context, theme);
    setContentView(R.layout.shop_dialog);

    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
        SouShangApplication.FONT);

    mApplication = (SouShangApplication) ((Activity) context)
        .getApplication();

    mTitle = (TextView) findViewById(R.id.shop_dialog_title_exchange);
    mTitle.setTypeface(typeface);
    mTitle.setText(SouShangApplication.CurrentShopInfo.getTitle());

    mYourMark = (TextView) findViewById(R.id.shop_dialog_ymark);
    mYourMark.setTypeface(typeface);
    mYourMark.setText(context.getResources().getString(
        R.string.shop_dialog_ymark)
        + mApplication.getUser().getPoint());

    mNeedMark = (TextView) findViewById(R.id.shop_dialog_nmark);
    mNeedMark.setTypeface(typeface);
    mNeedMark.setText(context.getResources().getString(
        R.string.shop_dialog_nmark)
        + SouShangApplication.CurrentShopInfo.getIntegral());

    mReceMsg = (TextView) findViewById(R.id.shop_dialog_rece_msg);
    mReceMsg.setTypeface(typeface);

    mReceName = (TextView) findViewById(R.id.shop_dialog_rece_name);
    mReceName.setTypeface(typeface);
    mEditName = (EditText) findViewById(R.id.shop_dialog_edit_name);
    mEditName.setTypeface(typeface);

    mReceAddr = (TextView) findViewById(R.id.shop_dialog_rece_address);
    mReceAddr.setTypeface(typeface);
    mEditAddr = (EditText) findViewById(R.id.shop_dialog_edit_address);
    mEditAddr.setTypeface(typeface);

    mRecePhone = (TextView) findViewById(R.id.shop_dialog_rece_phone);
    mRecePhone.setTypeface(typeface);
    mEditPhone = (EditText) findViewById(R.id.shop_dialog_edit_phone);
    mEditPhone.setTypeface(typeface);

    sp = context.getSharedPreferences(Intents.EXTRA_SHOP_SEND_ADDRESS,
        Activity.MODE_PRIVATE);
    et = sp.edit();
    String key = mApplication.getUser().getUsername();
    String shop = sp.getString(key, "first");
    if (!shop.equals("first")) {
      String msg[] = shop.split("#");
      mEditName.setText(msg[0]);
      mEditAddr.setText(msg[1]);
      mEditPhone.setText(msg[2]);
    }

    mExchange = (Button) findViewById(R.id.shop_dialog_btn_exchange);
    mExchange.setTypeface(typeface);
    mExchange.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        String name = mEditName.getText().toString();
        if (TextUtils.isEmpty(name)) {
          Toast.makeText(
              context,
              context.getResources().getString(
                  R.string.shop_dialog_rec_name_erro),
              Toast.LENGTH_SHORT).show();
          return;
        }

        String addr = mEditAddr.getText().toString();
        if (TextUtils.isEmpty(addr)) {
          Toast.makeText(
              context,
              context.getResources().getString(
                  R.string.shop_dialog_rec_addr_erro),
              Toast.LENGTH_SHORT).show();
          return;
        }

        String phone = mEditPhone.getText().toString();
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
        sExchangeInfo.setGid(SouShangApplication.CurrentShopInfo.getId());
        Apis.exchange(context, sExchangeInfo, null);

        mYourMark.setText(context.getResources().getString(
            R.string.shop_dialog_ymark)
            + mApplication.getUser().getPoint());
        mNeedMark.setText(context.getResources().getString(
            R.string.shop_dialog_nmark)
            + SouShangApplication.CurrentShopInfo.getIntegral());
        dismiss();

        mApplication.updateUserExtraInfo();
      }
    });

    mCancel = (Button) findViewById(R.id.shop_dialog_btn_cancel);
    mCancel.setTypeface(typeface);
    mCancel.setOnClickListener(new View.OnClickListener() {

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
