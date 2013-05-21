package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
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
import com.baidu.soushang.cloudapis.AnswerRequest.Answer;
import com.baidu.soushang.cloudapis.Apis;
import com.baidu.soushang.cloudapis.Apis.ApiResponseCallback;
import com.baidu.soushang.cloudapis.QuestionResponse;
import com.baidu.soushang.cloudapis.UserInfoResponse;
import com.baidu.soushang.widgets.PausedDialog;
import com.baidu.soushang.widgets.WebViewDialog;

public class QuestionActivity extends FragmentActivity implements ApiResponseCallback<QuestionResponse>, OnClickListener {
  class AnswerTimer extends CountDownTimer {
    private ProgressBar mProgressBar;

    public AnswerTimer(ProgressBar progressBar, long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
      mProgressBar = progressBar;
    }

    @Override
    public void onFinish() {
      mProgressBar.setProgress(0);
      mTimeout.setVisibility(View.VISIBLE);
      
      mMainHandler.postDelayed(new Runnable() {
        
        @Override
        public void run() {
          getQuestion();
        }
      }, 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
      Log.i("time left", "" + millisUntilFinished);
      int progress = (int) (millisUntilFinished / 100);
      mProgressBar.setProgress(progress);
      if (progress <= 50) {
        mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.answer_timer_red));
      } else {
        mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.answer_timer));
      }
    }

  }
  
  private TextView mMatchName;
  private TextView mCreditAndPoint;
  private Button mPauseAndResume;
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
  private int mCredit;
  private int mPoint;
  private List<Answer> mAnswers;
  private BaiduSpeechDialog mBaiduSpeechDialog;
  private PausedDialog mPausedDialog;
  private WebViewDialog mSearchResultDialog;
  
  private Handler mMainHandler;
  
  private static final int CREDIT_INTERVAL = 10;
  private static final int POINT_INTERVAL = 20;

  ApiResponseCallback<UserInfoResponse> mUserInfoCallback = new ApiResponseCallback<UserInfoResponse>() {
    
    @Override
    public void onResults(UserInfoResponse arg0) {
      if (arg0.getRetCode() == 0 && arg0.getUser() != null) {
        updateUserInfo(arg0.getUser().getIntegral(), arg0.getUser().getPoint());
      } else {
        updateUserInfo(0, 0);
      }
    }
    
    @Override
    public void onError(Throwable arg0) {
      updateUserInfo(0, 0);
    }
  };
  
  @Override
  protected void onCreate(Bundle arg0) {

    setContentView(R.layout.question);

    mMatchName = (TextView) findViewById(R.id.match_name);
    mCreditAndPoint = (TextView) findViewById(R.id.credit_and_point);
    mPauseAndResume = (Button) findViewById(R.id.pause_resume);
    mQuestionOrder = (TextView) findViewById(R.id.question_order);
    mQuestionTitle = (TextView) findViewById(R.id.title);
    mAnswerTime = (ProgressBar) findViewById(R.id.time);
    mHelp = (Button) findViewById(R.id.help);
    mOptionA = (Button) findViewById(R.id.option_a);
    mOptionB = (Button) findViewById(R.id.option_b);
    mOptionC = (Button) findViewById(R.id.option_c);
    mOptionD = (Button) findViewById(R.id.option_d);
    mTimeout = (ImageView) findViewById(R.id.timeout);
    
    mPauseAndResume.setOnClickListener(this);
    mOptionA.setOnClickListener(this);
    mOptionB.setOnClickListener(this);
    mOptionC.setOnClickListener(this);
    mOptionD.setOnClickListener(this);
    mHelp.setOnClickListener(this);
    
    Typeface typeface = Typeface.createFromAsset(getAssets(), SouShangApplication.FONT);
    mMatchName.setTypeface(typeface);
    mCreditAndPoint.setTypeface(typeface);
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
    
    if (!Config.isLogged(this)) {
      updateUserInfo(mCredit, mPoint);
      mAnswers = new ArrayList<Answer>();
    }
    
    mSearchResultDialog = new WebViewDialog(this);
    mSearchResultDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      
      @Override
      public void onCancel(DialogInterface dialog) {
        resume();
      }
    });
    mSearchResultDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      
      @Override
      public void onDismiss(DialogInterface dialog) {
        resume();
      }
    });
    
    initBaiduSpeechDialog();
    
    getQuestion();

    super.onCreate(arg0);
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
    mBaiduSpeechDialog.setDialogRecognitionListener(new DialogRecognitionListener() {

      @Override
      public void onResults(Bundle arg0) {
        if (arg0 != null) {
          ArrayList<String> results =
              arg0.getStringArrayList(BaiduSpeechDialog.RESULTS_RECOGNITION);
          if (results != null && results.size() > 0) {
            try {
              JSONObject result = new JSONObject(results.get(0));
              if (result.has("command_str")) {
                String commandStr = result.getString("command_str");
                if (!TextUtils.isEmpty(commandStr)) {
                  JSONObject commandObject = new JSONObject(commandStr);
                  String handleText = commandObject.getString("handle_text");
                  mSearchResultDialog.show(handleText, "http://m.baidu.com/s?word=" + handleText);
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
  }
  
  private void getQuestion() {
    Apis.getNextQuestion(this, mCurrentQuestion == null ? 0 : mCurrentQuestion.getId(), Config.getAccessToken(this), this);
    
    if (Config.isLogged(this)) {
      Apis.getUserInfo(this, Config.getAccessToken(this), mUserInfoCallback);
    }
  }
  
  @Override
  public void onResults(QuestionResponse arg0) {
    if (arg0 != null) {
      if (arg0.getRetCode() == 0) {
        mCurrentQuestion = arg0.getQuestion();
        
        if (mCurrentQuestion != null) {
          updateUI(mCurrentQuestion);
        } else {
          showEventComplated();
        }
      } else {
        mCurrentQuestion = null;
        
        showEventComplated();
      }
    } else {
      Toast.makeText(this, getResources().getString(R.string.get_question_failed), Toast.LENGTH_SHORT).show();
    }
  }

  private void showEventComplated() {
    Intent intent = new Intent(QuestionActivity.this, EventCompletedActivity.class);
    
    intent.putExtra(Intents.EXTRA_CREDIT, mCredit);
    intent.putExtra(Intents.EXTRA_POINT, mPoint);
    
    if (!Config.isLogged(QuestionActivity.this)) {
      SouShangApplication application = (SouShangApplication) getApplication();
      application.setAnswers(mAnswers);
    }
    
    startActivity(intent);
    finish();
  }
  
  private void updateUI(QuestionResponse.Question question) {
    mMatchName.setText(question.getEventTitle());
    mQuestionOrder.setText(String.format(getResources().getString(R.string.question_order), question.getIndex()+1, question.getTotal()));
    mQuestionTitle.setText(question.getTitle());
    
    mOptionA.setText(question.getOptions().get(0));
    mOptionB.setText(question.getOptions().get(1));
    mOptionC.setText(question.getOptions().get(2));
    mOptionD.setText(question.getOptions().get(3));
    
    mTimeout.setVisibility(View.INVISIBLE);
    
    mAnswerTime.setMax(question.getAnswerTime() * 10);
    mAnswerTime.setProgress(question.getAnswerTime() * 10);
    
    clearAnswerResult();
    
    resume();
  }
  
  private void updateUserInfo(int credit, int point) {
    mCreditAndPoint.setText(String.format(getResources().getString(R.string.credit_and_point), credit, point));
  }
  
  private void stopTimer() {
    if (mTimer != null) {
      mTimer.cancel();
    }
  }
  
  private void startTimer() {
    stopTimer();
    mTimer = new AnswerTimer(mAnswerTime, mAnswerTime.getProgress() * 100, 100);
    mTimer.start();
  }
  
  private void pause() {
    stopTimer();
    mPauseAndResume.setBackgroundResource(R.drawable.resume);
    mPaused = true;
  }
  
  private void resume() {
    startTimer();
    mPauseAndResume.setBackgroundResource(R.drawable.pause);
    mPaused = false;
  }

  private void answer(int index) {
    if (mCurrentQuestion != null) {
      if (index == mCurrentQuestion.getRightAnswer()) {
        showAnswerResult(index, true);
        
        mCredit += CREDIT_INTERVAL;
        mPoint += POINT_INTERVAL;
        
        if (!Config.isLogged(this)) {
          updateUserInfo(mCredit, mPoint);
        }
      } else {
        showAnswerResult(index, false);
      }

      Answer answer = new Answer();
      answer.setId(mCurrentQuestion.getId());
      answer.setAnswer(index);
      
      if (Config.isLogged(this)) {
        List<Answer> answers = new ArrayList<Answer>();
        answers.add(answer);
        Apis.answer(this, answers, Config.getAccessToken(QuestionActivity.this), null);
      } else {
        mAnswers.add(answer);
      }
    } else {
      showAnswerResult(index, false);
    }

    mMainHandler.postDelayed(new Runnable() {
      
      @Override
      public void run() {
        getQuestion();
      }
    }, 500);
  }
  
  private void showAnswerResult(int index, boolean correct) {
    int xIndex = index;
    int yIndex = correct ? 0 : 1;
    
    for (int x=0; x<4; x++) {
      for (int y=0; y<2; y++) {
        if (x == xIndex && y == yIndex) {
          mAnswerResults[x][y].setVisibility(View.VISIBLE);
        } else {
          mAnswerResults[x][y].setVisibility(View.GONE);
        }
      }
    }
  }
  
  private void clearAnswerResult() {
    for (int x=0; x<4; x++) {
      for (int y=0; y<2; y++) {
        mAnswerResults[x][y].setVisibility(View.GONE);
      }
    }
  }

  @Override
  public void onError(Throwable arg0) {
    
  }

  @Override
  protected void onDestroy() {
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
    mPausedDialog.show();
  }

  @Override
  public void onClick(View v) {
    if (v == mPauseAndResume) {
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
    }
  }

}
