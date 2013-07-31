package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.request.AbstractJSONRequest;

public class ShopExchangeInfo extends AbstractJSONRequest {

	@JsonProperty(value = "access_token")
	private String access_token;

	@JsonProperty(value = "gid")
	private String gid;

	@JsonProperty(value = "realname")
	private String realname;

	@JsonProperty(value = "delivery")
	private String delivery;

	@JsonProperty(value = "phone")
	private String phone;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
