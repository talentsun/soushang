package com.baidu.soushang.activities;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.android.speech.SpeechConfig;
import com.baidu.android.speech.ui.BaiduSpeechDialog;
import com.baidu.android.speech.ui.DialogRecognitionListener;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baidu.soushang.R;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.QuestionResponse;
import com.baidu.soushang.utils.DialogUtils;
import com.google.gson.JsonObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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
	private Button mNextQuestion;
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
					ArrayList<String> results = arg0.getStringArrayList(BaiduSpeechDialog.RESULTS_RECOGNITION);
					if (results != null && results.size() > 0) {
						try {
							JSONObject result = new JSONObject(results.get(0));
							if (result.has("command_str")) {
								String commandStr = result.getString("command_str");
								if (!TextUtils.isEmpty(commandStr)) {
									JSONObject commandObject = new JSONObject(commandStr);
									
									JSONArray commandList = commandObject.getJSONArray("commandlist");
									if (commandList != null && commandList.length() > 0) {
										JSONObject command = commandList.getJSONObject(0);
										if (command != null) {
											String commandType = command.getString("commandtype");
											if (commandType.equalsIgnoreCase("search")) {
												JSONObject commandContent = command.getJSONObject("commandcontent");
												if (commandContent != null) {
													String baseUrl = commandContent.getString("baseurl");
													String searchContent = commandContent.getString("searchcontent");
													String web = commandContent.getString("web");
													
													if (!TextUtils.isEmpty(baseUrl) && !TextUtils.isEmpty(searchContent) && !TextUtils.isEmpty(web)) {
														DialogUtils.showSearchResultDialog(LoginActivity.this, searchContent, baseUrl, web);
														return;
													}
												}
											}
										}
									}
									
									String handleText = commandObject.getString("handle_text");
									DialogUtils.showSearchResultDialog(LoginActivity.this, handleText);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
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
		
		mNextQuestion = (Button) findViewById(R.id.next_question);
		mNextQuestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Apis.getNextQuestion(LoginActivity.this, new ApiResponseCallback<QuestionResponse>() {
					
					@Override
					public void onResults(QuestionResponse arg0) {
						if (arg0 != null) {
							Log.i("next question", arg0.getTitle());
							Log.i("next question", "" + arg0.getRightAnswer());
						} else {
							Log.i("next question", "failed");
						}
					}
					
					@Override
					public void onError(Throwable arg0) {
						Log.i("next question", "failed");
					}
				});
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
