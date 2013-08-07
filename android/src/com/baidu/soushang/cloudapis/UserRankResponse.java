package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;

public class UserRankResponse extends CommonResponse {
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "users")
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public UserRankResponse() {

	}

}
