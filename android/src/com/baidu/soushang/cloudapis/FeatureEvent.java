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
  private String mCat;
  private int mScore;
  private boolean mIsPractice;
  private boolean mIsStartPoint;

  public boolean isIsStartPoint() {
    return mIsStartPoint;
  }

  public void setIsStartPoint(boolean isStartPoint) {
    this.mIsStartPoint = isStartPoint;
  }

  public boolean isIsPractice() {
    return mIsPractice;
  }

  public void setIsPractice(boolean isPractice) {
    this.mIsPractice = isPractice;
  }

  public String getCat() {
    return mCat;
  }

  public void setCat(String cat) {
    this.mCat = cat;
  }

  public int getScore() {
    return mScore;
  }

  public void setScore(int score) {
    this.mScore = score;
  }

  public int getStartTime() {
    return mStartTime;
  }

  public void setStartTime(int startTime) {
    this.mStartTime = startTime;
  }

  public int getEndTime() {
    return mEndTime;
  }

  public void setEndTime(int endTime) {
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

  public void setId(int id) {
    this.mId = id;
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

  public void setIntroduce(String introduce) {
    this.mIntroduce = introduce;
  }

}
