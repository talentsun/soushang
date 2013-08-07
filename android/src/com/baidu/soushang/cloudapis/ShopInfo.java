package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class ShopInfo extends AbstractJSONResponse {
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private String mId;

	@JsonProperty(value = "title")
	private String mTitle;

	@JsonProperty(value = "integral")
	private String mIntegral;

	@JsonProperty(value = "image")
	private String mImage;

	@JsonProperty(value = "catid")
	private String mCatid;

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getIntegral() {
		return mIntegral;
	}

	public void setIntegral(String integral) {
		this.mIntegral = integral;
	}

	public String getImage() {
		return mImage;
	}

	public void setImage(String image) {
		this.mImage = image;
	}

	public String getCatid() {
		return mCatid;
	}

	public void setmCatid(String catid) {
		this.mCatid = catid;
	}

	public ShopInfo() {
		// TODO Auto-generated constructor stub
	}
}