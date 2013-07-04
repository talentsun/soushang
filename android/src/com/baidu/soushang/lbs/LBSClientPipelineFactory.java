package com.baidu.soushang.lbs;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;

public class LBSClientPipelineFactory implements ChannelPipelineFactory {

  @Override
  public ChannelPipeline getPipeline() throws Exception {
    ChannelPipeline pipeline = pipeline();
//    pipeline.addLast("encoder", "")
    return null;
  }

}
