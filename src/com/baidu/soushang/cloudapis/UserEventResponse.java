package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

import java.util.List;

public class UserEventResponse extends CommonResponse {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value="answers")
  private List<Answer> answers;

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }
  
  public UserEventResponse() {
    
  }
  
  public static class Answer extends AbstractJSONResponse {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value="id")
    private long id;
    
    @JsonProperty(value="ans")
    private int answer;

    public long getId() {
      return id;
    }

    public void setId(long id) {
      this.id = id;
    }

    public int getAnswer() {
      return answer;
    }

    public void setAnswer(int answer) {
      this.answer = answer;
    }
    
    public Answer() {
      
    }
  }
}
