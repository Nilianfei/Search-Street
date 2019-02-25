package com.graduation.ss.entity;

import java.util.Date;

public class ServiceImg {
	private Long serviceImgId;
	private String imgAddr;
	private Date createTime;
	private Long serviceId;
	public Long getServiceImgId() {
		return serviceImgId;
	}
	public void setServiceImgId(Long serviceImgId) {
		this.serviceImgId = serviceImgId;
	}
	public String getImgAddr() {
		return imgAddr;
	}
	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
}
