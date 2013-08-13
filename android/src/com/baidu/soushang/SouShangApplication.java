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
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.CommonResponse;
import com.baidu.soushang.cloudapis.AnswerRequest.Answer;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.FeatureEvent;
import com.baidu.soushang.cloudapis.ShopInfo;
import com.baidu.soushang.cloudapis.UserInfoResponse;
import com.baidu.soushang.lbs.LBSService;
import com.baidu.soushang.lbs.Models.User;
import com.baidu.soushang.utils.SystemUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SouShangApplication extends Application {
  public static final String FONT = "fonts/yuppy-sc.otf";

  private static final String APP_KEY = "VKgVRtN9Lja2Uu4mcRumpkTY";
  private static final String APP_SECRET = "Fnp8AQYSp9jR7G9RluVbgBxqmz7bQexH";
  private static final String USERINFO_URL =
      "https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser";

  private List<Answer> mAnswers;
  private boolean mIsLBSServiceOn = true;

  static {
    SouShangApplication.CurrentFeatureEvent = new FeatureEvent();
    SouShangApplication.CurrentShopInfo = new ShopInfo();
  }

  public void setAnswers(List<Answer> answers) {
    mAnswers = answers;
  }

  public List<Answer> getAnswers() {
    return mAnswers;
  }


  public boolean isLBSServiceOn() {
    return mIsLBSServiceOn;
  }

  public void setLBSServiceOn(boolean serviceOn) {
    this.mIsLBSServiceOn = serviceOn;
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

  public interface UpdateUserInfoListener {
    public void onUpdated(com.baidu.soushang.cloudapis.User user);

    public void onError();
  }

  private UpdateUserInfoListener mUpdateUserInfoListener;

  public void setUpdateUserInfoListener(UpdateUserInfoListener listener) {
    mUpdateUserInfoListener = listener;
  }

  private com.baidu.soushang.cloudapis.User mUser;

  public com.baidu.soushang.cloudapis.User getUser() {
    return mUser;
  }

  private List<User> mPeers;

  public void setPeers(List<User> peers) {
    mPeers = peers;
  }

  public List<User> getPeers() {
    return mPeers;
  }

  private User mCurrentPeer;

  public void setCurrentPeer(User peer) {
    mCurrentPeer = peer;
  }

  public User getCurrentPeer() {
    return mCurrentPeer;
  }

  private DisplayImageOptions mOption;

  public DisplayImageOptions getAvatarDisplayOption() {
    return mOption;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
        .cacheInMemory().cacheOnDisc().build();
    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
        getApplicationContext()).defaultDisplayImageOptions(
        displayOptions).build();
    ImageLoader.getInstance().init(config);
    mOption = new DisplayImageOptions.Builder()
        .showImageOnFail(R.drawable.default_avatar)
        .showImageForEmptyUri(R.drawable.default_avatar)
        .showStubImage(R.drawable.default_avatar)
        .displayer(
            new RoundedBitmapDisplayer(
                getResources().getDimensionPixelSize(
                    R.dimen.avatar_width) / 2)).build();

    SpeechConfig.setAppId(APP_KEY);
    SpeechConfig.setAppKey(APP_SECRET);
//  CookieSyncManager.createInstance(this);

    Config.getUDID(this);
  }

  private ApiResponseCallback<CommonResponse> mLoginCallback =
      new ApiResponseCallback<CommonResponse>() {

        @Override
        public void onResults(CommonResponse arg0) {

          if (arg0 != null && arg0.getRetCode() == 0) {
            Config.setLogged(getApplicationContext(), true);
            updateBaseUserInfo();
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

  public void logout(Activity context) {
    if (mBaidu != null) {
      mBaidu.LogOut();
    }
    
    mUser = null;
  }

  public void login(Activity context) {
    Log.d("app", "login");

    mBaidu = new Baidu(APP_KEY, APP_SECRET, context);
    mBaidu.authorize(context, new BaiduDialogListener() {

      @Override
      public void onError(BaiduDialogError arg0) {

      }

      @Override
      public void onComplete(Bundle arg0) {
        
        Log.i("access_token", mBaidu.getAccessToken());
        Config.setAccessToken(SouShangApplication.this,
            mBaidu.getAccessToken());

        Apis.Login(SouShangApplication.this, mBaidu.getAccessToken(),
            mLoginCallback);

      }

      @Override
      public void onCancel() {

      }

      @Override
      public void onBaiduException(BaiduException arg0) {}
    });
  }

  private ApiResponseCallback<UserInfoResponse> mUserExtraInfoCallback =
      new ApiResponseCallback<UserInfoResponse>() {

        @Override
        public void onResults(UserInfoResponse arg0) {
          if (arg0 != null && arg0.getRetCode() == 0
              && arg0.getUser() != null) {
            mUser = arg0.getUser();
            if (mUpdateUserInfoListener != null) {
              mUpdateUserInfoListener.onUpdated(mUser);
            }
          }
        }

        @Override
        public void onError(Throwable arg0) {

          if (mUpdateUserInfoListener != null) {
            mUpdateUserInfoListener.onError();
          }
        }
      };

  public static int DailyFeatureEvent = 0;

  public static String CATID = "1";

  public static ShopInfo CurrentShopInfo = null;

  public static FeatureEvent CurrentFeatureEvent = null;

  public void updateUserExtraInfo() {
    Apis.getUserInfo(this, Config.getAccessToken(this),
        mUserExtraInfoCallback);
  }

  public void updateBaseUserInfo() {
    
    new AsyncBaiduRunner(mBaidu).request(USERINFO_URL, null, "GET",
        new RequestListener() {

          @Override
          public void onIOException(IOException arg0) {
            updateUserBaseInfoError();
          }

          @Override
          public void onComplete(String arg0) {
            Log.d("userInfo", arg0);
            try {

              JSONObject obj = new JSONObject(arg0);
              if (obj.has("uname")) {
                Config.setUserName(SouShangApplication.this,
                    obj.getString("uname"));
              } else {
                Config.setUserName(SouShangApplication.this,
                    SystemUtils.getDeviceName(SouShangApplication.this));
              }

              if (obj.has("uid")) {
                Config.setUserId(SouShangApplication.this,
                    obj.getLong("uid"));
                Log.d("userId", obj.getLong("uid") + "");
              } else {
                Config.setUserId(SouShangApplication.this, 0);
              }

              if (obj.has("portrait")) {
                Config.setAvatar(SouShangApplication.this,
                    "http://tb.himg.baidu.com/sys/portrait/item/"
                        + obj.getString("portrait"));
              } else {
                Config.setAvatar(SouShangApplication.this,
                    "http://tb.himg.baidu.com/sys/portrait/item/");
              }

              Intent lbsIntent = new Intent(
                  SouShangApplication.this, LBSService.class);

              startService(lbsIntent);

              if (mLoginListener != null) {
                mLoginListener.onSuccess();

              }
            } catch (Exception e) {
              updateUserBaseInfoError();
            }

            updateUserExtraInfo();
          }

          @Override
          public void onBaiduException(BaiduException arg0) {
            updateUserBaseInfoError();
          }
        });
  }

  private void updateUserBaseInfoError() {
    Config.setUserName(SouShangApplication.this,
        SystemUtils.getDeviceName(SouShangApplication.this));
    Config.setAvatar(SouShangApplication.this,
        "http://tb.himg.baidu.com/sys/portrait/item/");
    Config.setUserId(SouShangApplication.this, 0);

    if (mLoginListener != null) {
      mLoginListener.onError();
    }
  }

}
