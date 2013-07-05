package com.baidu.soushang.lbs;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class LBSClient {
  private static final String LBS_SERVER = "118.244.225.222";
  private static final int LBS_SERVER_PORT = 9300;
  private static LBSClient sInstance = null;
  
  private ClientBootstrap mClientBootstrap;
  private Channel mChannel;
  private LBSClientRequestClientHandler mHandler;
  
  private boolean mStartup = false;
  
  public static LBSClient getInstance() {
    if (sInstance == null) {
      sInstance = new LBSClient();
    }
    
    return sInstance;
  }
  
  public boolean startup() {
    if (!mStartup) {
      mClientBootstrap =
          new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
              Executors.newCachedThreadPool(), 1));
      mClientBootstrap.setPipelineFactory(new LBSClientPipelineFactory());
      ChannelFuture connectFuture =
          mClientBootstrap.connect(new InetSocketAddress(LBS_SERVER, LBS_SERVER_PORT));
      if (connectFuture.awaitUninterruptibly(1000)) {
        mChannel = connectFuture.getChannel();
        mHandler = mChannel.getPipeline().get(LBSClientRequestClientHandler.class);
        mStartup = true;
      }
    }

    return mStartup;
  }

  public void shutdown() {
    if (mStartup) {
      mChannel.close().awaitUninterruptibly();
      mClientBootstrap.releaseExternalResources();
    }
  }

}
