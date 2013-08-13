package com.baidu.soushang.lbs;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.baidu.soushang.lbs.Models.CommandMsg;

public class LBSClientPipelineFactory implements ChannelPipelineFactory {

  @Override
  public ChannelPipeline getPipeline() throws Exception {
    ChannelPipeline pipeline = pipeline();

    pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
    pipeline.addLast("protobufDecoder",
        new ProtobufDecoder(CommandMsg.getDefaultInstance()));
    pipeline.addLast("frameEncoder",
        new ProtobufVarint32LengthFieldPrepender());
    pipeline.addLast("protobufEncoder", new ProtobufEncoder());

    pipeline.addLast("handler", new LBSClientRequestClientHandler());

    return pipeline;
  }

}
