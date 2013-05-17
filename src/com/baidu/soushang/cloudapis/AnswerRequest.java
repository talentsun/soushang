package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.request.AbstractJSONRequest;

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
}
