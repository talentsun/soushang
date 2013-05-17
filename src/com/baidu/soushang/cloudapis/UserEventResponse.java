package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;

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
}
