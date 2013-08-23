package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class DialyEventDialog extends Dialog {
  private TextView mDialyTips;

  public DialyEventDialog(Context context) {
    this(context, R.style.DialyEventDialog);
  }

  public DialyEventDialog(Context context, int theme) {
    super(context, theme);
    setContentView(R.layout.loading_daily_dialog);
    mDialyTips = (TextView) findViewById(R.id.dialy_dialog_tip);
    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
        SouShangApplication.FONT);
    mDialyTips.setTypeface(typeface);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

  }

}
