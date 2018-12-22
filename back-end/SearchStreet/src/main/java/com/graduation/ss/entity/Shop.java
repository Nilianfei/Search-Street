package com.graduation.ss.entity;

import java.util.Date;

public class Shop {
	private Long shopId;
	private String shopName;
	private String shopImg;
	private String businessLicenseImg;
	private String businessLicenseCode;
	private String phone;
	private String province;
	private String city;
	private String district;
	private String fullAddress;
	private String shopMoreInfo;
	//1:是移动商铺，0：不是移动商铺
	private Integer isMobile;
	private Date openTime;
	private Date closeTime;
	private String profileImg;
	private Float coordinateX;
	private Float coordinateY;
	//0：审核中，1：许可，2：不许可
	private Integer enableStatus;
	private String businessScope;
	private Date createTime;
	private Date lastEditTime;
	private Long userId;
	
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopImg() {
		return shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}
	public String getBusinessLicenseImg() {
		return businessLicenseImg;
	}
	public void setBusinessLicenseImg(String businessLicenseImg) {
		this.businessLicenseImg = businessLicenseImg;
	}
	public String getBusinessLicenseCode() {
		return businessLicenseCode;
	}
	public void setBusinessLicenseCode(String businessLicenseCode) {
		this.businessLicenseCode = businessLicenseCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public String getShopMoreInfo() {
		return shopMoreInfo;
	}
	public void setShopMoreInfo(String shopMoreInfo) {
		this.shopMoreInfo = shopMoreInfo;
	}
	public Integer getIsMobile() {
		return isMobile;
	}
	public void setIsMobile(Integer isMobile) {
		this.isMobile = isMobile;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public Float getCoordinateX() {
		return coordinateX;
	}
	public void setCoordinateX(Float coordinateX) {
		this.coordinateX = coordinateX;
	}
	public Float getCoordinateY() {
		return coordinateY;
	}
	public void setCoordinateY(Float coordinateY) {
		this.coordinateY = coordinateY;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
