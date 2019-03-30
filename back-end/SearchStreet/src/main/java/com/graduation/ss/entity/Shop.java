package com.graduation.ss.entity;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "shop对象", description = "店铺对象shop")
public class Shop {
	@ApiModelProperty(value = "店铺ID，注册时不填，修改信息时必填")
	private Long shopId;
	@ApiModelProperty(value = "店铺名", required = true)
	private String shopName;
	@ApiModelProperty(value = "shop详情图", hidden = true)
	private List<ShopImg> shopImgList;
	@ApiModelProperty(value = "营业执照图片", hidden = true)
	private String businessLicenseImg;
	@ApiModelProperty(value = "营业执照编号")
	private String businessLicenseCode;
	@ApiModelProperty(value = "平均消费")
	private Integer perCost;
	@ApiModelProperty(value = "联系电话", required = true)
	private String phone;
	@ApiModelProperty(value = "省份", required = true)
	private String province;
	@ApiModelProperty(value = "城市", required = true)
	private String city;
	@ApiModelProperty(value = "地区", required = true)
	private String district;
	@ApiModelProperty(value = "详细地址", required = true)
	private String fullAddress;
	@ApiModelProperty(value = "补充地址")
	private String shopMoreInfo;
	// 1:是移动商铺，0：不是移动商铺
	@ApiModelProperty(value = "是否移动商铺", required = true)
	private Integer isMobile;
	@ApiModelProperty(value = "shop头像", hidden = true)
	private String profileImg;
	@ApiModelProperty(value = "纬度", required = true)
	private Float latitude;
	@ApiModelProperty(value = "经度", required = true)
	private Float longitude;
	// 0：审核中，1：许可，2：不许可
	@ApiModelProperty(value = "店铺状态", hidden = true)
	private Integer enableStatus;
	@ApiModelProperty(value = "营业范围", required = true)
	private String businessScope;
	@ApiModelProperty(value = "创建时间", hidden = true)
	private Date createTime;
	@ApiModelProperty(value = "最近修改时间", hidden = true)
	private Date lastEditTime;
	@ApiModelProperty(value = "用户ID，用token来获取", hidden = true)
	private Long userId;
	@ApiModelProperty(value = "服务评分平均分")
	private int serviceAvg;
	@ApiModelProperty(value = "星级评分平均分")
	private int starAvg;
	@ApiModelProperty(value = "搜街成功率，完成的订单数除以完成的订单数加取消的订单数")
	private int successRate;
	public Integer getPerCost() {
		return perCost;
	}

	public void setPerCost(Integer perCost) {
		this.perCost = perCost;
	}

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

	public List<ShopImg> getShopImgList() {
		return shopImgList;
	}

	public void setShopImgList(List<ShopImg> shopImgList) {
		this.shopImgList = shopImgList;
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

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
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

	public int getServiceAvg() {
		return serviceAvg;
	}

	public void setServiceAvg(int serviceAvg) {
		this.serviceAvg = serviceAvg;
	}

	public int getStarAvg() {
		return starAvg;
	}

	public void setStarAvg(int starAvg) {
		this.starAvg = starAvg;
	}


	public int getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(int successRate) {
		this.successRate = successRate;
	}

	@Override
	public String toString() {
		return "Shop [shopId=" + shopId + ", shopName=" + shopName + ", shopImgList=" + shopImgList
				+ ", businessLicenseImg=" + businessLicenseImg + ", businessLicenseCode=" + businessLicenseCode
				+ ", perCost=" + perCost + ", phone=" + phone + ", province=" + province + ", city=" + city
				+ ", district=" + district + ", fullAddress=" + fullAddress + ", shopMoreInfo=" + shopMoreInfo
				+ ", isMobile=" + isMobile + ", profileImg=" + profileImg + ", latitude=" + latitude + ", longitude="
				+ longitude + ", enableStatus=" + enableStatus + ", businessScope=" + businessScope + ", createTime="
				+ createTime + ", lastEditTime=" + lastEditTime + ", userId=" + userId + ", serviceAvg=" + serviceAvg
				+ ", starAvg=" + starAvg + ", successRate=" + successRate + "]";
	}



	

}
