package com.baidu.soushang.cloudapis;

import java.util.List;

import android.content.Context;
import it.restrung.rest.client.ContextAwareAPIDelegate;
import it.restrung.rest.client.RestClientFactory;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class Apis {
  private static final String QUESTION_URL = "http://soushang.limijiaoyin.com/index.php/Devent/next/s/%d.html";
  private static final String LOGIN_URL = "http://soushang.limijiaoyin.com/index.php/Oauth/login.html";
  private static final String ANSWER_URL = "http://soushang.limijiaoyin.com/index.php/Devent/answer.html";
  private static final String USERINFO_URL = "http://soushang.limijiaoyin.com/index.php/Devent/userinfo.html";
  private static final String USEREVENT_URL = "http://soushang.limijiaoyin.com/index.php/Devent/userevent.html?event_id=%d";
  private static final String USERRANK_URL = "http://soushang.limijiaoyin.com/index.php/Devent/userrank.html";
  
  public interface ApiResponseCallback<T extends AbstractJSONResponse> {
    public void onResults(T arg0);

    public void onError(Throwable arg0);
  }

  public static void getNextQuestion(Context context, int questionId,
      final ApiResponseCallback<QuestionResponse> callback) {
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
        }, String.format(QUESTION_URL, questionId), 2 * 1000);
  }
  
  public static void Login(Context context, String accessToken,
      final ApiResponseCallback<CommonResponse> callback) {
    LoginRequest request = new LoginRequest();
    request.setAccessToken(accessToken);
    
    RestClientFactory.getClient().postAsync(
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
      }, LOGIN_URL, request, 2 * 1000);
  }
  
  public static void getUserInfo(Context context,
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
      
    }, USERINFO_URL, 2 * 1000);
  }
  
  public static void getUserEvent(Context context, long eventId,
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
      
    }, String.format(USEREVENT_URL, eventId), 2 * 1000);
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
  
  public static void answer(Context context, List<Answer> answers, 
      final ApiResponseCallback<CommonResponse> callback) {
    AnswerRequest request = new AnswerRequest();
    request.setAnswers(answers);
    
    RestClientFactory.getClient().postAsync(new ContextAwareAPIDelegate<CommonResponse>(context, CommonResponse.class) {

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
      
    }, ANSWER_URL, request);
  }
}
