package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class FeatureEvent extends AbstractJSONResponse {
	private static final long serialVersionUID = 1L;

//	@JsonProperty(value = "starttime")
//	private long startTime;
//
//	@JsonProperty(value = "endtime")
//	private long endTime;
//   
//	@JsonProperty(value = "running")
//	private boolean running;

	@JsonProperty(value = "id")
	private int id;

//	@JsonProperty(value = "pnum")
//	private int pNum;
//
//	@JsonProperty(value = "title")
//	private String title;
//
//	@JsonProperty(value = "introduce")
//	private String introduce;

  

//	public long getStartTime() {
//		return startTime;
//	}
//
//
//
//	public void setStartTime(long startTime) {
//		this.startTime = startTime;
//	}
//
//
//
//	public long getEndTime() {
//		return endTime;
//	}
//
//
//
//	public void setEndTime(long endTime) {
//		this.endTime = endTime;
//	}
//
//
//
//	public boolean isRunning() {
//		return running;
//	}
//
//
//
//	public void setRunning(boolean running) {
//		this.running = running;
//	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



//	public int getpNum() {
//		return pNum;
//	}
//
//
//
//	public void setpNum(int pNum) {
//		this.pNum = pNum;
//	}
//
//
//
//	public String getTitle() {
//		return title;
//	}
//
//
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//
//
//	public String getIntroduce() {
//		return introduce;
//	}
//
//
//
//	public void setIntroduce(String introduce) {
//		this.introduce = introduce;
//	}



	public FeatureEvent() {
	}
}
