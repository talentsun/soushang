package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class FeatureEventResponse extends AbstractJSONResponse {

	@JsonProperty(value = "ret_code")
	protected int retCode;

	@JsonProperty(value = "ret_msg")
	protected String retMsg;

	@JsonProperty(value = "rooms")
	private List<FeatureEvent> mEvents;

	public List<FeatureEvent> getEvents() {
		return mEvents;
	}

	public void setEvents(List<FeatureEvent> events) {
		mEvents = events;
	}

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public FeatureEventResponse() {
	}

}
