package com.baidu.soushang.lbs;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import android.util.Log;

import com.baidu.soushang.lbs.Models.CommandMsg;
import com.baidu.soushang.lbs.Models.EmptyMsg;
import com.baidu.soushang.lbs.Models.IClientInfo;
import com.baidu.soushang.lbs.Models.IClientLBS;
import com.baidu.soushang.lbs.Models.IFightReq;
import com.baidu.soushang.lbs.Models.IFightResp;

public class LBSClientRequestClientHandler extends SimpleChannelUpstreamHandler {
  private volatile Channel mChannel;
  
  private static final String TAG = "lbs";
  
  private static final int FETCH_PEER_LIST_REQ = 1;
  private static final int FETCH_PEER_LIST_RESP = 2;
  private static final int FIGHT_REQ = 3;
  private static final int FIGHT_RESP = 4;
  private static final int QUESTION = 5;
  private static final int ANSWER = 6;
  private static final int FIGHT_STATE = 7;
  private static final int FIGHT_RESULT = 8;
  private static final int CLIENT_INFO = 9;
  private static final int CLIENT_LBS = 11;
  private static final int FIGHT_CANCEL = 12;
  private static final int FIGHT_QUIT = 13;
  private static final int UNKNOWN_OP = 1000;
  private static final int HEARTBEAT = 1001;
  
  public interface ClientListener {
    public void onClosed();
  }
  
  private ClientListener mListener;
  
  public void setClientListener(ClientListener listener) {
    mListener = listener;
  }

  @Override
  public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    Log.d(TAG, "channel closed");
    if (mListener != null) {
      mListener.onClosed();
    }
    super.channelClosed(ctx, e);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext arg0, ExceptionEvent arg1) throws Exception {
    Log.e(TAG, arg1.toString());
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    Log.d(TAG, "message received");
    
    if (e.getMessage() != null && e.getMessage() instanceof CommandMsg) {
      CommandMsg msg = (CommandMsg) e.getMessage();
      Log.d(TAG, "" + msg.getType());
    }
    
    super.messageReceived(ctx, e);
  }
  
  @Override
  public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    Log.d(TAG, "channel open");
    mChannel = e.getChannel();
    super.channelOpen(ctx, e);
  }

  public void sendHeartbeat() {
    Log.d(TAG, "heartbeat");
    
    CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
    msgBuilder.setType(HEARTBEAT);
    msgBuilder.setContent(EmptyMsg.newBuilder().build().toByteString());

    mChannel.write(msgBuilder.build());
  }
  
  public void sendFetchPeerListReq() {
    Log.d(TAG, "send fetch peer list req");
    
    CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
    msgBuilder.setType(FETCH_PEER_LIST_REQ);
    msgBuilder.setContent(EmptyMsg.newBuilder().build().toByteString());
    
    mChannel.write(msgBuilder);
  }
  
  public void sendFightReq(int peerId) {
    Log.d(TAG, "send fight req");
    
    IFightReq.Builder builder = IFightReq.newBuilder();
    builder.setId(peerId);
    
    CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
    msgBuilder.setType(FIGHT_REQ);
    msgBuilder.setContent(builder.build().toByteString());
    
    mChannel.write(msgBuilder.build());
  }
  
  public void sendFightResp(boolean accept) {
    Log.d(TAG, "send fight resp");
    
    IFightResp.Builder builder = IFightResp.newBuilder();
    builder.setResult(accept ? 1 : 0);
    
    CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
    msgBuilder.setType(FIGHT_REQ);
    msgBuilder.setContent(builder.build().toByteString());
    
    mChannel.write(msgBuilder.build());
  }
  
  public void sendClientInfo(String clientName) {
    Log.d(TAG, "send client info");
    
    IClientInfo.Builder builder = IClientInfo.newBuilder();
    builder.setName(clientName);
    
    CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
    msgBuilder.setType(CLIENT_INFO);
    msgBuilder.setContent(builder.build().toByteString());
    
    mChannel.write(msgBuilder.build());
  }
  
  public void sendLBSInfo(float longitude, float latitude) {
    Log.d(TAG, "send lbs info");
    
    IClientLBS.Builder builder = IClientLBS.newBuilder();
    builder.setLongitude(longitude);
    builder.setLatitude(latitude);
    
    CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
    msgBuilder.setType(CLIENT_LBS);
    msgBuilder.setContent(builder.build().toByteString());
    
    mChannel.write(msgBuilder.build());
  }
}
