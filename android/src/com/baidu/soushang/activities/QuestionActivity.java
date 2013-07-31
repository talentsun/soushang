package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.speech.ui.BaiduSpeechDialog;
import com.baidu.android.speech.ui.DialogRecognitionListener;
import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.Variables;
import com.baidu.soushang.cloudapis.AnswerRequest.Answer;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.QuestionResponse;
import com.baidu.soushang.lbs.LBSService;
import com.baidu.soushang.widgets.LoadingDialog;
import com.baidu.soushang.widgets.PausedDialog;
import com.baidu.soushang.widgets.QuitLBSEventDialog;
import com.baidu.soushang.widgets.WebViewDialog;

public class QuestionActivity extends BaseActivity implements
		ApiResponseCallback<QuestionResponse>, OnClickListener {
	class AnswerTimer extends CountDownTimer {
		private ProgressBar mProgressBar;

		public AnswerTimer(ProgressBar progressBar, long millisInFuture,
				long countDownInterval) {
			super(millisInFuture, countDownInterval);
			mProgressBar = progressBar;
		}

		@Override
		public void onFinish() {
			mProgressBar.setProgress(0);
			mTimeout.setVisibility(View.VISIBLE);
			answer(-1);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int progress = (int) (millisUntilFinished / 100);
			mProgressBar.setProgress(progress);
			// if (progress <= 50) {
			// mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.answer_timer_red));
			// } else {
			// mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.answer_timer));
			// }
		}

	}

	public class FightStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String action = intent.getAction();
				if (Intents.ACTION_FIGHTING.equalsIgnoreCase(action)) {
					mOtherPoint = intent.getIntExtra(Intents.EXTRA_RIGHT, 0)
							* POINT_INTERVAL;
					updatePointBoard();
				} else if (Intents.ACTION_FIGHT_END.equalsIgnoreCase(action)) {
					showLBSEventCompleted(
							intent.getBooleanExtra(Intents.EXTRA_WIN, false),
							intent.getIntExtra(Intents.EXTRA_MY_POINT, 0),
							intent.getIntExtra(Intents.EXTRA_MY_TIME, 0),
							intent.getIntExtra(Intents.EXTRA_OTHER_POINT, 0),
							intent.getIntExtra(Intents.EXTRA_OTHER_TIME, 0),
							intent.getIntExtra(Intents.EXTRA_MY_POINT_DELTA, 0),
							intent.getIntExtra(Intents.EXTRA_MY_WIN_RATE, 0));
				}
			}
		}

	}

	private TextView mMatchName;
	private TextView mPointBoard;
	private Button mPauseResume;
	private Button mQuit;
	private TextView mQuestionOrder;
	private TextView mQuestionTitle;
	private ProgressBar mAnswerTime;
	private Button mHelp;
	private Button mOptionA;
	private Button mOptionB;
	private Button mOptionC;
	private Button mOptionD;
	private ImageView mTimeout;

	private ImageView mAnswerResults[][] = new ImageView[4][2];

	private AnswerTimer mTimer;

	private QuestionResponse.Question mCurrentQuestion;
	private volatile boolean mPaused = false;
	private int mMyCredit;
	private int mMyPoint;
	private int mOtherPoint;
	private List<Answer> mAnswers;
	private BaiduSpeechDialog mBaiduSpeechDialog;
	private PausedDialog mPausedDialog;
	private QuitLBSEventDialog mQuitDialog;
	private WebViewDialog mSearchResultDialog;
	private LoadingDialog mLoadingDialog;

	private Handler mMainHandler;

	private int mEventType = Intents.EVENT_TYPE_DAILY;
	private String mEventKey;
	private FightStateReceiver mFightStateReceiver;
	private long mStartTime;

	private static final int CREDIT_INTERVAL = 10;
	private static final int POINT_INTERVAL = 5;

	@Override
	protected void onCreate(Bundle arg0) {

		setContentView(R.layout.question);

		mMatchName = (TextView) findViewById(R.id.match_name);
		mPointBoard = (TextView) findViewById(R.id.credit_and_point);
		mPauseResume = (Button) findViewById(R.id.pause_resume);
		mQuit = (Button) findViewById(R.id.quit);
		mQuestionOrder = (TextView) findViewById(R.id.question_order);
		mQuestionTitle = (TextView) findViewById(R.id.title);
		mAnswerTime = (ProgressBar) findViewById(R.id.time);
		mHelp = (Button) findViewById(R.id.help);
		mOptionA = (Button) findViewById(R.id.option_a);
		mOptionB = (Button) findViewById(R.id.option_b);
		mOptionC = (Button) findViewById(R.id.option_c);
		mOptionD = (Button) findViewById(R.id.option_d);
		mTimeout = (ImageView) findViewById(R.id.timeout);

		mPauseResume.setOnClickListener(this);
		mOptionA.setOnClickListener(this);
		mOptionB.setOnClickListener(this);
		mOptionC.setOnClickListener(this);
		mOptionD.setOnClickListener(this);
		mHelp.setOnClickListener(this);
		mQuit.setOnClickListener(this);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				SouShangApplication.FONT);
		mMatchName.setTypeface(typeface);
		mPointBoard.setTypeface(typeface);
		mQuestionOrder.setTypeface(typeface);
		mQuestionTitle.setTypeface(typeface);
		mOptionA.setTypeface(typeface);
		mOptionB.setTypeface(typeface);
		mOptionC.setTypeface(typeface);
		mOptionD.setTypeface(typeface);

		mAnswerResults[0][0] = (ImageView) findViewById(R.id.a_correct);
		mAnswerResults[0][1] = (ImageView) findViewById(R.id.a_incorrect);
		mAnswerResults[1][0] = (ImageView) findViewById(R.id.b_correct);
		mAnswerResults[1][1] = (ImageView) findViewById(R.id.b_incorrect);
		mAnswerResults[2][0] = (ImageView) findViewById(R.id.c_correct);
		mAnswerResults[2][1] = (ImageView) findViewById(R.id.c_incorrect);
		mAnswerResults[3][0] = (ImageView) findViewById(R.id.d_correct);
		mAnswerResults[3][1] = (ImageView) findViewById(R.id.d_incorrect);

		mMainHandler = new Handler(getMainLooper());
		mPausedDialog = new PausedDialog(this);
		mPausedDialog.setOnClickListener(new PausedDialog.OnClickListener() {

			@Override
			public void onResume() {
				resume();
			}

			@Override
			public void onHome() {
				finish();
			}
		});

		mQuitDialog = new QuitLBSEventDialog(this);
		mQuitDialog
				.setOnClickListener(new QuitLBSEventDialog.OnClickListener() {

					@Override
					public void onQuit() {
						Intent intent = new Intent(QuestionActivity.this,
								LBSService.class);
						intent.setAction(Intents.ACTION_FIGHT_QUIT);
						QuestionActivity.this.startService(intent);

						finish();
					}

					@Override
					public void onCancel() {
						resume();
					}
				});

		mAnswers = new ArrayList<Answer>();

		mSearchResultDialog = new WebViewDialog(this);
		mSearchResultDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						resume();
					}
				});
		mSearchResultDialog
				.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						resume();
					}
				});
		mLoadingDialog = new LoadingDialog(this);

		mTimeout.setVisibility(View.GONE);
		mAnswerTime.setMax(10);
		mAnswerTime.setProgress(10);

		initBaiduSpeechDialog();

		mLoadingDialog.show(getResources().getString(R.string.get_question));

		if (getIntent() != null) {
			mEventType = getIntent().getIntExtra(Intents.EXTRA_EVENT_TYPE,
					Intents.EVENT_TYPE_DAILY);

			if (mEventType == Intents.EVENT_TYPE_LBS) {
				mEventKey = getIntent().getStringExtra(Intents.EXTRA_FIGHT_KEY);
			} else if (mEventType == Intents.EVENT_TYPE_FEATURE) {
				mMatchName.setText(Variables.feBean.getTitle());
				mEventKey = Variables.feBean.getId() + "";
			}

			IntentFilter filter = new IntentFilter(Intents.ACTION_FIGHTING);
			filter.addAction(Intents.ACTION_FIGHT_END);
			mFightStateReceiver = new FightStateReceiver();
			registerReceiver(mFightStateReceiver, filter);
		}

		mStartTime = System.currentTimeMillis();
		updatePointBoard();
		mMainHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getQuestion();
			}
		}, 1000);

		super.onCreate(arg0);

		if (mEventType == Intents.EVENT_TYPE_DAILY
				|| mEventType == Intents.EVENT_TYPE_FEATURE) {
			mPauseResume.setVisibility(View.VISIBLE);
			mQuit.setVisibility(View.GONE);
		} else if (mEventType == Intents.EVENT_TYPE_LBS) {
			mPauseResume.setVisibility(View.GONE);
			mQuit.setVisibility(View.VISIBLE);
		}
	}

	private void initBaiduSpeechDialog() {
		mBaiduSpeechDialog = new BaiduSpeechDialog(QuestionActivity.this);
		mBaiduSpeechDialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				pause();
			}
		});
		mBaiduSpeechDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				resume();
			}
		});
		mBaiduSpeechDialog
				.setDialogRecognitionListener(new DialogRecognitionListener() {

					@Override
					public void onResults(Bundle arg0) {
						if (arg0 != null) {
							ArrayList<String> results = arg0
									.getStringArrayList(BaiduSpeechDialog.RESULTS_RECOGNITION);
							if (results != null && results.size() > 0) {
								try {
									JSONObject result = new JSONObject(results
											.get(0));
									if (result.has("command_str")) {
										String commandStr = result
												.getString("command_str");
										if (!TextUtils.isEmpty(commandStr)) {
											JSONObject commandObject = new JSONObject(
													commandStr);
											String handleText = commandObject
													.getString("handle_text");
											mSearchResultDialog.show(
													handleText,
													"http://m.baidu.com/s?word="
															+ handleText);
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
		params.putString(BaiduSpeechDialog.PORMPT_TEXT, getResources()
				.getString(R.string.sa_help));
		mBaiduSpeechDialog.setParams(
				BaiduSpeechDialog.SpeechMode.VOICE_TO_COMMAND, null);
	}

	private void getQuestion() {
		Apis.getNextQuestion(this, mEventType, mEventKey,
				mCurrentQuestion == null ? 0 : mCurrentQuestion.getId(),
				Config.getAccessToken(this), this);
	}

	@Override
	public void onResults(QuestionResponse arg0) {
		if (arg0 != null) {
			if (arg0.getRetCode() == 0) {
				mCurrentQuestion = arg0.getQuestion();

				if (mCurrentQuestion != null) {
					updateUI(mCurrentQuestion);
					mMainHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							mLoadingDialog.dismiss();
						}
					}, 100);
				} else {
					Log.d("Question", "get next question is null");
					mLoadingDialog.dismiss();
					showEventComplated();
				}
			} else {
				Log.d("Question", "get next question error");
				mCurrentQuestion = null;
				mLoadingDialog.dismiss();
				showEventComplated();
			}
		} else {
			mLoadingDialog.dismiss();
			Toast.makeText(this,
					getResources().getString(R.string.get_question_failed),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void showEventComplated() {
		if (mEventType == Intents.EVENT_TYPE_DAILY) {
			Variables.daliyFeatureFlag=0;
			Intent intent = new Intent(QuestionActivity.this,
					DailyEventCompletedActivity.class);
			intent.putExtra(Intents.EXTRA_POINT, mMyPoint);

			if (!Config.isLogged(QuestionActivity.this)) {
				SouShangApplication application = (SouShangApplication) getApplication();
				application.setAnswers(mAnswers);
			}

			startActivity(intent);
		} else if (mEventType == Intents.EVENT_TYPE_LBS) {
			Intent intent = new Intent(QuestionActivity.this,
					LBSEventCompletedActivity.class);
			intent.setAction(Intents.ACTION_LBS_WAIT);
			intent.putExtra(Intents.EXTRA_MY_POINT, mMyPoint);
			intent.putExtra(Intents.EXTRA_MY_TIME,
					(int) (System.currentTimeMillis() - mStartTime) / 1000);

			startActivity(intent);
		} else if (mEventType == Intents.EVENT_TYPE_FEATURE) {
			Variables.daliyFeatureFlag=1;
			Intent intent = new Intent(QuestionActivity.this,
					DailyEventCompletedActivity.class);
			intent.putExtra(Intents.EXTRA_POINT, mMyPoint);
			startActivity(intent);
		}

		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	private void showLBSEventCompleted(boolean win, int myPoint, int myTime,
			int otherPoint, int otherTime, int eventPoint, int winRate) {
		Intent intent = new Intent(QuestionActivity.this,
				LBSEventCompletedActivity.class);

		intent.setAction(Intents.ACTION_LBS_RESULT);
		intent.putExtra(Intents.EXTRA_WIN, win);
		intent.putExtra(Intents.EXTRA_MY_POINT, myPoint);
		intent.putExtra(Intents.EXTRA_MY_TIME, myTime);
		intent.putExtra(Intents.EXTRA_OTHER_POINT, otherPoint);
		intent.putExtra(Intents.EXTRA_OTHER_TIME, otherTime);
		intent.putExtra(Intents.EXTRA_MY_POINT_DELTA, eventPoint);
		intent.putExtra(Intents.EXTRA_MY_WIN_RATE, winRate);

		startActivity(intent);

		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	private void updateUI(QuestionResponse.Question question) {
		if (mEventType == Intents.EVENT_TYPE_DAILY) {
			mMatchName.setText(question.getEventTitle());
		} else if (mEventType == Intents.EVENT_TYPE_LBS) {
			mMatchName.setText(getResources().getString(R.string.lbs_event));
		} else if (mEventType == Intents.EVENT_TYPE_FEATURE) {
			mMatchName.setText(Variables.feBean.getTitle());
		}

		mQuestionOrder.setText(String.format(
				getResources().getString(R.string.question_order),
				question.getIndex() + 1, question.getTotal()));
		mQuestionTitle.setText(Html.fromHtml(question.getTitle()));

		mOptionA.setText(Html.fromHtml(normalizeOption(question.getOptions()
				.get(0))));
		mOptionB.setText(Html.fromHtml(normalizeOption(question.getOptions()
				.get(1))));
		mOptionC.setText(Html.fromHtml(normalizeOption(question.getOptions()
				.get(2))));
		mOptionD.setText(Html.fromHtml(normalizeOption(question.getOptions()
				.get(3))));

		mTimeout.setVisibility(View.INVISIBLE);

		mAnswerTime.setMax(question.getAnswerTime() * 10);
		mAnswerTime.setProgress(question.getAnswerTime() * 10);

		clearAnswerResult();

		resume();
	}

	private String normalizeOption(String option) {
		String normalizedOption = option;

		if (!TextUtils.isEmpty(normalizedOption)) {
			if (normalizedOption.startsWith("A.")
					|| normalizedOption.startsWith("a.")
					|| normalizedOption.startsWith("B.")
					|| normalizedOption.startsWith("b.")
					|| normalizedOption.startsWith("C.")
					|| normalizedOption.startsWith("c.")
					|| normalizedOption.startsWith("D.")
					|| normalizedOption.startsWith("d.")) {
				normalizedOption = normalizedOption.substring(2);
			}
		}

		return normalizedOption;
	}

	private void updatePointBoard() {
		if (mEventType == Intents.EVENT_TYPE_DAILY) {
			mPointBoard.setText(String.format(
					getResources().getString(R.string.credit_and_point),
					mMyCredit, mMyPoint));
		} else if (mEventType == Intents.EVENT_TYPE_LBS) {
			mPointBoard.setText(String.format(
					getResources().getString(R.string.fight_state), mMyPoint,
					mOtherPoint));
		} else if (mEventType == Intents.EVENT_TYPE_FEATURE) {
			mPointBoard.setText(String.format(
					getResources().getString(R.string.credit_and_point),
					mMyPoint, mOtherPoint));
		}
	}

	private void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
		}
	}

	private void startTimer() {
		stopTimer();
		mTimer = new AnswerTimer(mAnswerTime, mAnswerTime.getProgress() * 100,
				100);
		mTimer.start();
	}

	private void pause() {
		stopTimer();
		mPauseResume.setBackgroundResource(R.drawable.resume);
		mPaused = true;
	}

	private void resume() {
		startTimer();
		mPauseResume.setBackgroundResource(R.drawable.pause);
		mPaused = false;
	}

	private void answer(int index) {
		boolean finished = false;
		boolean correct = false;
		if (index == mCurrentQuestion.getRightAnswer()) {
			correct = true;

			mMyCredit += CREDIT_INTERVAL;
			mMyPoint += POINT_INTERVAL;

			updatePointBoard();
		}
		showAnswerResult(index, correct);
		
		Answer answer = new Answer();
		answer.setId(mCurrentQuestion.getId());
		answer.setAnswer(index);
		if (mEventType == Intents.EVENT_TYPE_DAILY) {

			if (Config.isLogged(this)) {
				List<Answer> answers = new ArrayList<Answer>();
				answers.add(answer);
				Apis.answer(this, answers,
						Config.getAccessToken(QuestionActivity.this), Intents.EVENT_TYPE_DAILY, null);
			} else {
				mAnswers.add(answer);
			}
		} else if (mEventType == Intents.EVENT_TYPE_LBS) {
			answerForLBS(mCurrentQuestion.getIndex(), correct);
		} else if (mEventType == Intents.EVENT_TYPE_FEATURE) {
			List<Answer> answers = new ArrayList<Answer>();
			answers.add(answer);
			System.out.println(""+answer.getId()+""+answer.getAnswer());
			Apis.answer(this, answers,
					Config.getAccessToken(QuestionActivity.this), Intents.EVENT_TYPE_FEATURE, null);
		}

		finished = mCurrentQuestion.getTotal() == (mCurrentQuestion.getIndex() + 1);

		stopTimer();
		if (finished) {
			mMainHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mLoadingDialog.show(getResources().getString(
							R.string.complete_answer));
				}
			}, 300);
		} else {
			mMainHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mLoadingDialog.show(getResources().getString(
							R.string.get_question));
				}
			}, 300);
		}

		mMainHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getQuestion();
			}
		}, 1000);
	}

	private void answerForLBS(int index, boolean right) {
		Intent intent = new Intent(this, LBSService.class);
		intent.setAction(Intents.ACTION_ANSWER);
		intent.putExtra(Intents.EXTRA_INDEX, index);
		intent.putExtra(Intents.EXTRA_RIGHT, right ? 1 : 0);
		startService(intent);
	}

	private void showAnswerResult(int index, boolean correct) {
		if (index < 0 || index > 3) {
			return;
		}

		int xIndex = index;
		int yIndex = correct ? 0 : 1;

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 2; y++) {
				if (x == xIndex && y == yIndex) {
					mAnswerResults[x][y].setVisibility(View.VISIBLE);
				} else {
					mAnswerResults[x][y].setVisibility(View.GONE);
				}
			}
		}
	}

	private void clearAnswerResult() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 2; y++) {
				mAnswerResults[x][y].setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onError(Throwable arg0) {
		mLoadingDialog.dismiss();
	}

	@Override
	protected void onDestroy() {
		if (mEventType == Intents.EVENT_TYPE_LBS
				|| mEventType == Intents.EVENT_TYPE_FEATURE
				&& mFightStateReceiver != null) {
			unregisterReceiver(mFightStateReceiver);
		}
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		pause();
	}

	@Override
	public void onBackPressed() {
		pause();
		if (mEventType == Intents.EVENT_TYPE_DAILY
				|| mEventType == Intents.EVENT_TYPE_FEATURE) {
			mPausedDialog.show();
		} else if (mEventType == Intents.EVENT_TYPE_LBS) {
			mQuitDialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mPauseResume) {
			if (mPaused) {
				resume();
			} else {
				pause();
				mPausedDialog.show();
			}
		} else if (v == mOptionA) {
			answer(0);
		} else if (v == mOptionB) {
			answer(1);
		} else if (v == mOptionC) {
			answer(2);
		} else if (v == mOptionD) {
			answer(3);
		} else if (v == mHelp) {
			mBaiduSpeechDialog.show();
		} else if (v == mQuit) {
			pause();
			mQuitDialog.show();
		}
	}

}
