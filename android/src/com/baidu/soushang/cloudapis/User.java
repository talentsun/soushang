package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class User extends AbstractJSONResponse {
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "user_id")
	private String userId;

	@JsonProperty(value = "user_name")
	private String username;

	@JsonProperty(value = "point")
	private int point;

	@JsonProperty(value = "integral")
	private int integral;


	@JsonProperty(value = "fight_num")
	private String mFightNum;

	@JsonProperty(value="win_num")
	private String mWinNum;
	
	// @JsonProperty(value = "win_ratio")
	// private int mWinRatio;

	@JsonProperty(value = "user_rank")
	private int mUserRank;
	
	@JsonProperty(value="gifts")
	private List<Gift> gifts;

	public List<Gift> getGifts() {
		return gifts;
	}

	public void setGifts(List<Gift> gifts) {
		this.gifts = gifts;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFightNum() {
		return mFightNum;
	}

	public void setFightNum(String fightNum) {
		this.mFightNum = fightNum;
	}

	public String getWinNum() {
		return mWinNum;
	}

	public void setWinNum(String winNum) {
		this.mWinNum = winNum;
	}

	// public float getWinRatio() {
	// return mWinRatio;
	// }
	//
	// public void setWinRatio(int winRatio) {
	// this.mWinRatio = winRatio;
	// }

	public int getUserRank() {
		return mUserRank;
	}

	public void setUserRank(int userRank) {
		this.mUserRank = userRank;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	   
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public User() {

	}
}
