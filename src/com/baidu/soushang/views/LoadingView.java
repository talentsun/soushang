package com.baidu.soushang.views;

import com.baidu.soushang.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class LoadingView extends ImageView {

  public LoadingView(Context context) {
    super(context);
    initView();
  }

  public LoadingView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initView();
  }



  public LoadingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }



  private void initView() {
    setBackgroundResource(R.drawable.loading);
    
    Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
    LinearInterpolator lin = new LinearInterpolator();
    anim.setInterpolator(lin);
    
    clearAnimation();
    startAnimation(anim);
  }

}
