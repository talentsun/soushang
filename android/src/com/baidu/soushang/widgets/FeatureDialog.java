package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.cloudapis.FeatureEvent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FeatureDialog extends Dialog {
  private TextView mTitle;
  private TextView mIntegral;
  private TextView mIntroduce;
  private Button mKonwn;
  private FeatureEvent mFeatureEvent;
  private Context mContext;

  public void setFeatureEvent(FeatureEvent featureEvent) {
    this.mFeatureEvent = featureEvent;
  }

  public interface OnClickListener {
    public void onResume();

    public void onHome();
  }

  private OnClickListener mOnClickListener;

  public OnClickListener getOnClickListener() {
    return mOnClickListener;
  }

  public void setOnClickListener(OnClickListener onClickListener) {
    this.mOnClickListener = onClickListener;
  }

  public FeatureDialog(Context context) {
    this(context, R.style.FeatureDialog);
  }

  public FeatureDialog(final Context context, int theme) {
    super(context, theme);
    setContentView(R.layout.feature_event_dialog);
    this.mContext = context;
    mTitle = (TextView) findViewById(R.id.title);
    mIntegral = (TextView) findViewById(R.id.integral);
    mIntroduce = (TextView) findViewById(R.id.introduce);
    mKonwn = (Button) findViewById(R.id.know);
    mKonwn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });

    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
        SouShangApplication.FONT);
    mKonwn.setTypeface(typeface);
    mTitle.setTypeface(typeface);
    mIntegral.setTypeface(typeface);
    mIntroduce.setTypeface(typeface);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

  }

  @Override
  public void show() {
    super.show();
    String title = mFeatureEvent.getTitle();
    int intergal = mFeatureEvent.getScore();
    if (!TextUtils.isEmpty(title)) {
      mTitle.setText(title);
    }
    mIntegral.setText(mContext.getResources().getString(R.string.feature_dialog_intergal)
        + intergal);
  }
}
