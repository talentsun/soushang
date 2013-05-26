package com.baidu.soushang.cloudapis;

import java.util.List;

import com.baidu.soushang.Config;
import com.baidu.soushang.cloudapis.AnswerRequest.Answer;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import it.restrung.rest.client.ContextAwareAPIDelegate;
import it.restrung.rest.client.RestClientFactory;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class Apis {
  private static final String BASE_URL = "http://sou.baidu.com/";
  private static final String QUESTION_URL = BASE_URL + "Devent/next/s/%d.html?udid=%s";
  private static final String QUESTION_URL_LOGGED = BASE_URL + "Devent/next/s/%d.html?access_token=%s";
  private static final String LOGIN_URL = BASE_URL + "Oauth/login.html?access_token=%s";
  private static final String ANSWER_URL = BASE_URL + "Devent/answer.html";
  private static final String USERINFO_URL = BASE_URL + "Devent/userinfo.html?access_token=%s";
  private static final String USERRANK_URL = BASE_URL + "Devent/userrank.html";
  private static final String DAYEVENT_URL = BASE_URL + "Devent/dayevent.html?access_token=%s";
  private static final String SHARE_URL = BASE_URL + "Devent/share_add_point.html?access_token=%s";
  
  public interface ApiResponseCallback<T extends AbstractJSONResponse> {
    public void onResults(T arg0);

    public void onError(Throwable arg0);
  }
  
  public static void share(Context context, String accessToken, final ApiResponseCallback<CommonResponse> callback) {
    RestClientFactory.getClient().getAsync(
      new ContextAwareAPIDelegate<CommonResponse>(context, CommonResponse.class) {

        @Override
        public void onError(Throwable arg0) {
          if (callback != null) {
            callback.onError(arg0);
          }
        }

        @Override
        public void onResults(CommonResponse arg0) {
          if (callback != null) {
            callback.onResults(arg0);
          }
        }

      }, String.format(SHARE_URL, accessToken), 2 * 1000);
  }

  public static void getNextQuestion(Context context, int questionId, String accessToken, 
      final ApiResponseCallback<QuestionResponse> callback) {
    String url = TextUtils.isEmpty(accessToken) ? String.format(QUESTION_URL, questionId, Config.getUDID(context)) : String.format(QUESTION_URL_LOGGED, questionId, accessToken);
    RestClientFactory.getClient().getAsync(
        new ContextAwareAPIDelegate<QuestionResponse>(context, QuestionResponse.class) {

          @Override
          public void onError(Throwable arg0) {
            if (callback != null) {
              callback.onError(arg0);
            }
          }

          @Override
          public void onResults(QuestionResponse arg0) {
            if (callback != null) {
              callback.onResults(arg0);
            }
          }
        }, url, 2 * 1000);
  }
  
  public static void Login(Context context, String accessToken,
      final ApiResponseCallback<CommonResponse> callback) {
    RestClientFactory.getClient().getAsync(
      new ContextAwareAPIDelegate<CommonResponse>(context, CommonResponse.class) {
        @Override
        public void onError(Throwable arg0) {
          if (callback != null) {
            callback.onError(arg0);
          }
        }

        @Override
        public void onResults(CommonResponse arg0) {
          if (callback != null) {
            callback.onResults(arg0);
          }
        }
      }, String.format(LOGIN_URL, accessToken), 2 * 1000);
  }
  
  public static void getUserInfo(Context context, String accessToken,
      final ApiResponseCallback<UserInfoResponse> callback) {
    RestClientFactory.getClient().getAsync(new ContextAwareAPIDelegate<UserInfoResponse>(context, UserInfoResponse.class) {

      @Override
      public void onError(Throwable arg0) {
        if (callback != null) {
          callback.onError(arg0);
        }
      }

      @Override
      public void onResults(UserInfoResponse arg0) {
        if (callback != null) {
          callback.onResults(arg0);
        }
      }
      
    }, String.format(USERINFO_URL, accessToken), 2 * 1000);
  }
  
  public static void getUserRank(Context context,
      final ApiResponseCallback<UserRankResponse> callback) {
    RestClientFactory.getClient().getAsync(new ContextAwareAPIDelegate<UserRankResponse>(context, UserRankResponse.class) {

      @Override
      public void onError(Throwable arg0) {
        if (callback != null) {
          callback.onError(arg0);
        }
      }

      @Override
      public void onResults(UserRankResponse arg0) {
        if (callback != null) {
          callback.onResults(arg0);
        }
      }
      
    }, USERRANK_URL, 2 * 1000);
  }
  
  @SuppressWarnings("deprecation")
  public static void answer(Context context, List<Answer> answers, String accessToken,
      final ApiResponseCallback<CommonResponse> callback) {
    AnswerRequest request = new AnswerRequest();
    request.setAnswers(answers);
    request.setAccessToken(accessToken);
    
    RestClientFactory.getClient().postAsync(new ContextAwareAPIDelegate<CommonResponse>(context, CommonResponse.class) {

      @Override
      public void onError(Throwable arg0) {
        if (callback != null) {
          callback.onError(arg0);
        }
        
        Log.i("answer", arg0.toString());
      }

      @Override
      public void onResults(CommonResponse arg0) {
        if (callback != null) {
          callback.onResults(arg0);
        }
      }

    }, ANSWER_URL, request);
  }
  
  public static void getDayEvent(Context context, String accessToken,
      final ApiResponseCallback<DayEventResponse> callback) {
    RestClientFactory.getClient().getAsync(
        new ContextAwareAPIDelegate<DayEventResponse>(context, DayEventResponse.class) {

          @Override
          public void onError(Throwable arg0) {
            if (callback != null) {
              callback.onError(arg0);
            }
          }

          @Override
          public void onResults(DayEventResponse arg0) {
            if (callback != null) {
              callback.onResults(arg0);
            }
          }

        }, String.format(DAYEVENT_URL, accessToken), 2 * 1000);
  }
}
