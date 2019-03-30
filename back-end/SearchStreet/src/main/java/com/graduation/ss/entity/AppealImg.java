package com.graduation.ss.entity;

import java.util.Date;

public class AppealImg {
	private Long appealImgId;
	private String imgAddr;
	private Long appealId;
	private Date createTime;

	public Long getAppealImgId() {
		return appealImgId;
	}

	public void setAppealImgId(Long appealImgId) {
		this.appealImgId = appealImgId;
	}

	public String getImgAddr() {
		return imgAddr;
	}

	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}

	public Long getAppealId() {
		return appealId;
	}

	public void setAppealId(Long appealId) {
		this.appealId = appealId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
