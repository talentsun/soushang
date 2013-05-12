package com.baidu.soushang.activities;

import java.io.IOException;

import com.baidu.android.speech.SpeechConfig;
import com.baidu.android.speech.ui.BaiduSpeechDialog;
import com.baidu.android.speech.ui.DialogRecognitionListener;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baidu.soushang.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.widget.Button;

public class LoginActivity extends FragmentActivity {
	private static final String APP_KEY = "VKgVRtN9Lja2Uu4mcRumpkTY";
	private static final String APP_SECRET = "Fnp8AQYSp9jR7G9RluVbgBxqmz7bQexH";
	private static final String LOGGED_USER_INGO = "https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser";
	
	private Button mLogin;
	private Button mSa;
	private Baidu mBaidu;
	private BaiduSpeechDialog mBaiduSpeechDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.login);
		
		mLogin = (Button) findViewById(R.id.login);
		mLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mBaidu = new Baidu(APP_KEY, APP_SECRET, LoginActivity.this);
				mBaidu.authorize(LoginActivity.this, new BaiduDialogListener() {
					
					@Override
					public void onError(BaiduDialogError arg0) {
					}
					
					@Override
					public void onComplete(Bundle arg0) {
						Log.i("token", mBaidu.getAccessToken());
						
						try {
							String json = mBaidu.request(LOGGED_USER_INGO, null, "POST");
							Log.i("json", json);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (BaiduException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onBaiduException(BaiduException arg0) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
		mSa = (Button) findViewById(R.id.sa);
		SpeechConfig.setAppId(APP_KEY);
		SpeechConfig.setAppKey(APP_SECRET);
		
		CookieSyncManager.createInstance(this);
		
		mBaiduSpeechDialog = new BaiduSpeechDialog(LoginActivity.this);
		mBaiduSpeechDialog.setDialogRecognitionListener(new DialogRecognitionListener() {
			
			@Override
			public void onResults(Bundle arg0) {
				if (arg0 != null) {
					Log.i("sa", arg0.toString());
				}
			}
			
			@Override
			public void onPartialResults(Bundle arg0) {
			}
		});
		Bundle params = new Bundle();
		params.putString(BaiduSpeechDialog.PORMPT_TEXT, getResources().getString(R.string.sa_help));
		mBaiduSpeechDialog.setParams(BaiduSpeechDialog.SpeechMode.VOICE_TO_COMMAND, null);
		
		mSa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mBaiduSpeechDialog.show();
			}
		});
		
		super.onCreate(arg0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mBaiduSpeechDialog != null) {
			mBaiduSpeechDialog.dismiss();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
}
