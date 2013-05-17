package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.request.AbstractJSONRequest;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class AnswerRequest extends AbstractJSONRequest {
  @JsonProperty(value="answers")
  private List<Answer> answers;
  
  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }

  public AnswerRequest() {
    
  }
  
  public static class Answer extends AbstractJSONRequest {
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
