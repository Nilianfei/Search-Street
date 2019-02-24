package com.graduation.ss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.graduation.ss.entity.WechatAuth;

public class WechatAuthAndEnableStatus {
	@JsonProperty("WechatAuth")
	private WechatAuth wechatAuth;
	@JsonProperty("enableStatus")
	private int enableStatus;

	public WechatAuth getWechatAuth() {
		return wechatAuth;
	}

	public void setWechatAuth(WechatAuth wechatAuth) {
		this.wechatAuth = wechatAuth;
	}

	public int getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(int enableStatus) {
		this.enableStatus = enableStatus;
	}

}
