package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class CommonResponse extends AbstractJSONResponse {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "ret_code")
  protected int retCode;

  @JsonProperty(value = "ret_msg")
  protected String retMsg;

  public int getRetCode() {
    return retCode;
  }

  public void setRetCode(int retCode) {
    this.retCode = retCode;
  }

  public String getRetMsg() {
    return retMsg;
  }

  public void setRetMsg(String retMsg) {
    this.retMsg = retMsg;
  }

  public CommonResponse() {

  }
}
