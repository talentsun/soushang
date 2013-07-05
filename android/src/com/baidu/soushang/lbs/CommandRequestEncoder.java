package com.baidu.soushang.lbs;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public class CommandRequestEncoder extends OneToOneDecoder {

  @Override
  protected Object decode(ChannelHandlerContext arg0, Channel arg1, Object arg2) throws Exception {
    if (arg2 instanceof CommandRequest) {
      CommandRequest request = (CommandRequest) arg2;
      
      ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
      buffer.writeShort(request.getCommandType());
      buffer.writeShort(request.getCommandLength());
      if (request.getCommandLength() > 0) {
        buffer.writeBytes(request.getCommand());
      }
      
      return buffer;
    }
    
    return arg2;
  }

}
