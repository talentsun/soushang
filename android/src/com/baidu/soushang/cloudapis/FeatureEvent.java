package com.baidu.soushang.cloudapis;

public class FeatureEvent {
  private int mStartTime;
  private int mEndTime;
  private boolean mRunning;
  private boolean mFinished;
  private int mId;
  private int mPnum;
  private String mTitle;
  private String mIntroduce;

  public int getStartTime() {
    return mStartTime;
  }

  public void setmStartTime(int startTime) {
    this.mStartTime = startTime;
  }

  public int getEndTime() {
    return mEndTime;
  }

  public void setmEndTime(int endTime) {
    this.mEndTime = endTime;
  }

  public boolean isRunning() {
    return mRunning;
  }

  public void setRunning(boolean running) {
    this.mRunning = running;
  }

  public boolean isFinished() {
    return mFinished;
  }

  public void setFinished(boolean finished) {
    this.mFinished = finished;
  }

  public int getId() {
    return mId;
  }

  public void setId(int mId) {
    this.mId = mId;
  }

  public int getPnum() {
    return mPnum;
  }

  public void setPnum(int pnum) {
    this.mPnum = pnum;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String title) {
    this.mTitle = title;
  }

  public String getIntroduce() {
    return mIntroduce;
  }

  public void setmIntroduce(String introduce) {
    this.mIntroduce = introduce;
  }

}