package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TipsDialog extends Dialog {
  private TextView mContent;
  private Button mKnow;

  public TipsDialog(Context context) {
    this(context, R.style.TipsDialog);
  }

  public TipsDialog(Context context, int theme) {
    super(context, theme);

    setContentView(R.layout.no_day_event);

    mKnow = (Button) findViewById(R.id.know);
    mContent = (TextView) findViewById(R.id.content);

    mKnow.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        dismiss();
      }
    });

    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
        SouShangApplication.FONT);
    mKnow.setTypeface(typeface);
    mContent.setTypeface(typeface);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

  }

  public void show(String message) {
    if (!TextUtils.isEmpty(message)) {
      mContent.setText(message);
    }
    super.show();
  }

}
