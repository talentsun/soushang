package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class FeatureEventResponse extends CommonResponse {
  @JsonProperty(value="rooms")
  private List<FeatureEvent> mEvents;
  
  public List<FeatureEvent> getEvents() {
    return mEvents;
  }
  
  public void setEvents(List<FeatureEvent> events) {
    mEvents = events;
  }
  
  public FeatureEventResponse() {
    
  }
  
  public static class FeatureEvent extends AbstractJSONResponse {
    @JsonProperty(value="id")
    private int mId;
    
    public int getId() {
      return mId;
    }
    
    public void setId(int id) {
      mId = id;
    }
    
    @JsonProperty(value="pwd")
    private String mPwd;
    
    public String getPwd() {
      return mPwd;
    }
    
    public void setPwd(String pwd) {
      mPwd = pwd;
    }
    
    @JsonProperty(value="pnum")
    private int mPNum;
    
    public int getPNum() {
      return mPNum;
    }
    
    public void setPNum(int pNum) {
      mPNum = pNum;
    }
    
    @JsonProperty(value="title")
    private String mTitle;
    
    public String getTitle() {
      return mTitle;
    }
    
    public void setTitle(String title) {
      mTitle = title;
    }
    
    @JsonProperty(value="starttime")
    private long mStartTime;
    
    public long getStartTime() {
      return mStartTime;
    }
    
    public void setStartTime(long startTime) {
      mStartTime = startTime;
    }
    
    @JsonProperty(value="endtime")
    private long mEndTime;
    
    public long getEndTime() {
      return mEndTime;
    }
    
    public void setEndTime(long endTime) {
      mEndTime = endTime;
    }
    
    @JsonProperty(value="introduce")
    private String mIntroduce;
    
    public String getIntroduce() {
      return mIntroduce;
    }
    
    public void setIntroduce(String introduce) {
      mIntroduce = introduce;
    }
    
    @JsonProperty(value="running")
    private boolean mIsRunning;
    
    public boolean isRunning() {
      return mIsRunning;
    }
    
    public void setIsRunning(boolean isRunning) {
      mIsRunning = isRunning;
    }
    
    public FeatureEvent() {
      
    }
  }
}
