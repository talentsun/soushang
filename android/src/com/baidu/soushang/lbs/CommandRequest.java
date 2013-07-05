package com.baidu.soushang.lbs;

public class CommandRequest {
  public static final int FETCH_PEER_LIST_REQ = 1;
  public static final int FETCH_PEER_LIST_RESP = 2;
  public static final int FIGHT_REQ = 3;
  public static final int FIGHT_RESP = 4;
  public static final int QUESTION = 5;
  public static final int ANSWER = 6;
  public static final int FIGHT_STATE = 7;
  public static final int FIGHT_RESULT = 8;
  public static final int CLIENT_INFO = 9;
  public static final int CLIENT_LBS = 11;
  public static final int UNKNOWN_OP = 1000;
  public static final int HEARTBEAT = 1001;
  
  private int mCommandType;
  private int mCommandLength;
  private byte[] mCommand;
  
  public int getCommandType() {
    return mCommandType;
  }
  
  public int getCommandLength() {
    return mCommandLength;
  }
  
  public byte[] getCommand() {
    return mCommand;
  }
  
  public CommandRequest(int commandType, byte[] command) {
    mCommandType = commandType;
    mCommandLength = command.length;
    mCommand = command;
  }
}
