package com.baidu.soushang.lbs;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

public class LBSClient {
  private static LBSClient sInstance = null;
  
  private ClientBootstrap mClientBootstrap;
  
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
