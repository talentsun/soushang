package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;

public class UserInfoResponse extends CommonResponse {
  private static final long serialVersionUID = 1L;
  
  @JsonProperty(value="user")
  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  public UserInfoResponse() {
    
  }

}
