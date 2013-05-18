package com.baidu.soushang.cloudapis;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.soushang.cloudapis.AnswerRequest.Answer;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import it.restrung.rest.client.ContextAwareAPIDelegate;
import it.restrung.rest.client.RestClientFactory;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class Apis {
  private static final String QUESTION_URL = "http://soushang.limijiaoyin.com/index.php/Devent/next/s/%d.html";
  private static final String QUESTION_URL_LOGGED = "http://soushang.limijiaoyin.com/index.php/Devent/next/s/%d.html?access_token=%s";
  private static final String LOGIN_URL = "http://soushang.limijiaoyin.com/index.php/Oauth/login.html?access_token=%s";
  private static final String ANSWER_URL = "http://soushang.limijiaoyin.com/index.php/Devent/answer.html?answers=%s&access_token=%s";
  private static final String USERINFO_URL = "http://soushang.limijiaoyin.com/index.php/Devent/userinfo.html?access_token=%s";
  private static final String USEREVENT_URL = "http://soushang.limijiaoyin.com/index.php/Devent/userevent.html?event_id=%d&access_token=%s";
  private static final String USERRANK_URL = "http://soushang.limijiaoyin.com/index.php/Devent/userrank.html";
  
  public interface ApiResponseCallback<T extends AbstractJSONResponse> {
    public void onResults(T arg0);

    public void onError(Throwable arg0);
  }

  public static void getNextQuestion(Context context, int questionId, String accessToken, 
      final ApiResponseCallback<QuestionResponse> callback) {
    String url = TextUtils.isEmpty(accessToken) ? String.format(QUESTION_URL, questionId) : String.format(QUESTION_URL_LOGGED, questionId, accessToken);
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
  
  public static void getUserEvent(Context context, long eventId, String accessToken,
      final ApiResponseCallback<UserEventResponse> callback) {
    RestClientFactory.getClient().getAsync(
      new ContextAwareAPIDelegate<UserEventResponse>(context, UserEventResponse.class) {

        @Override
        public void onError(Throwable arg0) {
          if (callback != null) {
            callback.onError(arg0);
          }
        }

        @Override
        public void onResults(UserEventResponse arg0) {
          if (callback != null) {
            callback.onResults(arg0);
          }
        }
      
    }, String.format(USEREVENT_URL, eventId, accessToken), 2 * 1000);
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
  
  public static void answer(Context context, List<Answer> answers, String accessToken,
      final ApiResponseCallback<CommonResponse> callback) {
//    AnswerRequest request = new AnswerRequest();
//    request.setAnswers(answers);
    
    JSONArray jsonArray = new JSONArray();
    try {
      for (Answer answer : answers) {
        jsonArray.put(new JSONObject(answer.toJSON()));
      }
    } catch (Exception e) {
    }
    String json = jsonArray.toString();
    Log.i("answer", json);
    
    RestClientFactory.getClient().getAsync(new ContextAwareAPIDelegate<CommonResponse>(context, CommonResponse.class) {

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
      
    }, String.format(ANSWER_URL, json, accessToken), 2 * 1000);
  }
}
