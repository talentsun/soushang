package com.baidu.soushang;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;

import com.baidu.android.speech.SpeechConfig;
import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baidu.api.AsyncBaiduRunner.RequestListener;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.soushang.activities.EventCompletedActivity;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.CommonResponse;
import com.baidu.soushang.cloudapis.AnswerRequest.Answer;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.lbs.LBSService;
import com.baidu.soushang.lbs.Models.User;
import com.baidu.soushang.utils.SystemUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

public class SouShangApplication extends Application {
  public static final String FONT = "fonts/yuppy-sc.otf";
  
  private static final String APP_KEY = "VKgVRtN9Lja2Uu4mcRumpkTY";
  private static final String APP_SECRET = "Fnp8AQYSp9jR7G9RluVbgBxqmz7bQexH";
  private static final String USERINFO_URL = "https://openapi.baidu.com/rest/2.0/passport/users/getInfo";
  
  private List<Answer> mAnswers;
  public void setAnswers(List<Answer> answers) {
    mAnswers = answers;
  }
  
  public List<Answer> getAnswers() {
    return mAnswers;
  }
  
  private Baidu mBaidu;
  
  public interface LoginListener {
    public void onSuccess();
    public void onFail();
    public void onError();
  }
  
  private LoginListener mLoginListener;
  
  public void setLoginListener(LoginListener listener) {
    mLoginListener = listener;
  }
  
  private List<User> mPeers;
  
  public void setPeers(List<User> peers) {
    mPeers = peers;
  }
  
  public List<User> getPeers() {
    return mPeers;
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
    
    Config.getUDID(this);
  }
  
  private ApiResponseCallback<CommonResponse> mLoginCallback =
    new ApiResponseCallback<CommonResponse>() {

      @Override
      public void onResults(CommonResponse arg0) {
        if (arg0 != null && arg0.getRetCode() == 0) {
          Config.setLogged(getApplicationContext(), true);
          updateUserInfo();
        } else {
          Config.setLogged(getApplicationContext(), false);
          
          if (mLoginListener != null) {
            mLoginListener.onFail();
          }
        }
      }

      @Override
      public void onError(Throwable arg0) {
        Config.setLogged(getApplicationContext(), false);
        
        if (mLoginListener != null) {
          mLoginListener.onError();
        }
      }
    };
  
  public void login(Activity context) {
    Log.d("app", "login");
    mBaidu = new Baidu(APP_KEY,
      APP_SECRET, context);
    mBaidu.authorize(context,
      new BaiduDialogListener() {

        @Override
        public void onError(BaiduDialogError arg0) {
          
        }

        @Override
        public void onComplete(Bundle arg0) {
          Log.i("access_token", mBaidu.getAccessToken());
          Config.setAccessToken(SouShangApplication.this,
                      mBaidu.getAccessToken());
          Apis.Login(SouShangApplication.this,
                      mBaidu.getAccessToken(), mLoginCallback);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onBaiduException(BaiduException arg0) {}
      });
  }
  
  private void updateUserInfo() {
    new AsyncBaiduRunner(mBaidu).request(USERINFO_URL, null, "GET", new RequestListener() {
      
      @Override
      public void onIOException(IOException arg0) {
        Config.setUserName(SouShangApplication.this, SystemUtils.getDeviceName(SouShangApplication.this));
      }
      
      @Override
      public void onComplete(String arg0) {
        try {
          JSONObject obj = new JSONObject(arg0);
          Config.setUserName(SouShangApplication.this, obj.getString("username"));
          
          Intent lbsIntent = new Intent(SouShangApplication.this, LBSService.class);
          SouShangApplication.this.startService(lbsIntent);
        } catch (Exception e) {
          Config.setUserName(SouShangApplication.this, SystemUtils.getDeviceName(SouShangApplication.this));
        }
        
        if (mLoginListener != null) {
          mLoginListener.onSuccess();
        }
      }
      
      @Override
      public void onBaiduException(BaiduException arg0) {
        Config.setUserName(SouShangApplication.this, SystemUtils.getDeviceName(SouShangApplication.this));
        if (mLoginListener != null) {
          mLoginListener.onSuccess();
        }
      }
    });
  }

}
