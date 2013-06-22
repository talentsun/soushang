package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.request.AbstractJSONRequest;

public class LoginRequest extends AbstractJSONRequest {
  @JsonProperty(value="access_token")
  private String access_token;

  public String getAccessToken() {
    return access_token;
  }

  public void setAccessToken(String accessToken) {
    this.access_token = accessToken;
  }
  
  public LoginRequest() {
    
  }
}
