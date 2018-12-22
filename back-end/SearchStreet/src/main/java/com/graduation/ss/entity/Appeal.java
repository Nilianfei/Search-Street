package com.graduation.ss.entity;

import java.util.Date;

public class Appeal {

		/**
		 * 求助者id 主键
		 */
		private Long appealId;
		/**
		 * 用户id Foreign key
		 */
		private Long userId;
		private String appealImg;
		private String appealTitle;
		private String appealDesc;
		private String appealContent;
		private String province;
		private String city;
		private String district;
		private String fullAddress;
		private Long souCoin;
		/**
		 * 求助的状态
		 * 0不确定帮助对象，1已确定帮助对象，2已完成,3已删除
		 */
		private Integer appealStatus;
		private Float coordinateX;
		private Float coordinateY;
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


		public String getAppealImg() {
			return appealImg;
		}


		public void setAppealImg(String appealImg) {
			this.appealImg = appealImg;
		}


		public String getAppealTitle() {
			return appealTitle;
		}


		public void setAppealTitle(String appealTitle) {
			this.appealTitle = appealTitle;
		}


		public String getAppealDesc() {
			return appealDesc;
		}


		public void setAppealDesc(String appealDesc) {
			this.appealDesc = appealDesc;
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
