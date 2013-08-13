package com.baidu.soushang.widgets;

import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuitLBSEventDialog extends Dialog implements
    android.view.View.OnClickListener {
  private TextView mTitle;
  private TextView mContent;
  private Button mQuit;
  private Button mCancel;

  public interface OnClickListener {
    public void onQuit();

    public void onCancel();
  }

  private OnClickListener mListener;

  public void setOnClickListener(OnClickListener listener) {
    mListener = listener;
  }

  public QuitLBSEventDialog(Context context) {
    this(context, R.style.PopupDialog);
  }

  public QuitLBSEventDialog(Context context, int theme) {
    super(context, theme);

    setContentView(R.layout.quit_dialog);

    mTitle = (TextView) findViewById(R.id.title);
    mContent = (TextView) findViewById(R.id.content);
    mQuit = (Button) findViewById(R.id.quit);
    mCancel = (Button) findViewById(R.id.cancel);

    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
        SouShangApplication.FONT);
    mTitle.setTypeface(typeface);
    mContent.setTypeface(typeface);
    mQuit.setTypeface(typeface);
    mCancel.setTypeface(typeface);

    mQuit.setOnClickListener(this);
    mCancel.setOnClickListener(this);

    setCanceledOnTouchOutside(false);
  }

  @Override
  public void onClick(View v) {
    if (v == mQuit) {
      if (mListener != null) {
        mListener.onQuit();
      }

      dismiss();
    } else if (v == mCancel) {
      if (mListener != null) {
        mListener.onCancel();
      }

      cancel();
    }
  }

}
