package com.graduation.ss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCode2Session {
	@JsonProperty("openid")
	private String openId;
	@JsonProperty("session_key")
	private String session_key;
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getSession_key() {
		return session_key;
	}
	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}
	@Override
	public String toString() {
		return "UserCode2Session [openId=" + openId + ", session_key=" + session_key + "]";
	}
}
