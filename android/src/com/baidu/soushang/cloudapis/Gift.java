package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class Gift  extends AbstractJSONResponse {
 
	@JsonProperty(value="userid")
	private String mUserId;
	
	@JsonProperty(value="username")
	private String mUserName;
	
	@JsonProperty(value="nums")
	private String mNums;
	
	@JsonProperty(value="addtime")
	private String mAddTime;
	
	@JsonProperty(value="catid")
	private String mCatid;
	
	@JsonProperty(value="title")
	private String mTitle;
	
	@JsonProperty(value="gif")
	private String mGif;
	
	@JsonProperty(value="thumb")
	private String mThumb;

	public String getmUserId() {
		return mUserId;
	}

	public void setUserId(String userId) {
		this.mUserId = userId;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		this.mUserName = userName;
	}

	public String getNums() {
		return mNums;
	}

	public void setNums(String nums) {
		this.mNums = nums;
	}

	public String getAddTime() {
		return mAddTime;
	}

	public void setAddTime(String addTime) {
		this.mAddTime = addTime;
	}

	public String getCatid() {
		return mCatid;
	}

	public void setCatid(String catid) {
		this.mCatid = catid;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getGif() {
		return mGif;
	}

	public void setGif(String gif) {
		this.mGif = gif;
	}

	public String getThumb() {
		return mThumb;
	}

	public void setThumb(String thumb) {
		this.mThumb = thumb;
	}
}
