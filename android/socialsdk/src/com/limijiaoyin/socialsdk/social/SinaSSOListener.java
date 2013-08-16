package com.limijiaoyin.socialsdk.social;import java.util.ArrayList;import java.util.List;import org.apache.http.HttpEntity;import org.apache.http.HttpResponse;import org.apache.http.HttpStatus;import org.apache.http.NameValuePair;import org.apache.http.client.entity.UrlEncodedFormEntity;import org.apache.http.client.methods.HttpPost;import org.apache.http.impl.client.DefaultHttpClient;import org.apache.http.message.BasicNameValuePair;import org.apache.http.protocol.BasicHttpContext;import org.apache.http.protocol.HttpContext;import android.os.Bundle;import android.util.Log;import com.limijiaoyin.socialsdk.Const;import com.limijiaoyin.socialsdk.ISocialLogin;import com.limijiaoyin.socialsdk.Platform;import com.weibo.sdk.android.Oauth2AccessToken;import com.weibo.sdk.android.WeiboAuthListener;import com.weibo.sdk.android.WeiboDialogError;import com.weibo.sdk.android.WeiboException;public class SinaSSOListener implements WeiboAuthListener {  String token;  String expireTime;  String code;  Oauth2AccessToken accessToken;  private String deviceId;  private ISocialLogin mLoginListener;  public SinaSSOListener(String deviceId, ISocialLogin loginListener) {    this.deviceId = deviceId;    this.mLoginListener = loginListener;  }  @Override  public void onCancel() {    Log.d("sina", "onCancel");  }  @Override  public void onComplete(Bundle values) {    code = values.getString("code");    if (code != null) {//      mLoginListener.onLoginSuccess(Platform.SINA);    } else {      token = values.getString("access_token");      expireTime = values.getString("expires_in");      accessToken = new Oauth2AccessToken(token, expireTime);      if (accessToken.isSessionValid()) {        new PostSinaSSOTokenThread().start();        Log.d("sina", accessToken.getToken());      } else {        Log.d("sina", "accessToken is invalid");      }    }  }  @Override  public void onError(WeiboDialogError arg0) {    Log.d("sina", arg0.getMessage());  }  @Override  public void onWeiboException(WeiboException arg0) {    Log.d("sina", arg0.getMessage());  }  class PostSinaSSOTokenThread extends Thread {    @Override    public void run() {      sendSinaSSOToken(accessToken.getToken());    }  }  private void sendSinaSSOToken(String accessToken) {    HttpPost request = new HttpPost(Const.ACCESS_TOKEN_URL);    List<NameValuePair> params = new ArrayList<NameValuePair>();    params.add(new BasicNameValuePair("deviceId", deviceId));    params.add(new BasicNameValuePair("accessToken", accessToken));    params.add(new BasicNameValuePair("platform", "sina"));    try {      HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");      request.setEntity(entity);      DefaultHttpClient client = new DefaultHttpClient();      HttpContext context = new BasicHttpContext();      HttpResponse response = client.execute(request, context);      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {        mLoginListener.onLoginSuccess(Platform.SINA);      } else {}    } catch (Exception e) {      Log.d("sina", e.getMessage());    }  }}