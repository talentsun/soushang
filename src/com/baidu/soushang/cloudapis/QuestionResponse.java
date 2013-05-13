package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class QuestionResponse extends AbstractJSONResponse {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value="id")
	private int id;
	
	@JsonProperty(value="catid")
	private int catId;
	
	@JsonProperty(value="title")
	private String title;
	
	@JsonProperty(value="answertime")
	private int answerTime;
	
	@JsonProperty(value="difficulty")
	private int difficulty;
	
	@JsonProperty(value="pnumber")
	private int pnumber;
	
	@JsonProperty(value="average")
	private int average;
	
	@JsonProperty(value="typeid")
	private int typeId;
	
	@JsonProperty(value="integral")
	private int integral;
	
	@JsonProperty(value="addtime")
	private long addTime;
	
	@JsonProperty(value="isdo")
	private boolean isDone;
	
	@JsonProperty(value="updatetime")
	private long updateTime;
	
	@JsonProperty(value="rightanswer")
	private int rightAnswer;
	
	@JsonProperty(value="userid")
	private long userId;
	
	@JsonProperty(value="options")
	private List<String> options;
	
	@JsonProperty(value="status")
	private int status;
	
	@JsonProperty(value="sendnum")
	private int sendNum;
	
	@JsonProperty(value="total")
	private int total;
	
	@JsonProperty(value="score")
	private int score;
	
	@JsonProperty(value="username")
	private String userName;
	
	@JsonProperty(value="videourl")
	private String videoUrl;
	
	@JsonProperty(value="audiourl")
	private String audioUrl;
	
	@JsonProperty(value="imageurl")
	private String imageUrl;
	
	@JsonProperty(value="bonusPoint")
	private String bonusPoint;
	
	@JsonProperty(value="type")
	private String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(int answerTime) {
		this.answerTime = answerTime;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getPnumber() {
		return pnumber;
	}

	public void setPnumber(int pnumber) {
		this.pnumber = pnumber;
	}

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(int rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSendNum() {
		return sendNum;
	}

	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBonusPoint() {
		return bonusPoint;
	}

	public void setBonusPoint(String bonusPoint) {
		this.bonusPoint = bonusPoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public QuestionResponse() {
		
	}
}
