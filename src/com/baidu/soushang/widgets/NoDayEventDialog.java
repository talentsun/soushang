package com.baidu.soushang.widgets;

import com.baidu.soushang.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NoDayEventDialog extends Dialog {
  private TextView mContent;
  private Button mKnow;

  public NoDayEventDialog(Context context) {
    this(context, 0);
  }

  public NoDayEventDialog(Context context, int theme) {
    super(context, theme);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.no_day_event);
    
    mKnow = (Button) findViewById(R.id.know);
    mContent = (TextView) findViewById(R.id.content);
    
    mKnow.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
    
    super.onCreate(savedInstanceState);
  }

}
