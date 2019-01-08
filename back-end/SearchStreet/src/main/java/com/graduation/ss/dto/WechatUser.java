package com.graduation.ss.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WechatUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7730211303425177715L;
	// js_code临时凭证
	@JsonProperty("code")
	private String code;
	@JsonProperty("userInfo")
	private Map<String, String> userInfo;
	
	public Map<String, String> getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(Map<String, String> userInfo) {
		this.userInfo = userInfo;
	}
	public String getNickName() {
		return userInfo.get("nickName");
	}
	
	public String getProvince() {
		return userInfo.get("province");
	}
	
	public String getCity() {
		return userInfo.get("city");
	}
	
	public String getCountry() {
		return userInfo.get("country");
	}
	
	public String getCode() {
		return code;
	}
	
	public String getGender() {
		return userInfo.get("gender");
	}
	
	public String getAvatarUrl() {
		return userInfo.get("avatarUrl");
	}
	
}
