package com.graduation.ss.entity;

import java.util.Date;

public class ShopImg {
	private Long shopImgId;
	private String imgAddr;
	private Date createTime;
	private Long shopId;
	public Long getShopImgId() {
		return shopImgId;
	}
	public void setShopImgId(Long shopImgId) {
		this.shopImgId = shopImgId;
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
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
}
