package com.baidu.soushang.lbs;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import android.content.Context;

public class LBSClientPipelineFactory implements ChannelPipelineFactory {

  @Override
  public ChannelPipeline getPipeline() throws Exception {
    ChannelPipeline pipeline = pipeline();
    
    pipeline.addLast("encoder", new CommandRequestEncoder());
    pipeline.addLast("handler", new LBSClientRequestClientHandler());
    
    return pipeline;
  }

}
