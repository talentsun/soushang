package com.baidu.soushang.lbs;

public class LBSClient {
  private static LBSClient sInstance = null;
  
  public static LBSClient getInstance() {
    if (sInstance == null) {
      sInstance = new LBSClient();
    }
    
    return sInstance;
  }
  
  public boolean startup() {
    return false;
  }
  
  public boolean shutdown() {
    return true;
  }

}
