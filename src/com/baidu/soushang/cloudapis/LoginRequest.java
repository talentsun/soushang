package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.request.AbstractJSONRequest;

public class LoginRequest extends AbstractJSONRequest {
  @JsonProperty(value="access_token")
  private String accessToken;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
  
  public LoginRequest() {
    
  }
}
