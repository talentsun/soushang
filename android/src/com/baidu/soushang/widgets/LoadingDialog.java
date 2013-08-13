package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
  private ImageView mPencil;
  private TextView mStatus;

  private Animation mAnim;

  public LoadingDialog(Context context) {
    this(context, R.style.LoadingDialog);
  }

  public LoadingDialog(Context context, int theme) {
    super(context, theme);
    setCancelable(false);
    setCanceledOnTouchOutside(false);

    setContentView(R.layout.loading_dialog);

    mPencil = (ImageView) findViewById(R.id.pencil);
    mStatus = (TextView) findViewById(R.id.status);

    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
        SouShangApplication.FONT);
    mStatus.setTypeface(typeface);

    LinearInterpolator lin = new LinearInterpolator();

    mAnim = AnimationUtils.loadAnimation(getContext(), R.anim.pencilz);
    mAnim.setInterpolator(lin);
    mAnim.setAnimationListener(new AnimationListener() {

      @Override
      public void onAnimationStart(Animation animation) {}

      @Override
      public void onAnimationRepeat(Animation animation) {}

      @Override
      public void onAnimationEnd(Animation animation) {
        mPencil.clearAnimation();
        mPencil.startAnimation(mAnim);
      }
    });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void show(String message) {
    if (!TextUtils.isEmpty(message)) {
      mStatus.setText(message);
    }

    mPencil.clearAnimation();
    mPencil.startAnimation(mAnim);

    super.show();
  }
}
