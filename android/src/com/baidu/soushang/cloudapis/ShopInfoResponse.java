package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;

public class ShopInfoResponse extends CommonResponse {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "gifts")
  private List<ShopInfo> mGifts;

  public List<ShopInfo> getGifts() {
    return mGifts;
  }

  public void setGifts(List<ShopInfo> gifts) {
    this.mGifts = gifts;
  }

  public ShopInfoResponse() {
    // TODO Auto-generated constructor stub
  }

}
