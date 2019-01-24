package com.graduation.ss.entity;

import java.util.Date;
import java.util.List;

public class Appeal {

	/**
	 * 求助id 主键
	 */
	private Long appealId;
	/**
	 * 求助者用户id
	 */
	private Long userId;
	private String appealTitle;
	private List<AppealImg> appealImgList;
	private String phone;
	private String appealContent;
	private String province;
	private String city;
	private String district;
	private String fullAddress;
	private String appealMoreInfo;
	private Long souCoin;
	/**
	 * 求助的状态 0不确定帮助对象，1已确定帮助对象，2已完成,3已删除
	 */
	private Integer appealStatus;
	private Float latitude;
	private Float longitude;
	private Date startTime;
	private Date endTime;

	public Long getAppealId() {
		return appealId;
	}

	public void setAppealId(Long appealId) {
		this.appealId = appealId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAppealTitle() {
		return appealTitle;
	}

	public void setAppealTitle(String appealTitle) {
		this.appealTitle = appealTitle;
	}

	public List<AppealImg> getAppealImgList() {
		return appealImgList;
	}

	public void setAppealImgList(List<AppealImg> appealImgList) {
		this.appealImgList = appealImgList;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAppealContent() {
		return appealContent;
	}

	public void setAppealContent(String appealContent) {
		this.appealContent = appealContent;
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

	public String getAppealMoreInfo() {
		return appealMoreInfo;
	}

	public void setAppealMoreInfo(String appealMoreInfo) {
		this.appealMoreInfo = appealMoreInfo;
	}

	public Long getSouCoin() {
		return souCoin;
	}

	public void setSouCoin(Long souCoin) {
		this.souCoin = souCoin;
	}

	public Integer getAppealStatus() {
		return appealStatus;
	}

	public void setAppealStatus(Integer appealStatus) {
		this.appealStatus = appealStatus;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
