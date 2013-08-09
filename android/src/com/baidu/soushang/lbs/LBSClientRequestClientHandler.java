package com.baidu.soushang.lbs;

import java.util.List;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import android.util.Log;

import com.baidu.soushang.lbs.Models.CommandMsg;
import com.baidu.soushang.lbs.Models.EmptyMsg;
import com.baidu.soushang.lbs.Models.IAnswer;
import com.baidu.soushang.lbs.Models.IClientInfo;
import com.baidu.soushang.lbs.Models.IClientLBS;
import com.baidu.soushang.lbs.Models.IFightReq;
import com.baidu.soushang.lbs.Models.IFightResp;
import com.baidu.soushang.lbs.Models.OFightReq;
import com.baidu.soushang.lbs.Models.OFightResp;
import com.baidu.soushang.lbs.Models.OFightResult;
import com.baidu.soushang.lbs.Models.OFightState;
import com.baidu.soushang.lbs.Models.OPeerListResp;
import com.baidu.soushang.lbs.Models.OQuestion;
import com.baidu.soushang.lbs.Models.User;

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
	private static final int ONLINE = 14;
	private static final int OFFLINE = 15;
	private static final int LOGIN_FAIL = 17;
	private static final int HEARTBEAT = 1001;

	public interface ClientListener {
		public void onClosed();

		public void onPeersUpdated(List<User> peers);

		public void onFightReq(User peer, int bet);

		public void onLoginFail();

		public void onFightResp(int result);

		public void onFightCancel();

		public void onFightBegin(String fightKey);

		public void onFighting(int right, int done, int total);

		public void onFightEnd(int result, int myPoint, int myTime,
				int myPointDelta, int myWinRate, int otherPoint, int otherTime);
	}

	private ClientListener mListener;

	public void setClientListener(ClientListener listener) {
		mListener = listener;
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		Log.d(TAG, "channel closed");
		if (mListener != null) {
			mListener.onClosed();
		}
		super.channelClosed(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext arg0, ExceptionEvent arg1)
			throws Exception {
		Log.e(TAG, arg1.toString());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Log.d(TAG, "message received");

		if (e.getMessage() != null && e.getMessage() instanceof CommandMsg) {
			CommandMsg msg = (CommandMsg) e.getMessage();
			Log.d(TAG, msg.getType() + "");
			switch (msg.getType()) {
			case FETCH_PEER_LIST_RESP:
				OPeerListResp resp = OPeerListResp.parseFrom(msg.getContent());
				if (mListener != null) {
					mListener.onPeersUpdated(resp.getUsersList());
				}
				break;
			case FIGHT_REQ:
				OFightReq fightReq = OFightReq.parseFrom(msg.getContent());
				if (mListener != null) {
					mListener.onFightReq(fightReq.getUser(), fightReq.getBet());
				}
				break;
			case FIGHT_RESP:
				OFightResp fightResp = OFightResp.parseFrom(msg.getContent());
				if (mListener != null) {
					mListener.onFightResp(fightResp.getResult());
				}
				break;
			case FIGHT_CANCEL:
				if (mListener != null) {
					mListener.onFightCancel();
				}
				break;
			case QUESTION:
				OQuestion questionResp = OQuestion.parseFrom(msg.getContent());
				if (mListener != null) {
					mListener.onFightBegin(questionResp.getFightKey());
				}
				break;
			case FIGHT_STATE:
				OFightState fightState = OFightState
						.parseFrom(msg.getContent());
				if (mListener != null) {
					mListener.onFighting(fightState.getRight(),
							fightState.getDone(), fightState.getAll());
				}
				break;
			case FIGHT_RESULT:
				OFightResult fightResult = OFightResult.parseFrom(msg
						.getContent());
				if (mListener != null) {
					mListener.onFightEnd(fightResult.getResult(),
							fightResult.getMePoint(), fightResult.getMeTime(),
							fightResult.getMeScore(),
							(int) (fightResult.getMeWinRatio() * 100),
							fightResult.getOtherPoint(),
							fightResult.getOtherTime());
				}
				break;

			case LOGIN_FAIL:

				if (mListener != null) {
					mListener.onLoginFail();
				}

				break;

			}
		}

		super.messageReceived(ctx, e);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
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

	public void sendFightReq(long peerId, int bet) {
		Log.d(TAG, "send fight req");

		IFightReq.Builder builder = IFightReq.newBuilder();
		builder.setId(peerId);
		builder.setBet(bet);

		CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
		msgBuilder.setType(FIGHT_REQ);
		msgBuilder.setContent(builder.build().toByteString());

		mChannel.write(msgBuilder.build());
	}

	public void sendFightCancel() {
		Log.d(TAG, "send fight cancel");

		CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
		msgBuilder.setType(FIGHT_CANCEL);
		msgBuilder.setContent(EmptyMsg.newBuilder().build().toByteString());

		mChannel.write(msgBuilder.build());
	}

	public void sendOnline() {
		Log.d(TAG, "send online");

		CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
		msgBuilder.setType(ONLINE);
		msgBuilder.setContent(EmptyMsg.newBuilder().build().toByteString());

		mChannel.write(msgBuilder.build());
	}

	public void sendOffline() {
		Log.d(TAG, "send offline");

		CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
		msgBuilder.setType(OFFLINE);
		msgBuilder.setContent(EmptyMsg.newBuilder().build().toByteString());

		mChannel.write(msgBuilder.build());
	}

	public void sendFightResp(int result) {
		Log.d(TAG, "send fight resp");

		IFightResp.Builder builder = IFightResp.newBuilder();
		builder.setResult(result);

		CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
		msgBuilder.setType(FIGHT_RESP);
		msgBuilder.setContent(builder.build().toByteString());

		mChannel.write(msgBuilder.build());
	}

	public void sendClientInfo(long userId, String username, String avatar,
			int networkType) {
		Log.d(TAG, "send client info");

		IClientInfo.Builder builder = IClientInfo.newBuilder();
		builder.setName(username);
		builder.setId(userId);
		builder.setAvatar(avatar);
		builder.setNetType(networkType);

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

	public void sendAnswer(int index, int right) {
		Log.d(TAG, "send answer");

		IAnswer.Builder builder = IAnswer.newBuilder();
		builder.setIndex(index);
		builder.setRight(right);

		CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
		msgBuilder.setType(ANSWER);
		msgBuilder.setContent(builder.build().toByteString());

		mChannel.write(msgBuilder.build());
	}

	public void sendFightQuit() {
		Log.d(TAG, "send fight quit");

		CommandMsg.Builder msgBuilder = CommandMsg.newBuilder();
		msgBuilder.setType(FIGHT_QUIT);
		msgBuilder.setContent(EmptyMsg.newBuilder().build().toByteString());

		mChannel.write(msgBuilder.build());
	}
}
