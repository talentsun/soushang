package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;

public class Answer {
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
