package com.graduation.ss.entity;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class ServiceImg {
	@ApiModelProperty(value = "服务图片Id", required = true)
	private Long serviceImgId;
	@ApiModelProperty(value = "图片地址", required = true)
	private String imgAddr;
	@ApiModelProperty(value = "图片上传时间")
	private Date createTime;
	@ApiModelProperty(value = "服务Id", required = true)
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
