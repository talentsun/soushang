package com.baidu.soushang.lbs;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.baidu.soushang.lbs.Models.IAnswer;
import com.baidu.soushang.lbs.Models.IClientInfo;
import com.baidu.soushang.lbs.Models.IClientLBS;
import com.baidu.soushang.lbs.Models.IFightReq;
import com.baidu.soushang.lbs.Models.IFightResp;

public class LBSClientRequestClientHandler extends SimpleChannelUpstreamHandler {
  private volatile Channel mChannel;

  @Override
  public void exceptionCaught(ChannelHandlerContext arg0, ExceptionEvent arg1) throws Exception {
    // TODO Auto-generated method stub
    super.exceptionCaught(arg0, arg1);
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    // TODO Auto-generated method stub
    super.messageReceived(ctx, e);
  }
  
  @Override
  public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    mChannel = e.getChannel();
    super.channelOpen(ctx, e);
  }

  public void sendHeartbeat() {
    mChannel.write(new CommandRequest(CommandRequest.HEARTBEAT, new byte[] {}));
  }
  
  public void sendFetchList() {
    mChannel.write(new CommandRequest(CommandRequest.FETCH_PEER_LIST_REQ, new byte[] {}));
  }
  
  public void sendFightReq(int peerId) {
    IFightReq.Builder builder = IFightReq.newBuilder();
    builder.setId(peerId);
    mChannel.write(new CommandRequest(CommandRequest.FIGHT_REQ, builder.build().toByteArray()));
  }
  
  public void sendFightResp(boolean accept) {
    IFightResp.Builder builder = IFightResp.newBuilder();
    builder.setResult(accept ? 1 : 0);
    mChannel.write(new CommandRequest(CommandRequest.FIGHT_RESP, builder.build().toByteArray()));
  }
  
  public void sendClientInfo(String clientName) {
    IClientInfo.Builder builder = IClientInfo.newBuilder();
    builder.setName(clientName);
    mChannel.write(new CommandRequest(CommandRequest.CLIENT_INFO, builder.build().toByteArray()));
  }
  
  public void sendLBSInfo(float longitude, float latitude) {
    IClientLBS.Builder builder = IClientLBS.newBuilder();
    builder.setLongitude(longitude);
    builder.setLatitude(latitude);
    mChannel.write(new CommandRequest(CommandRequest.CLIENT_LBS, builder.build().toByteArray()));
  }

  public void sendAnswer(int questionIndex, int choice) {
    IAnswer.Builder builder = IAnswer.newBuilder();
    builder.setIndex(questionIndex);
    builder.setChoose(choice);
    mChannel.write(new CommandRequest(CommandRequest.ANSWER, builder.build().toByteArray()));
  }
}
