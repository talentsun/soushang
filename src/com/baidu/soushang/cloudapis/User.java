package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class User extends AbstractJSONResponse {
  private static final long serialVersionUID = 1L;
  
  @JsonProperty(value="user_id")
  private long userId;
  
  @JsonProperty(value="username")
  private String username;
  
  @JsonProperty(value="point")
  private int point;
  
  @JsonProperty(value="integral")
  private int integral;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getPoint() {
    return point;
  }

  public void setPoint(int point) {
    this.point = point;
  }

  public int getIntegral() {
    return integral;
  }

  public void setIntegral(int integral) {
    this.integral = integral;
  }
  
  public User() {
    
  }
}
