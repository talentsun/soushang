package com.baidu.soushang;

import java.util.List;

import com.baidu.android.speech.SpeechConfig;
import com.baidu.soushang.cloudapis.AnswerRequest.Answer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.webkit.CookieSyncManager;

public class SouShangApplication extends Application {
  public static final String APP_KEY = "VKgVRtN9Lja2Uu4mcRumpkTY";
  public static final String APP_SECRET = "Fnp8AQYSp9jR7G9RluVbgBxqmz7bQexH";
  
  private List<Answer> mAnswers;
  
  public void setAnswers(List<Answer> answers) {
    mAnswers = answers;
  }
  
  public List<Answer> getAnswers() {
    return mAnswers;
  }
  
  @Override
  public void onCreate() {
    super.onCreate();

    DisplayImageOptions displayOptions =
        new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
    ImageLoaderConfiguration config =
        new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(
            displayOptions).build();
    ImageLoader.getInstance().init(config);
    
    SpeechConfig.setAppId(APP_KEY);
    SpeechConfig.setAppKey(APP_SECRET);
    CookieSyncManager.createInstance(this);
  }

}
